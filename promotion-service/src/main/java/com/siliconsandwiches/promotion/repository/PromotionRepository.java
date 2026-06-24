package com.siliconsandwiches.promotion.repository;

import com.siliconsandwiches.promotion.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    Optional<Promotion> findByCode(String code);

    @Query("SELECT p FROM Promotion p WHERE p.type = :type " +
           "AND (p.validFrom IS NULL OR p.validFrom <= :now) " +
           "AND (p.validTo IS NULL OR p.validTo >= :now)")
    List<Promotion> findActiveByType(String type, Instant now);
}
