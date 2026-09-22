-- 将已有的绝对时间转换为北京时间的本地时间，并固定精确到秒。
-- timestamp without time zone 不会根据数据库连接时区改变显示结果。

ALTER TABLE contact_message
    ALTER COLUMN submitted_at DROP DEFAULT;

ALTER TABLE contact_message
    ALTER COLUMN submitted_at TYPE TIMESTAMP(0) WITHOUT TIME ZONE
    USING date_trunc('second', submitted_at AT TIME ZONE 'Asia/Shanghai');

ALTER TABLE contact_message
    ALTER COLUMN submitted_at SET DEFAULT (CURRENT_TIMESTAMP(0) AT TIME ZONE 'Asia/Shanghai');

ALTER TABLE visit_event
    ALTER COLUMN visited_at DROP DEFAULT;

ALTER TABLE visit_event
    ALTER COLUMN visited_at TYPE TIMESTAMP(0) WITHOUT TIME ZONE
    USING date_trunc('second', visited_at AT TIME ZONE 'Asia/Shanghai');

ALTER TABLE visit_event
    ALTER COLUMN visited_at SET DEFAULT (CURRENT_TIMESTAMP(0) AT TIME ZONE 'Asia/Shanghai');

COMMENT ON COLUMN contact_message.submitted_at IS '北京时间，精确到秒';
COMMENT ON COLUMN visit_event.visited_at IS '北京时间，精确到秒';
