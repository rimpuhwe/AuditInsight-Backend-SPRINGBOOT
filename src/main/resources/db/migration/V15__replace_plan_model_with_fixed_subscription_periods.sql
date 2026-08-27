-- Replace the 4-tier USD plan catalog with 3 fixed-price RWF subscription periods
-- (MONTHLY/SIX_MONTHS/ANNUAL) plus a TRIAL lifecycle state. Existing rows may still carry
-- legacy plan tiers (FREE/STARTER/PROFESSIONAL/ENTERPRISE, seeded in V9 and exercised by the
-- now-removed V11 sandbox-price hack) which have no fixed-duration equivalent, so those values
-- are cleared to NULL rather than dropped, preserving the historical rows.

ALTER TABLE subscriptions RENAME COLUMN plan_tier TO subscription_type;
ALTER TABLE payments RENAME COLUMN plan_tier TO subscription_type;

-- Drops the plan_tier FK constraints on subscriptions/payments along with the table itself.
DROP TABLE plans CASCADE;

ALTER TABLE subscriptions
    ALTER COLUMN subscription_type DROP NOT NULL;

-- Legacy plan tiers (FREE/STARTER/PROFESSIONAL/ENTERPRISE) don't map to a fixed subscription
-- period; clear them so the new check constraint can be added without rejecting old rows.
UPDATE subscriptions SET subscription_type = NULL
    WHERE subscription_type NOT IN ('MONTHLY','SIX_MONTHS','ANNUAL');

ALTER TABLE subscriptions
    ADD CONSTRAINT chk_subscriptions_subscription_type
        CHECK (subscription_type IN ('MONTHLY','SIX_MONTHS','ANNUAL')),
    DROP COLUMN billing_cycle,
    ADD COLUMN trial_reminder_sent BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN expiry_reminder_sent BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE subscriptions DROP CONSTRAINT subscriptions_status_check;
ALTER TABLE subscriptions ADD CONSTRAINT subscriptions_status_check
    CHECK (status IN ('TRIAL','PENDING','ACTIVE','EXPIRED','CANCELLED'));

CREATE INDEX idx_subscriptions_end_date_status ON subscriptions (end_date, status);

ALTER TABLE payments
    ALTER COLUMN subscription_type DROP NOT NULL;

UPDATE payments SET subscription_type = NULL
    WHERE subscription_type NOT IN ('MONTHLY','SIX_MONTHS','ANNUAL');

ALTER TABLE payments
    ADD CONSTRAINT chk_payments_subscription_type
        CHECK (subscription_type IN ('MONTHLY','SIX_MONTHS','ANNUAL')),
    DROP COLUMN billing_cycle,
    DROP COLUMN usd_amount,
    DROP COLUMN exchange_rate,
    ADD COLUMN received_amount NUMERIC(18,2);

ALTER TABLE payments RENAME COLUMN charged_amount TO expected_amount;
ALTER TABLE payments RENAME COLUMN charged_currency TO currency;
ALTER TABLE payments RENAME COLUMN provider_reference TO provider_transaction_id;

ALTER TABLE payments DROP CONSTRAINT payments_status_check;
ALTER TABLE payments ADD CONSTRAINT payments_status_check
    CHECK (status IN ('PENDING','SUCCESSFUL','FAILED','CANCELLED','EXPIRED','UNDERPAID'));
