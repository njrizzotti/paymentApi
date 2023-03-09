package com.rizzotti.portx.application.rest;

import com.rizzotti.portx.domain.Payment;
import com.rizzotti.portx.exception.CustomErrorException;
import com.rizzotti.portx.domain.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.rizzotti.portx.utils.Constants.IDEMPOTENT_KEY;

@RestController
public class PaymentsController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/payments/{id}")
    public ResponseEntity getPaymentById(@PathVariable(name = "id",required = true) Integer id) {
        Optional<Payment> result = paymentService.checkStatus(id);
        if(result.isPresent()){
            return new ResponseEntity(result.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/payments")
    public ResponseEntity createPayment(@RequestBody Payment payment, @RequestHeader Map<String, String> headers) throws CustomErrorException {
        if(!isValidIdempotentKey(headers)){
            throw new CustomErrorException(IDEMPOTENT_KEY + " not found in request headers");
        }
        return new ResponseEntity(paymentService.savePayment(payment, headers), HttpStatus.ACCEPTED);
    }

    private boolean isValidIdempotentKey(Map<String, String> map){
        if(!map.containsKey(IDEMPOTENT_KEY) || map.get(IDEMPOTENT_KEY).isBlank()){
            return false;
        }
        return true;
    }
}
