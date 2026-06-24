package com.siliconsandwiches.promotion.repository;

import com.siliconsandwiches.promotion.entity.PromotionUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, UUID> {
    boolean existsByOrderIdAndPromotionId(UUID orderId, UUID promotionId);
}
