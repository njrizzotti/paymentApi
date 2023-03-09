package com.rizzotti.portx.domain.service;

import com.rizzotti.portx.domain.repository.PaymentRepository;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.exception.CustomErrorException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import static com.rizzotti.portx.utils.Constants.IDEMPOTENT_KEY;

public class DomainPaymentService implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(DomainPaymentService.class);

    private static final String PAYMENT_ALREADY_PRESENT = "Payment already present";

    final PaymentRepository paymentRepository;

    final ProducerService producerService;

    public DomainPaymentService(final PaymentRepository paymentRepository, final ProducerService producerService){
        this.paymentRepository = paymentRepository;
        this.producerService = producerService;
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
                logger.debug("UUID already present in DB: ",  uuidDB);
                throw new CustomErrorException(PAYMENT_ALREADY_PRESENT);
            }
            savedPayment = paymentRepository.save(payment, headers.get(IDEMPOTENT_KEY));
            producerService.sendMessage(savedPayment);
        }catch (Exception e){
            logger.error("An error occurred while saving entity. Exception: ", e.getStackTrace());
            throw new CustomErrorException(e.getMessage());
        }
        return savedPayment;
    }
}
