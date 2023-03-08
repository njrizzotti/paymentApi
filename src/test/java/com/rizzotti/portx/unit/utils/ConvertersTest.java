package com.rizzotti.portx.unit.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.rizzotti.portx.dao.PaymentEntity;
import com.rizzotti.portx.dto.Account;
import com.rizzotti.portx.dto.Customer;
import com.rizzotti.portx.dto.Payment;
import com.rizzotti.portx.utils.Converters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ConvertersTest {

    Converters converters = new Converters();

    @Test
    public void convertPaymentDtoToPaymentEntity() {
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
        PaymentEntity paymentEntity = converters.getPaymentEntityFromPaymentDto(payment);

        // Then
        Assertions.assertEquals(paymentEntity.getPaymentStatus(), payment.getStatus());
        Assertions.assertEquals(paymentEntity.getAmount(), payment.getAmount());
        Assertions.assertEquals(paymentEntity.getCurrency(), payment.getCurrency());
        Assertions.assertEquals(paymentEntity.getBeneficiaryName(), payment.getBeneficiary().getName());
        Assertions.assertEquals(paymentEntity.getBeneficiaryId(), payment.getBeneficiary().getId());
        Assertions.assertEquals(paymentEntity.getOriginatorName(), payment.getOriginator().getName());
        Assertions.assertEquals(paymentEntity.getOriginatorId(), payment.getOriginator().getId());
        Assertions.assertEquals(paymentEntity.getReceiverAccountNumber(), payment.getReceiver().getAccountNumber());
        Assertions.assertEquals(paymentEntity.getReceiverAccountType(), payment.getReceiver().getAccountType());
        Assertions.assertEquals(paymentEntity.getSenderAccountNumber(), payment.getSender().getAccountNumber());
        Assertions.assertEquals(paymentEntity.getSenderAccountType(), payment.getSender().getAccountType());
    }

    @Test
    public void convertPaymentEntityToPaymentDto() {
        // Given
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAmount(new BigDecimal(100));
        paymentEntity.setBeneficiaryName("mockBeneficiaryName");
        paymentEntity.setBeneficiaryId("mockBeneficiaryId");
        paymentEntity.setOriginatorName("mockOriginatorName");
        paymentEntity.setOriginatorId("mockOriginatorId");
        paymentEntity.setReceiverAccountType("mockReceiverAccountType");
        paymentEntity.setReceiverAccountNumber("mockReceiverAccountNumber");
        paymentEntity.setSenderAccountType("mockSenderAccountType");
        paymentEntity.setSenderAccountNumber("mockSenderAccountNumber");
        paymentEntity.setCurrency("mockCurrency");
        paymentEntity.setPaymentStatus("CREATED");

        // When
        Payment payment = converters.getPaymentDtoFromPaymentEntity(paymentEntity);

        // Then
        Assertions.assertEquals(paymentEntity.getPaymentStatus(), payment.getStatus());
        Assertions.assertEquals(paymentEntity.getAmount(), payment.getAmount());
        Assertions.assertEquals(paymentEntity.getCurrency(), payment.getCurrency());
        Assertions.assertEquals(paymentEntity.getBeneficiaryName(), payment.getBeneficiary().getName());
        Assertions.assertEquals(paymentEntity.getBeneficiaryId(), payment.getBeneficiary().getId());
        Assertions.assertEquals(paymentEntity.getOriginatorName(), payment.getOriginator().getName());
        Assertions.assertEquals(paymentEntity.getOriginatorId(), payment.getOriginator().getId());
        Assertions.assertEquals(paymentEntity.getReceiverAccountNumber(), payment.getReceiver().getAccountNumber());
        Assertions.assertEquals(paymentEntity.getReceiverAccountType(), payment.getReceiver().getAccountType());
        Assertions.assertEquals(paymentEntity.getSenderAccountNumber(), payment.getSender().getAccountNumber());
        Assertions.assertEquals(paymentEntity.getSenderAccountType(), payment.getSender().getAccountType());
    }

    @Test
    public void test() {
        // Given
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAmount(new BigDecimal(100));
        paymentEntity.setBeneficiaryName("mockBeneficiaryName");
        paymentEntity.setBeneficiaryId("mockBeneficiaryId");
        paymentEntity.setOriginatorName("mockOriginatorName");
        paymentEntity.setOriginatorId("mockOriginatorId");
        paymentEntity.setReceiverAccountType("mockReceiverAccountType");
        paymentEntity.setReceiverAccountNumber("mockReceiverAccountNumber");
        paymentEntity.setSenderAccountType("mockSenderAccountType");
        paymentEntity.setSenderAccountNumber("mockSenderAccountNumber");
        paymentEntity.setCurrency("mockCurrency");
        paymentEntity.setPaymentStatus("CREATED");

        // When
        JsonNode payment = converters.getPayload(paymentEntity);

        // Then
        Assertions.assertEquals(paymentEntity.getPaymentStatus(), payment.get("paymentStatus").asText());

    }
}
