package com.rizzotti.portx.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.rizzotti.portx.dao.PaymentEntity;
import com.rizzotti.portx.dao.PaymentRepository;
import com.rizzotti.portx.dto.Payment;
import com.rizzotti.portx.exception.CustomErrorException;
import com.rizzotti.portx.utils.Converters;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static com.rizzotti.portx.utils.Constants.IDEMPOTENT_KEY;
import static com.rizzotti.portx.utils.Constants.PAYMENTS_TOPIC;

@Service
public class PaymentService {

    private static final String PAYMENT_ALREADY_PRESENT = "Payment already present";

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    KafkaTemplate<String, String> KafkaStringTemplate;

    @Autowired
    Converters converters;

    public Optional<Payment> checkStatus(Integer paymentId){
        Optional<PaymentEntity> payment =  paymentRepository.findById(paymentId);
        if(payment.isPresent()){
            return Optional.of(converters.getPaymentDtoFromPaymentEntity(payment.get()));
        }
        else{
            return Optional.empty();
        }
    }

    @Transactional
    public Payment savePayment(Payment payment, Map<String, String> headers) throws CustomErrorException {
        PaymentEntity paymentEntity = converters.getPaymentEntityFromPaymentDto(payment);
        try {
            String uuidDB = paymentRepository.existRecord(headers.get(IDEMPOTENT_KEY));
            if(uuidDB != null && !uuidDB.isBlank()){
                throw new CustomErrorException(PAYMENT_ALREADY_PRESENT);
            }
            paymentEntity.setUuid(headers.get(IDEMPOTENT_KEY));
            paymentEntity = paymentRepository.save(paymentEntity);

            KafkaStringTemplate.send(PAYMENTS_TOPIC, converters.getPayload(paymentEntity).toString());
        }catch (Exception e){
            throw new CustomErrorException(e.getMessage());
        }
        return converters.getPaymentDtoFromPaymentEntity(paymentEntity);
    }

}
