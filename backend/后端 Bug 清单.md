# Snail 项目后端 Bug 清单

**创建日期**: 2026-03-30  
**测试工程师**: 后端测试工程师  
**状态**: 持续更新中

---

## Bug 概览

| ID | 模块 | 严重程度 | 状态 | 标题 |
|----|------|----------|------|------|
| BUG-001 | 用户任务 | 中 | 待修复 | 无效状态码返回 500 而非 400 |
| BUG-002 | 认证 | 低 | 待确认 | Refresh Token 未实现 |

---

## Bug 详情

### BUG-001: 无效状态码返回 500 而非 400

**模块**: 用户任务 API (UserTaskController)  
**严重程度**: 🟡 中  
**状态**: 🔴 待修复  
**发现日期**: 2026-03-30

#### 问题描述
当调用 `PUT /api/user-tasks/{id}/status/{status}` 接口时，如果传入无效的状态值（如 "INVALID_STATUS"），系统抛出 500 Internal Server Error，而非更合适的 400 Bad Request。

#### 复现步骤
1. 登录获取有效 Token
2. 发送请求：`PUT /api/user-tasks/1/status/INVALID_STATUS`
3. 观察响应状态码

#### 预期结果
返回 400 Bad Request，并包含错误信息："无效的任务状态：INVALID_STATUS"

#### 实际结果
返回 500 Internal Server Error

#### 根本原因
代码中直接使用 `UserTaskStatus.valueOf(status.toUpperCase())`，当传入无效值时抛出 `IllegalArgumentException`，未被捕获处理。

#### 建议修复
```java
try {
    UserTaskStatus status = UserTaskStatus.valueOf(statusStr.toUpperCase());
    // ...
} catch (IllegalArgumentException e) {
    return ResponseEntity.badRequest()
        .body(Map.of("error", "无效的任务状态：" + statusStr));
}
```

#### 影响范围
- 用户体验：错误信息不友好
- 前端处理：难以区分服务器错误和参数错误

---

### BUG-002: Refresh Token 未实现

**模块**: 认证 API (AuthController)  
**严重程度**: 🟢 低  
**状态**: 🟡 待确认  
**发现日期**: 2026-03-30

#### 问题描述
`AuthResponse` 中的 `refreshToken` 字段始终返回 null，Refresh Token 功能未实现。

#### 复现步骤
1. 调用 `POST /api/auth/login`
2. 检查响应中的 `refreshToken` 字段

#### 预期结果
返回有效的 refresh token，用于 accessToken 过期后刷新

#### 实际结果
`refreshToken` 为 null

#### 代码位置
`AuthController.java` line 45, 72

#### 建议
- 如确认需要 Refresh Token 功能，需实现：
  - Refresh Token 生成和存储
  - `/api/auth/refresh` 端点
  - Refresh Token 过期策略
- 如不需要，可从响应中移除此字段，避免混淆

---

## 已修复 Bug

暂无

---

## 安全建议

### SEC-001: 密码策略增强
**严重程度**: 🟡 中  
**状态**: 🟡 待讨论

当前密码验证较弱，建议：
- 最小长度 8 位
- 包含大小写字母、数字、特殊字符
- 检查常见弱密码字典

### SEC-002: 登录失败次数限制
**严重程度**: 🟠 高  
**状态**: 🟡 待讨论

当前无登录失败次数限制，存在暴力破解风险。建议：
- 连续失败 5 次后锁定账户 15 分钟
- 记录登录失败日志
- 可选：添加验证码

### SEC-003: JWT Token 过期时间配置
**严重程度**: 🟢 低  
**状态**: 🟡 待确认

当前 Token 过期时间硬编码为 3600 秒（1 小时），建议：
- 移至配置文件
- 支持不同用户角色的不同过期时间

---

## 性能建议

### PERF-001: 数据库查询优化
**模块**: 积分交易查询  
**严重程度**: 🟢 低

`getTransactionsByUser` 未限制返回数量，大数据量时可能影响性能。

建议：
- 添加分页参数
- 默认限制返回最近 50 条
- 添加时间范围筛选

### PERF-002: 任务列表缓存
**模块**: 任务 API  
**严重程度**: 🟢 低

任务列表读取频繁，建议添加 Redis 缓存，TTL 5 分钟。

---

## 附录：测试环境信息

- **Spring Boot**: 3.2.0
- **Java**: 17
- **数据库**: MySQL 8.0 (生产) / H2 (测试)
- **安全框架**: Spring Security + JWT

---

**最后更新**: 2026-03-30  
**下次审查**: 2026-04-06
