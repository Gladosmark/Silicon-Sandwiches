package com.siliconsandwiches.purchase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOrderResponse {

    private UUID orderId;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal total;

    public CreateOrderResponse() {}

    public CreateOrderResponse(UUID orderId, String status, 
                               BigDecimal subtotal, BigDecimal discountTotal, BigDecimal total) {
        this.orderId = orderId;
        this.status = status;
        this.subtotal = subtotal;
        this.discountTotal = discountTotal;
        this.total = total;
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDiscountTotal() { return discountTotal; }
    public void setDiscountTotal(BigDecimal discountTotal) { this.discountTotal = discountTotal; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}