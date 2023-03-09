package com.rizzotti.portx.infrastructure.configuration;

import com.rizzotti.portx.DomainLayerApplication;
import com.rizzotti.portx.domain.repository.OutBoxRepository;
import com.rizzotti.portx.domain.repository.PaymentRepository;
import com.rizzotti.portx.domain.service.DomainPaymentService;
import com.rizzotti.portx.domain.service.PaymentService;
import com.rizzotti.portx.domain.service.ProducerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@ComponentScan(basePackageClasses = DomainLayerApplication.class)
public class BeanConfiguration {

    @Bean
    PaymentService paymentService(final PaymentRepository paymentRepository, final ProducerService producerService, final OutBoxRepository outBoxRepository, final TransactionTemplate transactionTemplate){
        return new DomainPaymentService(paymentRepository, producerService, outBoxRepository, transactionTemplate);
    }

}
