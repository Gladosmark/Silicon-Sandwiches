package com.siliconsandwiches.purchase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siliconsandwiches.purchase.dto.CreateOrderResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class IdempotencyService {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    public IdempotencyService(StringRedisTemplate redis, ObjectMapper objectMapper) {
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    // Проверяет, был ли уже обработан запрос с таким ключом
    public Optional<CreateOrderResponse> getCachedResponse(String idempotencyKey) {
        String key = "idempotent:" + idempotencyKey;
        String cached = redis.opsForValue().get(key);
        if (cached != null) {
            try {
                return Optional.of(objectMapper.readValue(cached, CreateOrderResponse.class));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    // Сохраняет ответ в Redis на 24 часа
    public void saveResponse(String idempotencyKey, CreateOrderResponse response) {
        String key = "idempotent:" + idempotencyKey;
        try {
            String json = objectMapper.writeValueAsString(response);
            redis.opsForValue().set(key, json, Duration.ofHours(24));
        } catch (Exception e) {
            throw new RuntimeException("Failed to cache idempotent response", e);
        }
    }
}
