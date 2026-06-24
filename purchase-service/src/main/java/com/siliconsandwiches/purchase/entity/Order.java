package com.siliconsandwiches.purchase.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "orders")
public class Order {
    @Id private UUID id = UUID.randomUUID();
    @Column(name = "customer_id", nullable = false) private UUID customerId;
    @Column(nullable = false, length = 32) private String status = "CREATED";
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal total = BigDecimal.ZERO;
    @Column(nullable = false, length = 3) private String currency = "RUB";
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL) private Payment payment;
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public UUID getCustomerId() { return customerId; } public void setCustomerId(UUID customerId) { this.customerId = customerId; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotal() { return total; } public void setTotal(BigDecimal total) { this.total = total; }
    public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; if (payment != null) payment.setOrder(this); }
}
