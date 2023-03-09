package com.rizzotti.portx.infrastructure.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SpringDataMySqlOutBoxRepository extends JpaRepository<OutBoxEntity, Integer> {

    @Query("from OutBoxEntity o where o.createdOn <= ?1")
    List<OutBoxEntity> findAllBefore(Date time);
}
