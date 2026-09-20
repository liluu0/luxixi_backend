ALTER TABLE visit_event
    ALTER COLUMN client_ip TYPE VARCHAR(64)
    USING client_ip::text;
