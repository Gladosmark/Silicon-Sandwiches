package com.siliconsandwiches.purchase.service;

import com.siliconsandwiches.purchase.dto.CreateOrderRequest;
import com.siliconsandwiches.purchase.dto.CreateOrderResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final JdbcTemplate jdbc;
    private final IdempotencyService idempotency;

    public OrderService(JdbcTemplate jdbc, IdempotencyService idempotency) {
        this.jdbc = jdbc;
        this.idempotency = idempotency;
    }

    @Transactional
    public CreateOrderResponse createOrder(String idempotencyKey, CreateOrderRequest request) {
        // 1. Проверяем идемпотентность
        Optional<CreateOrderResponse> cached = idempotency.getCachedResponse(idempotencyKey);
        if (cached.isPresent()) {
            return cached.get();
        }

        UUID orderId = UUID.randomUUID();
        Timestamp now = Timestamp.from(Instant.now());

        // 2. Считаем суммы
        BigDecimal subtotal = BigDecimal.ZERO;
        for (var item : request.getItems()) {
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(itemTotal);
        }

        BigDecimal discountTotal = BigDecimal.ZERO;
        BigDecimal total = subtotal.subtract(discountTotal);

        // 3. Вставляем заказ
        jdbc.update(
            "INSERT INTO orders (id, customer_id, idempotency_key, status, delivery_address, " +
            "delivery_zone, subtotal, discount_total, total, promo_code, created_at, updated_at) " +
            "VALUES (?::uuid, ?::uuid, ?, 'NEW', ?, ?, ?, ?, ?, ?, ?, ?)",
            orderId.toString(),
            request.getCustomerId().toString(),
            idempotencyKey,
            request.getDeliveryAddress(),
            request.getDeliveryZone(),
            subtotal,
            discountTotal,
            total,
            request.getPromoCode(),
            now,
            now
        );

        // 4. Формируем ответ
        var response = new CreateOrderResponse(orderId, "NEW", subtotal, discountTotal, total);

        // 5. Сохраняем в Redis для идемпотентности
        idempotency.saveResponse(idempotencyKey, response);

        return response;
    }
}