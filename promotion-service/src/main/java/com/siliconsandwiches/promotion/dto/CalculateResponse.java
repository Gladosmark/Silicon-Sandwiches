package com.siliconsandwiches.promotion.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CalculateResponse {
    private BigDecimal discount;
    private BigDecimal finalTotal;
    private List<AppliedPromotion> appliedPromotions;

    public CalculateResponse() {}
    public CalculateResponse(BigDecimal discount, BigDecimal finalTotal, List<AppliedPromotion> appliedPromotions) {
        this.discount = discount;
        this.finalTotal = finalTotal;
        this.appliedPromotions = appliedPromotions;
    }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public BigDecimal getFinalTotal() { return finalTotal; }
    public void setFinalTotal(BigDecimal finalTotal) { this.finalTotal = finalTotal; }
    public List<AppliedPromotion> getAppliedPromotions() { return appliedPromotions; }
    public void setAppliedPromotions(List<AppliedPromotion> appliedPromotions) { this.appliedPromotions = appliedPromotions; }

    public static class AppliedPromotion {
        private UUID promotionId;
        private String code;
        private String type;
        private BigDecimal discount;

        public AppliedPromotion() {}
        public AppliedPromotion(UUID promotionId, String code, String type, BigDecimal discount) {
            this.promotionId = promotionId;
            this.code = code;
            this.type = type;
            this.discount = discount;
        }

        public UUID getPromotionId() { return promotionId; }
        public void setPromotionId(UUID promotionId) { this.promotionId = promotionId; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public BigDecimal getDiscount() { return discount; }
        public void setDiscount(BigDecimal discount) { this.discount = discount; }
    }
}
