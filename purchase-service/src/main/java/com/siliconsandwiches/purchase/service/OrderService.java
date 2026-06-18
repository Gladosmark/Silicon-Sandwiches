package com.siliconsandwiches.purchase.service;

import com.siliconsandwiches.purchase.dto.CreateOrderRequest;
import com.siliconsandwiches.purchase.dto.CreateOrderResponse;
import com.siliconsandwiches.purchase.entity.Order;
import com.siliconsandwiches.purchase.entity.Payment;
import com.siliconsandwiches.purchase.repository.OrderRepository;
import com.siliconsandwiches.purchase.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

    public OrderService(OrderRepository orderRepo, PaymentRepository paymentRepo) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        
        String raw = request.getCustomerId() + "|" + request.getItems().toString();
        String hash = sha256(raw);

        
        Optional<Order> existing = orderRepo.findByIdempotencyHash(hash);
        if (existing.isPresent()) {
            Order o = existing.get();
            return new CreateOrderResponse(o.getId(), o.getStatus(), o.getTotal(), o.getCurrency());
        }

       
        BigDecimal total = BigDecimal.ZERO;
        for (var item : request.getItems()) {
            total = total.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setTotal(total);
        order.setCurrency(request.getCurrency() != null ? request.getCurrency() : "RUB");
        order.setIdempotencyHash(hash);
        order.setUpdatedAt(Instant.now());
        orderRepo.save(order);

        Payment payment = new Payment();
        payment.setAmount(total);
        payment.setMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "CARD");
        payment.setOrder(order);
        paymentRepo.save(payment);

        order.setStatus("PAID");
        orderRepo.save(order);

        return new CreateOrderResponse(order.getId(), "PAID", total, order.getCurrency());
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}