# luxixi_backend

露西西项目后端首期实现。当前只包含联系留言模块。

## 本地启动

1. 安装 Java 21、Docker Desktop。
2. 执行 `docker compose up -d` 启动 PostgreSQL。
3. 使用 Maven 执行 `mvn spring-boot:run`。

接口：`POST /api/v1/contact-messages`

请求示例：`{"visitorName":"访客","message":"你好"}`

数据库表结构由 Flyway 在应用启动时自动创建，应用使用 `ddl-auto=validate` 防止 Hibernate 修改表结构。
时间字段统一保存为北京时间的本地时间，精确到秒，数据库字段使用 `timestamp without time zone`，避免不同数据库连接的时区设置改变显示结果。