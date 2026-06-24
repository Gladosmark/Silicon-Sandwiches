package com.siliconsandwiches.purchase.service;
import com.siliconsandwiches.purchase.dto.CreateOrderRequest;
import com.siliconsandwiches.purchase.dto.CreateOrderResponse;
import com.siliconsandwiches.purchase.entity.Order;
import com.siliconsandwiches.purchase.entity.Payment;
import com.siliconsandwiches.purchase.repository.OrderRepository;
import com.siliconsandwiches.purchase.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final RestTemplate restTemplate;
    @Value("${promotion.url}") private String promotionUrl;
    public OrderService(OrderRepository orderRepo, PaymentRepository paymentRepo, RestTemplate restTemplate) {
        this.orderRepo = orderRepo; this.paymentRepo = paymentRepo; this.restTemplate = restTemplate;
    }
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        UUID idempotencyKey = UUID.randomUUID();
        Optional<Payment> existing = paymentRepo.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            Order o = existing.get().getOrder();
            return new CreateOrderResponse(o.getId(), o.getStatus(), o.getTotal(), o.getCurrency());
        }
        BigDecimal total = BigDecimal.ZERO;
        for (var item : request.getItems()) {
            total = total.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {
            try {
                var promoResponse = restTemplate.postForObject(promotionUrl,
                    Map.of("promoCode", request.getPromoCode(), "subtotal", total, "customerId", request.getCustomerId()), Map.class);
                if (promoResponse != null && promoResponse.get("discount") != null) {
                    total = total.subtract(new BigDecimal(promoResponse.get("discount").toString()));
                }
            } catch (Exception e) {}
        }
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setTotal(total);
        order.setCurrency(request.getCurrency() != null ? request.getCurrency() : "RUB");
        order.setUpdatedAt(Instant.now());
        orderRepo.save(order);
        Payment payment = new Payment();
        payment.setAmount(total);
        payment.setMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "CARD");
        payment.setIdempotencyKey(idempotencyKey);
        payment.setOrder(order);
        paymentRepo.save(payment);
        order.setStatus("PAID");
        orderRepo.save(order);
        return new CreateOrderResponse(order.getId(), "PAID", total, order.getCurrency());
    }
}
