package com.siliconsandwiches.purchase.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "payments")
public class Payment {
    @Id private UUID id = UUID.randomUUID();
    @OneToOne @JoinColumn(name = "order_id", nullable = false) private Order order;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
    @Column(nullable = false, length = 3) private String currency = "RUB";
    @Column(nullable = false, length = 32) private String method = "CARD";
    @Column(name = "idempotency_key", nullable = false) private UUID idempotencyKey = UUID.randomUUID();
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public Order getOrder() { return order; } public void setOrder(Order order) { this.order = order; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
    public String getMethod() { return method; } public void setMethod(String method) { this.method = method; }
    public UUID getIdempotencyKey() { return idempotencyKey; } public void setIdempotencyKey(UUID idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
