package com.siliconsandwiches.purchase.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
public class CreateOrderRequest {
    @NotNull private UUID customerId;
    private String currency = "RUB";
    private String paymentMethod = "CARD";
    private String promoCode;
    @NotEmpty @Valid private List<OrderItemRequest> items;
    public UUID getCustomerId() { return customerId; } public void setCustomerId(UUID customerId) { this.customerId = customerId; }
    public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
    public String getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPromoCode() { return promoCode; } public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
    public List<OrderItemRequest> getItems() { return items; } public void setItems(List<OrderItemRequest> items) { this.items = items; }
    public static class OrderItemRequest {
        @NotNull private String productId;
        @NotNull private Integer quantity;
        @NotNull private BigDecimal unitPrice;
        public String getProductId() { return productId; } public void setProductId(String productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; } public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }
}
