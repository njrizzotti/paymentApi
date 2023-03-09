package com.rizzotti.portx.domain.service;

import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.exception.CustomErrorException;

import java.util.Map;
import java.util.Optional;

public interface PaymentService {

    Payment savePayment(Payment payment, Map<String, String> headers) throws CustomErrorException;

    Optional<Payment> checkStatus(Integer paymentId);
}