package com.rizzotti.portx.infrastructure.repository;

import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.domain.repository.OutBoxRepository;
import com.rizzotti.portx.infrastructure.repository.mysql.MysqlDbOutboxRepository;
import com.rizzotti.portx.infrastructure.repository.mysql.MysqlDbPaymentRepository;
import com.rizzotti.portx.infrastructure.repository.mysql.OutBoxEntity;
import com.rizzotti.portx.infrastructure.repository.mysql.SpringDataMySqlOutBoxRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MysqlDbOutboxRepositoryTest {

    MysqlDbOutboxRepository outboxRepository;

    @Autowired
    SpringDataMySqlOutBoxRepository springDataMySqlOutBoxRepository;

    @BeforeEach
    void setUp(){
        outboxRepository = new MysqlDbOutboxRepository(springDataMySqlOutBoxRepository);
    }

    @Test
    public void itShouldSaveOutBoxEntity() {
        OutBoxEntity outBoxEntity = new OutBoxEntity(1, "CREATED", "payload", new Date());
        outBoxEntity.setAggregateId(1);
        outboxRepository.save(outBoxEntity);
        Optional<OutBoxEntity> saved = springDataMySqlOutBoxRepository.findById(outBoxEntity.getAggregateId());
        assertNotNull(saved);
        assertTrue(saved.isPresent());
        assertEquals(saved.get().getAggregateId(), outBoxEntity.getAggregateId());
    }

    @Test
    public void itShouldDeleteOutBoxEntity() {
        OutBoxEntity outBoxEntity = new OutBoxEntity(1, "CREATED", "payload", new Date());
        outBoxEntity.setAggregateId(1);
        outboxRepository.save(outBoxEntity);
        outboxRepository.delete(outBoxEntity);
        Optional<OutBoxEntity> saved = springDataMySqlOutBoxRepository.findById(outBoxEntity.getAggregateId());;
        assertFalse(saved.isPresent());
    }

    @Test
    public void itShouldFindOutBoxEntityBeforeDate() {
        OutBoxEntity outBoxEntity = new OutBoxEntity(1, "CREATED", "payload", new Date());
        outBoxEntity.setAggregateId(1);
        outboxRepository.save(outBoxEntity);
        List<OutBoxEntity> saved = outboxRepository.findCreatedPaymentsPendingToSend(new Date());;
        assertFalse(saved.isEmpty());
    }
}
