package com.payment.gateways.payGateways.service;

import com.payment.gateways.payGateways.model.PaymentResponse;
import com.payment.gateways.payGateways.model.RefundResponse;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentResponse processPayment(BigDecimal amount, String currency, String cardDetails);
    RefundResponse refundPayment(String transactionId, BigDecimal amount);
}
