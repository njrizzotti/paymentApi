package com.rizzotti.portx.unit.controller;

//import com.rizzotti.portx.controllers.PaymentsController;
//import com.rizzotti.portx.dto.Payment;
import com.rizzotti.portx.application.rest.PaymentsController;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.domain.service.PaymentService;
import com.rizzotti.portx.exception.CustomErrorException;
//import com.rizzotti.portx.services.PaymentService;
import com.rizzotti.portx.unit.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

import static com.rizzotti.portx.utils.Constants.IDEMPOTENT_KEY;

public class PaymentsControllerTests extends BaseTest {

    @Mock
    PaymentService paymentService;

    @InjectMocks
    PaymentsController paymentsController;

    @Test
    public void shouldReturnPaymentStatus(){
        // Given
        Payment paymentMock = Mockito.mock(Payment.class);

        // When
        Mockito.when(paymentService.checkStatus(Mockito.anyInt())).thenReturn(Optional.of(paymentMock));
        ResponseEntity result = paymentsController.getPaymentById(Mockito.anyInt());

        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void notFoundPaymentStatus(){
        // Given
        Mockito.when(paymentService.checkStatus(Mockito.anyInt())).thenReturn(Optional.empty());

        // When
        ResponseEntity result = paymentsController.getPaymentById(Mockito.anyInt());

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void createPaymentWithoutIdempotentKey() {
        // Given
        Payment paymentMock = Mockito.mock(Payment.class);
        Map<String, String> headers = Collections.emptyMap();

        // When
        CustomErrorException exception = Assertions.assertThrows(CustomErrorException.class, () -> paymentsController.createPayment(paymentMock, headers));

        // Then
        Assertions.assertTrue(exception.getMessage().contentEquals(IDEMPOTENT_KEY_NOT_PRESENT));
    }

    @Test
    public void createPaymentWithEmptyIdempotentKey() {
        // Given
        Payment paymentMock = Mockito.mock(Payment.class);
        Map<String, String> headers = Collections.singletonMap(IDEMPOTENT_KEY, "");

        // When
        CustomErrorException exception = Assertions.assertThrows(CustomErrorException.class, () -> paymentsController.createPayment(paymentMock, headers));

        // Then
        Assertions.assertTrue(exception.getMessage().contentEquals(IDEMPOTENT_KEY_NOT_PRESENT));
    }

    @Test
    public void createPayment() throws CustomErrorException {
        // Given
        Payment paymentMock = Mockito.mock(Payment.class);
        Map<String, String> headers = Collections.singletonMap(IDEMPOTENT_KEY, "123");
        Mockito .when(paymentService.savePayment(paymentMock, headers)).thenReturn(paymentMock);

        // When
        ResponseEntity result = paymentsController.createPayment(paymentMock, headers);

        // Then
        Assertions.assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
    }
}
