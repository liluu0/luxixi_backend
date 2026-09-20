CREATE TABLE visit_event (
  id BIGSERIAL PRIMARY KEY,
  visited_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  client_ip INET,
  path VARCHAR(255) NOT NULL,
  referrer TEXT,
  user_agent TEXT,
  browser_name VARCHAR(64),
  device_type VARCHAR(32),
  geo_region VARCHAR(128)
);

CREATE INDEX idx_visit_event_visited_at ON visit_event (visited_at DESC);
CREATE INDEX idx_visit_event_path ON visit_event (path);

COMMENT ON TABLE visit_event IS '首页匿名访客访问记录';
COMMENT ON COLUMN visit_event.visited_at IS '访问时间';
COMMENT ON COLUMN visit_event.client_ip IS '访客 IP 地址';
COMMENT ON COLUMN visit_event.path IS '访问路径';
COMMENT ON COLUMN visit_event.referrer IS '来源页面';
COMMENT ON COLUMN visit_event.user_agent IS '原始 User-Agent';
COMMENT ON COLUMN visit_event.browser_name IS '浏览器名称';
COMMENT ON COLUMN visit_event.device_type IS '设备类型';
COMMENT ON COLUMN visit_event.geo_region IS '根据 IP 推断的省份或地区';
