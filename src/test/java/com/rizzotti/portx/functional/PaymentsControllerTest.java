package com.rizzotti.portx.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rizzotti.portx.application.rest.PaymentsController;
import com.rizzotti.portx.domain.Account;
import com.rizzotti.portx.domain.Customer;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.domain.service.PaymentService;
import com.rizzotti.portx.exception.CustomErrorException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.rizzotti.portx.utils.Constants.IDEMPOTENT_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentsController.class)
public class PaymentsControllerTest {


    @MockBean
    PaymentService paymentService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void itShouldReturnPayment() throws Exception {
        Payment payment = createPayment();

        when(paymentService.savePayment(any(Payment.class), any())).thenReturn(payment);
        mockMvc.perform(post("/payments")
                .content(mapper.writeValueAsString(payment))
                .contentType(MediaType.APPLICATION_JSON)
                .header(IDEMPOTENT_KEY, UUID.randomUUID().toString()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(payment.getId()));
    }

    @Test
    public void itShouldReturnPaymentErrorIfHeaderDoesNotIncludeIdempotentKey() throws Exception {
        Payment payment = createPayment();

        mockMvc.perform(post("/payments")
                        .content(mapper.writeValueAsString(payment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("idempotent-key not found in request headers"));
    }

    @Test
    public void itShouldReturnPaymentErrorIfAnErrorOccursInDB() throws Exception {
        Payment payment = createPayment();

        when(paymentService.savePayment(any(Payment.class), any())).thenThrow(new CustomErrorException("method error"));
        mockMvc.perform(post("/payments")
                        .content(mapper.writeValueAsString(payment))
                        .contentType(MediaType.APPLICATION_JSON)
                .header(IDEMPOTENT_KEY, UUID.randomUUID().toString()))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("method error"));
    }

    @Test
    public void itShouldReturnPaymentIfItExistInDB() throws Exception {
        Payment payment = createPayment();

        when(paymentService.checkStatus(anyInt())).thenReturn(Optional.of(payment));
        mockMvc.perform(get("/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IDEMPOTENT_KEY, UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payment.getId()));
    }

    @Test
    public void itShouldReturnEmptyIfPaymentDoesNotExistInDB() throws Exception {
        when(paymentService.checkStatus(anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(get("/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(IDEMPOTENT_KEY, UUID.randomUUID().toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }


    private Payment createPayment(){
        Payment payment = new Payment();
        payment.setId(1);
        payment.setAmount(new BigDecimal(100));
        payment.setBeneficiary(new Customer("mockBeneficiaryName", "mockBeneficiaryId"));
        payment.setOriginator(new Customer("mockOriginatorName", "mockOriginatorId"));
        payment.setReceiver(new Account("mockReceiverAccountType()", "mockReceiverAccountNumber()"));
        payment.setSender(new Account("mockSenderAccountType()", "mockSenderAccountNumber()"));
        payment.setCurrency("mockCurrency");
        payment.setStatus("CREATED");
        return payment;
    }

}
