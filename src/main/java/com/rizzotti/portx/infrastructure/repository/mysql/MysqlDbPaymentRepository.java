package com.rizzotti.portx.infrastructure.repository.mysql;

import com.rizzotti.portx.domain.repository.PaymentRepository;
import com.rizzotti.portx.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class MysqlDbPaymentRepository implements PaymentRepository {

    private final SpringDataMySqlPaymentRepository paymentRepository;

    @Autowired
    public MysqlDbPaymentRepository(final SpringDataMySqlPaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> checkStatus(Integer id) {
        Optional<PaymentEntity> paymentEntity = paymentRepository.findById(id);
        if(paymentEntity.isPresent()){
            return Optional.of(paymentEntity.get().toPayment());
        }
        else {
            return Optional.empty();
        }

    }

    @Override
    public Payment save(Payment payment, String uuid) {
        PaymentEntity paymentEntity = new PaymentEntity(payment);
        paymentEntity.setUuid(uuid);
        return paymentRepository.save(paymentEntity).toPayment();
    }

    @Override
    public String existRecord(String uuid) {
        return paymentRepository.existRecord(uuid);
    }


}
