package com.luxixi.backend.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * 生成项目使用的北京时间本地时间值。
 *
 * <p>这类值会写入 PostgreSQL 的 timestamp without time zone 字段，
 * 因此数据库客户端不会再根据连接时区换算显示结果。</p>
 */
public final class BeijingTime {
    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    private BeijingTime() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE_ID).truncatedTo(ChronoUnit.SECONDS);
    }
}
