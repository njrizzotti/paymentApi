package com.rizzotti.portx.domain.service;

import com.rizzotti.portx.domain.Payment;

public interface ProducerService {

    void sendMessage(Payment payment);
}
