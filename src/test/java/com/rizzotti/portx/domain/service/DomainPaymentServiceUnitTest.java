package com.rizzotti.portx.domain.service;

import com.rizzotti.portx.domain.Account;
import com.rizzotti.portx.domain.Customer;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.domain.repository.PaymentRepository;
import com.rizzotti.portx.exception.CustomErrorException;
import org.hibernate.metamodel.model.convert.spi.Converters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.rizzotti.portx.utils.Constants.IDEMPOTENT_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DomainPaymentServiceUnitTest {

    private PaymentRepository paymentRepository;
    private ProducerService producerService;
    private DomainPaymentService domainPaymentService;

    @BeforeEach
    void setUp(){
        paymentRepository = mock(PaymentRepository.class);
        producerService = mock(ProducerService.class);
        domainPaymentService = new DomainPaymentService(paymentRepository, producerService);
    }

    @Test
    void shouldReturnPaymentIfExistInDb(){
        Payment payment = createPayment();
        when(paymentRepository.checkStatus(anyInt())).thenReturn(Optional.of(payment));

        Optional<Payment> returnedPayment = domainPaymentService.checkStatus(1);

        verify(paymentRepository).checkStatus(anyInt());
        assertTrue(returnedPayment.isPresent());
    }

    @Test
    void shouldNotReturnPaymentIfExistInDb(){
        when(paymentRepository.checkStatus(anyInt())).thenReturn(Optional.empty());

        Optional<Payment> returnedPayment = domainPaymentService.checkStatus(1);

        verify(paymentRepository).checkStatus(anyInt());
        assertFalse(returnedPayment.isPresent());
    }

    @Test
    void shouldCreatePayment() throws CustomErrorException {
        String uuid = UUID.randomUUID().toString();
        Payment payment = createPayment();
        Map<String, String> headers = Map.of(IDEMPOTENT_KEY, uuid);
        when(paymentRepository.save(any(Payment.class), any())).thenReturn(payment);

        Payment savedPayment = domainPaymentService.savePayment(payment, headers);

        verify(paymentRepository).existRecord(anyString());
        verify(paymentRepository).save(any(Payment.class), any());
        verify(producerService).sendMessage(any(Payment.class));
        assertNotNull(savedPayment);
    }

    @Test
    void shouldNotCreatePaymentWhenIdempotentKeyExistInDB() {
        String uuid = UUID.randomUUID().toString();
        Payment payment = createPayment();
        Map<String, String> headers = Map.of(IDEMPOTENT_KEY, uuid);
        when(paymentRepository.existRecord(anyString())).thenReturn(uuid);

        CustomErrorException exception = assertThrows(CustomErrorException.class, () -> {
            domainPaymentService.savePayment(payment, headers);
        });

        verify(paymentRepository).existRecord(anyString());
        verify(paymentRepository, times(0)).save(any(Payment.class), any());
        verify(producerService, times(0)).sendMessage(any(Payment.class));
        assertTrue(exception.getMessage().contains("Payment already present"));
    }

    @Test
    void shouldNotCreatePaymentIfAnErrorOccursInDB() {
        String uuid = UUID.randomUUID().toString();
        Payment payment = createPayment();
        Map<String, String> headers = Map.of(IDEMPOTENT_KEY, uuid);
        when(paymentRepository.save(any(Payment.class), anyString())).thenThrow(new RuntimeException("database error"));

        CustomErrorException exception = assertThrows(CustomErrorException.class, () -> {
            domainPaymentService.savePayment(payment, headers);
        });

        verify(paymentRepository).existRecord(anyString());
        verify(paymentRepository, times(1)).save(any(Payment.class), any());
        verify(producerService, times(0)).sendMessage(any(Payment.class));
        assertTrue(exception.getMessage().contains("database error"));
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
