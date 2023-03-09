package com.rizzotti.portx.infrastructure.configuration;

import com.rizzotti.portx.DomainLayerApplication;
import com.rizzotti.portx.domain.repository.PaymentRepository;
import com.rizzotti.portx.domain.service.DomainPaymentService;
import com.rizzotti.portx.domain.service.PaymentService;
import com.rizzotti.portx.domain.service.ProducerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = DomainLayerApplication.class)
public class BeanConfiguration {

    @Bean
    PaymentService paymentService(final PaymentRepository paymentRepository, final ProducerService producerService){
        return new DomainPaymentService(paymentRepository, producerService);
    }

}
