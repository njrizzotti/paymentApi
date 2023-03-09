package com.rizzotti.portx.infrastructure.messages.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.domain.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.rizzotti.portx.utils.Constants.PAYMENTS_TOPIC;

@Service
public class KafkaProducerService implements ProducerService {

    private final KafkaTemplate<String, String> KafkaStringTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate kafkaTemplate){
        this.KafkaStringTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(Payment payment) {
        //loger.info("Sending message to Kafka :: personDto:: {}", personDto);
        KafkaStringTemplate.send(PAYMENTS_TOPIC, payment.toJson().toString());
    }
}
