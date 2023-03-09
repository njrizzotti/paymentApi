package com.rizzotti.portx.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.rizzotti.portx.infrastructure.repository.mysql.PaymentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class PaymentUnitTest {

    @Test
    public void shouldReturnJsonFromPayment() {
        // Given
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(100));
        payment.setBeneficiary(new Customer("mockBeneficiaryName", "mockBeneficiaryId"));
        payment.setOriginator(new Customer("mockOriginatorName", "mockOriginatorId"));
        payment.setReceiver(new Account("mockReceiverAccountType()", "mockReceiverAccountNumber()"));
        payment.setSender(new Account("mockSenderAccountType()", "mockSenderAccountNumber()"));
        payment.setCurrency("mockCurrency");
        payment.setStatus("CREATED");
        // When
        JsonNode paymentJson = payment.toJson();

        // Then
        Assertions.assertEquals(payment.getStatus(), paymentJson.get("status").asText());
    }
}
