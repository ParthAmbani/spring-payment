package com.payment.gateways.payGateways.service;

import com.payment.gateways.payGateways.model.PaymentResponse;
import com.payment.gateways.payGateways.model.RefundResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RazorpayPaymentService implements PaymentService {

    @Value("${razorpay.api.key}")
    private String razorpayKey;

    @Value("${razorpay.api.secret}")
    private String razorpaySecret;

    private RazorpayClient client;

    @PostConstruct
    public void init() throws RazorpayException {
        client = new RazorpayClient(razorpayKey, razorpaySecret);
    }

    @Override
    public PaymentResponse processPayment(BigDecimal amount, String currency, String cardDetails) {
        try {
            System.out.println(this);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", "order_rcptid_11");

            Order order = client.orders.create(orderRequest);
            return new PaymentResponse(order.get("id"), "success");
        } catch (RazorpayException e) {
            return new PaymentResponse(null, "failed: " + e.getMessage());
        }
    }

    @Override
    public RefundResponse refundPayment(String transactionId, BigDecimal amount) {
        // Razorpay refund logic here
        return new RefundResponse(transactionId, "success");
    }
}
