package com.rizzotti.portx.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

    @Query("SELECT uuid From PaymentEntity p where p.uuid = ?1")
    String existRecord(String uuid);
}
