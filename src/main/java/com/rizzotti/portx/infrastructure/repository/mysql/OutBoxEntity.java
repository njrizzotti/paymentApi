package com.rizzotti.portx.infrastructure.repository.mysql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.rizzotti.portx.domain.Payment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "outbox")
public class OutBoxEntity {

    @Id
    private Integer aggregateId;
    private String eventType;
    private String payload;
    private Date createdOn;

    public OutBoxEntity(){}

    public OutBoxEntity(Payment payment){
        this.aggregateId = payment.getId();
        this.eventType = payment.getStatus();
        this.payload = getPayload(payment).toString();
    }

    public OutBoxEntity(Integer aggregateId, String eventType, String payload, Date createdOn) {
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdOn = createdOn;
    }

    public Integer getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(Integer aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Payment toPayment(){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this.getPayload(),Payment.class);
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
