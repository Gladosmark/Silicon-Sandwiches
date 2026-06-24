CREATE TABLE IF NOT EXISTS promotions (
    id           UUID PRIMARY KEY,
    code         VARCHAR(64) NOT NULL UNIQUE,
    type         VARCHAR(16) NOT NULL DEFAULT 'global',
    discount_pct NUMERIC(5,2) NOT NULL,
    valid_from   TIMESTAMPTZ,
    valid_to     TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS promotion_usage (
    id           UUID PRIMARY KEY,
    promotion_id UUID NOT NULL REFERENCES promotions(id),
    order_id     UUID NOT NULL,
    customer_id  UUID NOT NULL,
    used_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
