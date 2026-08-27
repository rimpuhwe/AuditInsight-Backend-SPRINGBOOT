ALTER TABLE organisation ADD COLUMN country_code VARCHAR(2);

-- Backfill existing organisations before the column is made mandatory.
UPDATE organisation SET country_code = 'RW' WHERE country_code IS NULL;

ALTER TABLE organisation ALTER COLUMN country_code SET NOT NULL;
ALTER TABLE organisation ADD CONSTRAINT chk_organisation_country
    CHECK (country_code IN ('RW','UG','KE','TZ'));
