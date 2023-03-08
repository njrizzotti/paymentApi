package com.rizzotti.portx.unit.repository;

import com.rizzotti.portx.dao.PaymentEntity;
import com.rizzotti.portx.dao.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PaymentsRepositoryTest {


    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PaymentRepository paymentRepository;

    @Test
    public void it_should_save_user() {
        PaymentEntity paymentEntity = paymentRepository.save(createPaymentEntity());
        Optional<PaymentEntity> foundedPayment = paymentRepository.findById(paymentEntity.getId());
        Assertions.assertTrue(foundedPayment.isPresent());
        Assertions.assertTrue(foundedPayment.get().getAmount().equals(paymentEntity.getAmount()));
    }

    private PaymentEntity createPaymentEntity(){
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAmount(new BigDecimal(100));
        paymentEntity.setBeneficiaryName("mockBeneficiaryName");
        paymentEntity.setBeneficiaryId("mockBeneficiaryId");
        paymentEntity.setOriginatorName("mockOriginatorName");
        paymentEntity.setOriginatorId("mockOriginatorId");
        paymentEntity.setReceiverAccountType("mockReceiverAccountType");
        paymentEntity.setReceiverAccountNumber("mockReceiverAccountNumber");
        paymentEntity.setSenderAccountType("mockSenderAccountType");
        paymentEntity.setSenderAccountNumber("mockSenderAccountNumber");
        paymentEntity.setCurrency("mockCurrency");
        paymentEntity.setPaymentStatus("CREATED");
        return paymentEntity;
    }
}
