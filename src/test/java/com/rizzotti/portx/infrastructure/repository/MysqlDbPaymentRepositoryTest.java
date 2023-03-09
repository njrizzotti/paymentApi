package com.rizzotti.portx.infrastructure.repository;

import com.rizzotti.portx.domain.Account;
import com.rizzotti.portx.domain.Customer;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.infrastructure.repository.mysql.MysqlDbPaymentRepository;
import com.rizzotti.portx.infrastructure.repository.mysql.PaymentEntity;
import com.rizzotti.portx.infrastructure.repository.mysql.SpringDataMySqlPaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MysqlDbPaymentRepositoryTest {

    MysqlDbPaymentRepository mySqlPaymentRepository;

    @Autowired
    private SpringDataMySqlPaymentRepository paymentRepository;

    @BeforeEach
    void cleanUp(){
    mySqlPaymentRepository = new MysqlDbPaymentRepository(paymentRepository);

    }

    @Test
    public void itShouldSavePayment() {
        Payment created = mySqlPaymentRepository.save(getPayment(), UUID.randomUUID().toString());

        Assertions.assertNotNull(created.getId());
    }

    @Test
    public void itShouldReturnPayment() {
        Payment created = mySqlPaymentRepository.save(getPayment(), UUID.randomUUID().toString());

        Assertions.assertNotNull(created.getId());
        Optional<Payment> founded =  mySqlPaymentRepository.checkStatus(created.getId());
        Assertions.assertTrue(founded.isPresent());
    }

    @Test
    public void itShouldReturnEmptyIfPaymentDoesNotExistInDB() {
       Optional<Payment> founded =  mySqlPaymentRepository.checkStatus(1);
       Assertions.assertFalse(founded.isPresent());
    }

    @Test
    public void itShouldReturnUUID() {
        String uuid = UUID.randomUUID().toString();
        Payment created = mySqlPaymentRepository.save(getPayment(),uuid);
        Assertions.assertNotNull(created.getId());
        String founded =  mySqlPaymentRepository.existRecord(uuid);
        Assertions.assertFalse(founded.isEmpty());
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

    public Payment getPayment(){
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(100));
        payment.setBeneficiary(new Customer("mockBeneficiaryName", "mockBeneficiaryId"));
        payment.setOriginator(new Customer("mockOriginatorName", "mockOriginatorId"));
        payment.setReceiver(new Account("mockReceiverAccountType()", "mockReceiverAccountNumber()"));
        payment.setSender(new Account("mockSenderAccountType()", "mockSenderAccountNumber()"));
        payment.setCurrency("mockCurrency");
        payment.setStatus("CREATED");
        return payment;
    }
}
