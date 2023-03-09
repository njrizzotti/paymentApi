package com.rizzotti.portx.infrastructure.repository.mysql;

import com.rizzotti.portx.domain.repository.OutBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MysqlDbOutboxRepository implements OutBoxRepository {

    private final SpringDataMySqlOutBoxRepository springDataMySqlOutBoxRepository;

    @Autowired
    public MysqlDbOutboxRepository(final SpringDataMySqlOutBoxRepository springDataMySqlOutBoxRepository){
        this.springDataMySqlOutBoxRepository = springDataMySqlOutBoxRepository;
    }

    @Override
    public List<OutBoxEntity> findCreatedPaymentsPendingToSend(Date time) {
        return springDataMySqlOutBoxRepository.findAllBefore(time);
    }

    @Override
    public void delete(OutBoxEntity entity) {
        springDataMySqlOutBoxRepository.delete(entity);
    }

    @Override
    public void save(OutBoxEntity entity) {
        springDataMySqlOutBoxRepository.save(entity);
    }
}
