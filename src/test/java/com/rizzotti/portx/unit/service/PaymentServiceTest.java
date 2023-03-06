package com.rizzotti.portx.unit.service;

import com.rizzotti.portx.dao.PaymentEntity;
import com.rizzotti.portx.dao.PaymentRepository;
import com.rizzotti.portx.dto.Payment;
import com.rizzotti.portx.exception.CustomErrorException;
import com.rizzotti.portx.services.PaymentService;
import com.rizzotti.portx.unit.BaseTest;
import com.rizzotti.portx.utils.Converters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.rizzotti.portx.utils.Constants.IDEMPOTENT_KEY;

public class PaymentServiceTest extends BaseTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    KafkaTemplate<String, String> KafkaStringTemplate;

    @Mock
    Converters converters;

    @InjectMocks
    PaymentService paymentService;

    @Test
    public void paymentPresentInDatabaseShouldBeReturned() {
        // Given
        Payment paymentMock = Mockito.mock(Payment.class);
        PaymentEntity paymentEntityMock = Mockito.mock(PaymentEntity.class);
        Mockito.when(paymentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(paymentEntityMock));
        Mockito.when(converters.getPaymentDtoFromPaymentEntity(paymentEntityMock)).thenReturn(paymentMock);

        // When
        Optional<Payment> result = paymentService.checkStatus(Mockito.anyInt());

        // Then
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void paymentNotPresentInDatabaseShouldReturnEmpty() {
        // Given
        Mockito.when(paymentRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        // When
        Optional<Payment> result = paymentService.checkStatus(Mockito.anyInt());

        // Then
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void createPaymentThatAlreadyExistsInDBShouldThrowException() {
        // Given
        Mockito.when(paymentRepository.existRecord(Mockito.anyString())).thenReturn("12345");
        Payment paymentMock = Mockito.mock(Payment.class);
        Map<String, String> headers = Collections.singletonMap(IDEMPOTENT_KEY, "12345");

        // When
        CustomErrorException exception = Assertions.assertThrows(CustomErrorException.class, () -> paymentService.savePayment(paymentMock, headers));

        // Then
        Assertions.assertTrue(exception.getMessage().contentEquals(PAYMENT_ALREADY_PRESENT_IN_DATABASE));
    }

    @Test
    public void createPaymentFailedSaveOnDB() {
        Payment paymentMock = Mockito.mock(Payment.class);
        PaymentEntity paymentEntityMock = Mockito.mock(PaymentEntity.class);
        Map<String, String> headers = Collections.singletonMap(IDEMPOTENT_KEY, "12345");

        // When
        Mockito.when(paymentRepository.existRecord(Mockito.anyString())).thenReturn("");
        Mockito.when(paymentRepository.save(Mockito.any())).thenThrow(new RuntimeException("error mock"));
        Mockito.when(converters.getPaymentEntityFromPaymentDto(Mockito.any())).thenReturn(paymentEntityMock);
        CustomErrorException exception = Assertions.assertThrows(CustomErrorException.class, () -> paymentService.savePayment(paymentMock, headers));

        // Then
        Assertions.assertTrue(exception.getMessage().contentEquals("error mock"));
    }
}
