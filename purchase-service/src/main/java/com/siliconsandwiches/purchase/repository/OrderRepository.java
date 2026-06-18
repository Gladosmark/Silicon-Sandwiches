package com.siliconsandwiches.purchase.repository;

import com.siliconsandwiches.purchase.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByIdempotencyHash(String hash);
}