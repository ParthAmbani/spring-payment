package com.payment.gateways.payGateways.service;

import com.payment.gateways.payGateways.model.PaymentResponse;
import com.payment.gateways.payGateways.model.RefundResponse;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
public class PayPalPaymentService implements PaymentService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    private APIContext getApiContext() {
        return new APIContext(clientId, clientSecret, "sandbox");
    }

    @Override
    public PaymentResponse processPayment(BigDecimal amount, String currency, String cardDetails) {
        System.out.println(this);
        Amount amt = new Amount();
        amt.setCurrency(currency);
        amt.setTotal(String.format("%.2f", amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amt);

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(new Payer().setPaymentMethod("credit_card")); // Example with credit card
        payment.setTransactions(Collections.singletonList(transaction));

        try {
            Payment createdPayment = payment.create(getApiContext());
            return new PaymentResponse(createdPayment.getId(), "success");
        } catch (PayPalRESTException e) {
            return new PaymentResponse(null, "failed: " + e.getMessage());
        }
    }

    @Override
    public RefundResponse refundPayment(String transactionId, BigDecimal amount) {
        // PayPal refund logic here
        return new RefundResponse(transactionId, "success");
    }
}
