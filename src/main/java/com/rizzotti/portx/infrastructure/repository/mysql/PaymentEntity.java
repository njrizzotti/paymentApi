package com.rizzotti.portx.infrastructure.repository.mysql;

import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.domain.Account;
import com.rizzotti.portx.domain.Customer;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uuid;
    private BigDecimal amount;
    private String currency;
    private String originatorName;
    private String originatorId;
    private String beneficiaryName;
    private String beneficiaryId;
    private String senderAccountType;
    private String senderAccountNumber;
    private String receiverAccountType;
    private String receiverAccountNumber;
    private String paymentStatus;

    public PaymentEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid(){
        return uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency(){
        return currency;
    }

    public void setCurrency(String currency){
        this.currency = currency;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getSenderAccountType() {
        return senderAccountType;
    }

    public void setSenderAccountType(String senderAccountType) {
        this.senderAccountType = senderAccountType;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getReceiverAccountType() {
        return receiverAccountType;
    }

    public void setReceiverAccountType(String receiverAccountType) {
        this.receiverAccountType = receiverAccountType;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public String getPaymentStatus(){
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus){
        this.paymentStatus = paymentStatus;
    }

    public Payment toPayment() {
        Payment payment = new Payment();
        payment.setId(this.getId());
        payment.setAmount(this.getAmount());
        payment.setBeneficiary(new Customer(this.getBeneficiaryName(),this.getBeneficiaryId()));
        payment.setOriginator(new Customer(this.getOriginatorName(), this.getOriginatorId()));
        payment.setReceiver(new Account(this.getReceiverAccountType(), this.getReceiverAccountNumber()));
        payment.setSender(new Account(this.getSenderAccountType(), this.getSenderAccountNumber()));
        payment.setCurrency(this.getCurrency());
        payment.setStatus(this.getPaymentStatus());
        return payment;
    }

    public PaymentEntity(Payment payment){
        this.amount = payment.getAmount();
        this.beneficiaryId = payment.getBeneficiary().getId();
        this.beneficiaryName = payment.getBeneficiary().getName();
        this.originatorId = payment.getOriginator().getId();
        this.originatorName = payment.getOriginator().getName();
        this.receiverAccountNumber = payment.getReceiver().getAccountNumber();
        this.receiverAccountType = payment.getReceiver().getAccountType();
        this.senderAccountNumber = payment.getSender().getAccountNumber();
        this.senderAccountType = payment.getSender().getAccountType();
        this.currency = payment.getCurrency();
        this.paymentStatus = "CREATED";
    }
}
