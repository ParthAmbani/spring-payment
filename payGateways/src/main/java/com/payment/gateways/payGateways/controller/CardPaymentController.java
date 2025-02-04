package com.payment.gateways.payGateways.controller;

import com.payment.gateways.payGateways.model.EncryptedPaymentRequest;
import com.payment.gateways.payGateways.utils.EncryptionUtil;
import com.payment.gateways.payGateways.model.PaymentRequest;
import com.payment.gateways.payGateways.model.PaymentResponse;
import com.payment.gateways.payGateways.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/card/payments")
public class CardPaymentController {

    @Autowired
    private Map<String, PaymentService> paymentServices;

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody EncryptedPaymentRequest request) {
        PaymentRequest paymentRequest = null;
        try {
            System.out.println("Received encrypted data: " + request.getData());
            paymentRequest = EncryptionUtil.decrypt(request.getData());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }

        PaymentService paymentService = paymentServices.get(paymentRequest.getGateway() + "PaymentService");
        if (paymentService == null) {
            return ResponseEntity.badRequest().body("Invalid payment gateway");
        }

        // Process the payment via the selected gateway
        PaymentResponse response = paymentService.processPayment(
                paymentRequest.getAmount(),
                "INR",  // Defaulting to INR for this example
                paymentRequest.getCardDetails().getNumber()  // Pass actual card details here
        );

        return ResponseEntity.ok(response);
    }
}

