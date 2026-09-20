CREATE TABLE contact_message (
  id BIGSERIAL PRIMARY KEY,
  visitor_name VARCHAR(80) NOT NULL,
  message VARCHAR(2000) NOT NULL,
  submitted_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_contact_message_submitted_at ON contact_message (submitted_at DESC);
COMMENT ON TABLE contact_message IS '联系模块留言';
COMMENT ON COLUMN contact_message.visitor_name IS '留言人';
COMMENT ON COLUMN contact_message.message IS '留言内容';
COMMENT ON COLUMN contact_message.submitted_at IS '提交时间';
