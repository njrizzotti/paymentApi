package com.rizzotti.portx.infrastructure;

import com.rizzotti.portx.domain.Account;
import com.rizzotti.portx.domain.Customer;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.infrastructure.repository.mysql.PaymentEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentEntityUnitTest {

    @Test
    public void shouldReturnPaymentFromEntity(){
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

        Payment payment = paymentEntity.toPayment();
        assertEquals(payment.getAmount(), paymentEntity.getAmount());
    }

    @Test
    public void shouldReturnPaymentEntityFromPayment(){
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(100));
        payment.setBeneficiary(new Customer("mockBeneficiaryName", "mockBeneficiaryId"));
        payment.setOriginator(new Customer("mockOriginatorName", "mockOriginatorId"));
        payment.setReceiver(new Account("mockReceiverAccountType()", "mockReceiverAccountNumber()"));
        payment.setSender(new Account("mockSenderAccountType()", "mockSenderAccountNumber()"));
        payment.setCurrency("mockCurrency");
        payment.setStatus("CREATED");

        PaymentEntity paymentEntity = new PaymentEntity(payment);
        assertEquals(payment.getAmount(), paymentEntity.getAmount());
    }
}
