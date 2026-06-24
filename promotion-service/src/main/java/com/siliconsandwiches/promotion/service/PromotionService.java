package com.siliconsandwiches.promotion.service;

import com.siliconsandwiches.promotion.dto.CalculateRequest;
import com.siliconsandwiches.promotion.dto.CalculateResponse;
import com.siliconsandwiches.promotion.entity.Promotion;
import com.siliconsandwiches.promotion.entity.PromotionUsage;
import com.siliconsandwiches.promotion.repository.PromotionRepository;
import com.siliconsandwiches.promotion.repository.PromotionUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepo;
    private final PromotionUsageRepository usageRepo;

    public PromotionService(PromotionRepository promotionRepo, PromotionUsageRepository usageRepo) {
        this.promotionRepo = promotionRepo;
        this.usageRepo = usageRepo;
    }

    @Transactional
    public CalculateResponse calculate(CalculateRequest request) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        List<CalculateResponse.AppliedPromotion> applied = new ArrayList<>();
        Instant now = Instant.now();

        if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {
            Optional<Promotion> opt = promotionRepo.findByCode(request.getPromoCode());
            if (opt.isPresent()) {
                Promotion p = opt.get();
                if (isValid(p, now)) {
                    BigDecimal discount = calcDiscount(p, request.getSubtotal());
                    totalDiscount = totalDiscount.add(discount);
                    applied.add(new CalculateResponse.AppliedPromotion(p.getId(), p.getCode(), p.getType(), discount));
                    saveUsage(p, request);
                }
            }
        } else {
            List<Promotion> globals = promotionRepo.findActiveByType("global", now);
            List<Promotion> locals = promotionRepo.findActiveByType("local", now);

            BigDecimal globalMax = globals.stream()
                .map(p -> calcDiscount(p, request.getSubtotal()))
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            BigDecimal localMax = locals.stream()
                .map(p -> calcDiscount(p, request.getSubtotal()))
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

            totalDiscount = globalMax.add(localMax);
        }

        BigDecimal finalTotal = request.getSubtotal().subtract(totalDiscount);
        return new CalculateResponse(totalDiscount, finalTotal, applied);
    }

    private boolean isValid(Promotion p, Instant now) {
        if (p.getValidFrom() != null && now.isBefore(p.getValidFrom())) return false;
        if (p.getValidTo() != null && now.isAfter(p.getValidTo())) return false;
        return true;
    }

    private BigDecimal calcDiscount(Promotion p, BigDecimal subtotal) {
        return subtotal.multiply(p.getDiscountPct())
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private void saveUsage(Promotion p, CalculateRequest request) {
        if (request.getOrderId() != null
            && !usageRepo.existsByOrderIdAndPromotionId(request.getOrderId(), p.getId())) {
            PromotionUsage usage = new PromotionUsage();
            usage.setPromotion(p);
            usage.setOrderId(request.getOrderId());
            usage.setCustomerId(request.getCustomerId());
            usageRepo.save(usage);
        }
    }
}
