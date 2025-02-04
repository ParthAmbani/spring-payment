package com.payment.gateways.payGateways.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentRequest {
    @NotBlank(message = "Payment gateway is required")
    private String gateway;

    @Valid
    private CardDetails cardDetails;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    public PaymentRequest() {
    }

    public PaymentRequest(String gateway, CardDetails cardDetails, BigDecimal amount) {
        this.gateway = gateway;
        this.cardDetails = cardDetails;
        this.amount = amount;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(CardDetails cardDetails) {
        this.cardDetails = cardDetails;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
