package com.rizzotti.portx.infrastructure.configuration;

import com.rizzotti.portx.infrastructure.repository.mysql.SpringDataMySqlPaymentRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = SpringDataMySqlPaymentRepository.class)
public class MysqlDbConfiguration {
}
