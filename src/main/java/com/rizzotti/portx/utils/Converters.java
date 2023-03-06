package com.rizzotti.portx.utils;

import com.rizzotti.portx.dao.PaymentEntity;
import com.rizzotti.portx.dto.Account;
import com.rizzotti.portx.dto.Customer;
import com.rizzotti.portx.dto.Payment;
import org.springframework.stereotype.Component;

@Component
public class Converters {

    private final String PAYMENT_STATUS_CREATED = "CREATED";

    public Payment getPaymentDtoFromPaymentEntity(PaymentEntity paymentEntity){
        Payment payment = new Payment();
        payment.setId(paymentEntity.getId());
        payment.setAmount(paymentEntity.getAmount());
        payment.setBeneficiary(new Customer(paymentEntity.getBeneficiaryName(),paymentEntity.getBeneficiaryId()));
        payment.setOriginator(new Customer(paymentEntity.getOriginatorName(), paymentEntity.getOriginatorId()));
        payment.setReceiver(new Account(paymentEntity.getReceiverAccountType(), paymentEntity.getReceiverAccountNumber()));
        payment.setSender(new Account(paymentEntity.getSenderAccountType(), paymentEntity.getSenderAccountNumber()));
        payment.setCurrency(paymentEntity.getCurrency());
        payment.setStatus(paymentEntity.getPaymentStatus());
        return payment;
    }

    public PaymentEntity getPaymentEntityFromPaymentDto(Payment payment){
        PaymentEntity entity = new PaymentEntity();
        entity.setAmount(payment.getAmount());
        entity.setBeneficiaryId(payment.getBeneficiary().getId());
        entity.setBeneficiaryName(payment.getBeneficiary().getName());
        entity.setOriginatorId(payment.getOriginator().getId());
        entity.setOriginatorName(payment.getOriginator().getName());
        entity.setReceiverAccountNumber(payment.getReceiver().getAccountNumber());
        entity.setReceiverAccountType(payment.getReceiver().getAccountType());
        entity.setSenderAccountNumber(payment.getSender().getAccountNumber());
        entity.setSenderAccountType(payment.getSender().getAccountType());
        entity.setCurrency(payment.getCurrency());
        entity.setPaymentStatus(PAYMENT_STATUS_CREATED);
        return entity;
    }
}
