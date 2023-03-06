package com.rizzotti.portx.dto;

import java.math.BigDecimal;

public class Payment {

    Integer id;
    String currency;
    BigDecimal amount;
    Customer originator;
    Customer beneficiary;
    Account sender;
    Account receiver;
    String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Customer getOriginator() {
        return originator;
    }

    public void setOriginator(Customer originator) {
        this.originator = originator;
    }

    public Customer getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Customer beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
