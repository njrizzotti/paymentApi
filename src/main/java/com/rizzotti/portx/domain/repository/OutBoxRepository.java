package com.rizzotti.portx.domain.repository;

import com.rizzotti.portx.infrastructure.repository.mysql.OutBoxEntity;

import java.util.Date;
import java.util.List;

public interface OutBoxRepository {

    List<OutBoxEntity> findCreatedPaymentsPendingToSend(Date time);
    void delete(OutBoxEntity entity);
    void save(OutBoxEntity entity);
}
