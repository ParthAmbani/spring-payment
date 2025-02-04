package com.payment.gateways.payGateways.service;

import com.payment.gateways.payGateways.model.PaymentResponse;
import com.payment.gateways.payGateways.model.RefundResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripePaymentService implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public StripePaymentService() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentResponse processPayment(BigDecimal amount, String currency, String cardDetails) {
        try {
            System.out.println(this);
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
            params.put("currency", currency);
            params.put("source", cardDetails); // card token

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return new PaymentResponse(paymentIntent.getId(), "success");
        } catch (StripeException e) {
            return new PaymentResponse(null, "failed: " + e.getMessage());
        }
    }

    @Override
    public RefundResponse refundPayment(String transactionId, BigDecimal amount) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("charge", transactionId);
            params.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());

            Refund refund = Refund.create(params);
            return new RefundResponse(refund.getId(), "success");
        } catch (StripeException e) {
            return new RefundResponse(null, "failed: " + e.getMessage());
        }
    }
}
