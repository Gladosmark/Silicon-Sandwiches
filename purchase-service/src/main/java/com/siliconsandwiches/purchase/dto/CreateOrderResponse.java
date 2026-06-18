package com.siliconsandwiches.purchase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOrderResponse {

    private UUID orderId;
    private String status;
    private BigDecimal total;
    private String currency;

    public CreateOrderResponse() {}
    public CreateOrderResponse(UUID orderId, String status, BigDecimal total, String currency) {
        this.orderId = orderId;
        this.status = status;
        this.total = total;
        this.currency = currency;
    }

    public UUID getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public BigDecimal getTotal() { return total; }
    public String getCurrency() { return currency; }
}