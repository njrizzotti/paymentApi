package com.rizzotti.portx.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.rizzotti.portx.domain.Account;
import com.rizzotti.portx.domain.Customer;
import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.infrastructure.repository.mysql.OutBoxEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutBoxEntityUnitTest {

    @Test
    public void shouldReturnOutBoxEntityFromPayment(){
        Payment payment = new Payment();
        payment.setId(1);
        payment.setAmount(new BigDecimal(100));
        payment.setBeneficiary(new Customer("mockBeneficiaryName", "mockBeneficiaryId"));
        payment.setOriginator(new Customer("mockOriginatorName", "mockOriginatorId"));
        payment.setReceiver(new Account("mockReceiverAccountType()", "mockReceiverAccountNumber()"));
        payment.setSender(new Account("mockSenderAccountType()", "mockSenderAccountNumber()"));
        payment.setCurrency("mockCurrency");
        payment.setStatus("CREATED");

        OutBoxEntity outbox = new OutBoxEntity(payment);

        assertEquals(payment.getId(), outbox.getAggregateId());
    }

    @Test
    public void shouldCreatePayload(){

        Payment payment = createPayment();
        OutBoxEntity outBoxEntity = new OutBoxEntity(payment);
        JsonNode node = outBoxEntity.getPayload(payment);
        assertEquals(payment.getId(), outBoxEntity.getAggregateId());
        assertTrue(node.toString().contains(payment.getId().toString()));
    }

    private Payment createPayment(){
        Payment payment = new Payment();
        payment.setId(1);
        payment.setAmount(new BigDecimal(100));
        payment.setBeneficiary(new Customer("mockBeneficiaryName", "mockBeneficiaryId"));
        payment.setOriginator(new Customer("mockOriginatorName", "mockOriginatorId"));
        payment.setReceiver(new Account("mockReceiverAccountType()", "mockReceiverAccountNumber()"));
        payment.setSender(new Account("mockSenderAccountType()", "mockSenderAccountNumber()"));
        payment.setCurrency("mockCurrency");
        payment.setStatus("CREATED");
        return payment;
    }

    public JsonNode getPayload(Payment Payment){
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            // adding serializer to properly handler bigdecimal values.
            module.addSerializer(BigDecimal.class, new ToStringSerializer());
            mapper.registerModule(module);
            JsonNode jsonNode = mapper.convertValue(Payment, JsonNode.class);
            return jsonNode;

    }
}
