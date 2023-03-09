package com.rizzotti.portx.infrastructure.messages.kafka;

import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.domain.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.rizzotti.portx.utils.Constants.PAYMENTS_TOPIC;

@Service
public class KafkaProducerService implements ProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, String> KafkaStringTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate kafkaTemplate){
        this.KafkaStringTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(Payment payment) {
        logger.info("Sending message to Kafka :: payment:: {}", payment);
        KafkaStringTemplate.send(PAYMENTS_TOPIC, payment.toJson().toString());
    }
}
