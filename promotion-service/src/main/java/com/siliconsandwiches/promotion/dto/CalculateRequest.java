package com.siliconsandwiches.promotion.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CalculateRequest {
    private String promoCode;
    private BigDecimal subtotal;
    private UUID customerId;
    private UUID orderId;

    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
}
