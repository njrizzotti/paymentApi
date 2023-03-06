package com.rizzotti.portx.unit;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class BaseTest {

    protected final static String IDEMPOTENT_KEY_NOT_PRESENT = "idempotent-key not found in request headers";
    protected final static String PAYMENT_ALREADY_PRESENT_IN_DATABASE = "Payment already present";

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.openMocks(this);
    }
}
