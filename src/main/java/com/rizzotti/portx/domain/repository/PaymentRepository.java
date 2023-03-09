package com.rizzotti.portx.domain.repository;

import com.rizzotti.portx.domain.Payment;

import java.util.Optional;

public interface PaymentRepository {

    Optional<Payment> checkStatus(Integer id);
    Payment save(Payment payment,String uuid);
    String existRecord(String uuid);
}
