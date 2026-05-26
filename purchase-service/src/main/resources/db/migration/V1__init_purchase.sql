CREATE TABLE IF NOT EXISTS orders (
    id               UUID PRIMARY KEY,
    customer_id      UUID NOT NULL,
    idempotency_key  VARCHAR NOT NULL,
    status           VARCHAR NOT NULL,
    delivery_address TEXT NOT NULL,
    delivery_zone    VARCHAR,
    subtotal         NUMERIC(12,2) NOT NULL DEFAULT 0,
    discount_total   NUMERIC(12,2) NOT NULL DEFAULT 0,
    total            NUMERIC(12,2) NOT NULL DEFAULT 0,
    promo_code       VARCHAR,
    promotion_info   JSONB,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);