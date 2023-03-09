package com.rizzotti.portx.infrastructure.messages.kafka;

import com.rizzotti.portx.domain.repository.OutBoxRepository;
import com.rizzotti.portx.infrastructure.repository.mysql.OutBoxEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class DeliveryService {

    private final OutBoxRepository outBoxRepository;
    private final KafkaProducerService kafkaProducerService;

    public DeliveryService(final OutBoxRepository outBoxRepository, KafkaProducerService kafkaProducerService){
        this.outBoxRepository = outBoxRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Scheduled
    public void retry() {
        List<OutBoxEntity> outboxEntities = outBoxRepository.findCreatedPaymentsPendingToSend(Date.from(Instant.now().minusSeconds(60)));
        for (OutBoxEntity outbox : outboxEntities) {
            kafkaProducerService.sendMessage(outbox.toPayment());
            outBoxRepository.delete(outbox);
        }
    }

}
