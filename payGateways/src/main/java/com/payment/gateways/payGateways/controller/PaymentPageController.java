package com.payment.gateways.payGateways.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentPageController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
