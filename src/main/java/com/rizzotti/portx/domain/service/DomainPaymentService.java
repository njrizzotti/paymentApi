package com.rizzotti.portx.domain.service;

import com.rizzotti.portx.domain.repository.OutBoxRepository;
import com.rizzotti.portx.domain.repository.PaymentRepository;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.exception.CustomErrorException;
import com.rizzotti.portx.infrastructure.repository.mysql.OutBoxEntity;
import jakarta.transaction.Transactional;
import org.hibernate.metamodel.model.convert.spi.Converters;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Optional;
import static com.rizzotti.portx.utils.Constants.IDEMPOTENT_KEY;

public class DomainPaymentService implements PaymentService {

    private static final String PAYMENT_ALREADY_PRESENT = "Payment already present";

    final PaymentRepository paymentRepository;

    final ProducerService producerService;

    final OutBoxRepository outBoxRepository;

    final TransactionTemplate transactionTemplate;

    public DomainPaymentService(final PaymentRepository paymentRepository, final ProducerService producerService, final OutBoxRepository outBoxRepository, final TransactionTemplate transactionTemplate) {
        this.paymentRepository = paymentRepository;
        this.producerService = producerService;
        this.outBoxRepository = outBoxRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public Optional<Payment> checkStatus(Integer paymentId){
        return paymentRepository.checkStatus(paymentId);
    }

    @Override
    @Transactional
    public Payment savePayment(Payment payment, Map<String, String> headers) throws CustomErrorException {
        Payment savedPayment;
        try {
            String uuidDB = paymentRepository.existRecord(headers.get(IDEMPOTENT_KEY));
            if(uuidDB != null && !uuidDB.isBlank()){
                throw new CustomErrorException(PAYMENT_ALREADY_PRESENT);
            }


            savedPayment = transactionTemplate.execute(status -> {
                var updatedPayment  = paymentRepository.save(payment, headers.get(IDEMPOTENT_KEY));
                OutBoxEntity outBoxEntity = new OutBoxEntity(updatedPayment);
                outBoxRepository.save(outBoxEntity);
                return updatedPayment;
            });

            producerService.sendMessage(savedPayment);

        }catch (Exception e){
            throw new CustomErrorException(e.getMessage());
        }
        return savedPayment;
    }
}
