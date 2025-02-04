package com.payment.gateways.payGateways.model;

public class EncryptedPaymentRequest {
    private String data;

    // Default constructor
    public EncryptedPaymentRequest() {
    }

    public EncryptedPaymentRequest(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
