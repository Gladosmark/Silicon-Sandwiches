CREATE TABLE IF NOT EXISTS orders (
    id               UUID PRIMARY KEY,
    customer_id      UUID NOT NULL,
    status           VARCHAR(32) NOT NULL DEFAULT 'CREATED',
    total            NUMERIC(12,2) NOT NULL DEFAULT 0,
    currency         CHAR(3) NOT NULL DEFAULT 'RUB',
    idempotency_hash VARCHAR(64),
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS payments (
    id         UUID PRIMARY KEY,
    order_id   UUID NOT NULL REFERENCES orders(id),
    amount     NUMERIC(12,2) NOT NULL,
    currency   CHAR(3) NOT NULL DEFAULT 'RUB',
    method     VARCHAR(32) NOT NULL DEFAULT 'CARD',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_orders_idempotency ON orders(idempotency_hash);