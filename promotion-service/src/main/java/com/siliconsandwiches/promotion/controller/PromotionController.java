package com.siliconsandwiches.promotion.controller;

import com.siliconsandwiches.promotion.dto.CalculateRequest;
import com.siliconsandwiches.promotion.dto.CalculateResponse;
import com.siliconsandwiches.promotion.service.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PromotionController {

    private final PromotionService service;

    public PromotionController(PromotionService service) {
        this.service = service;
    }

    @PostMapping("/promotions/calculate")
    public ResponseEntity<CalculateResponse> calculate(@RequestBody CalculateRequest request) {
        return ResponseEntity.ok(service.calculate(request));
    }
}
