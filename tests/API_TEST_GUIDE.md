# 🐌 Snail 项目 API 测试指南

> 版本：1.0 | 更新日期：2026-03-30 | 测试工程师：AI Agent

---

## 📋 目录

1. [测试环境](#1-测试环境)
2. [认证流程](#2-认证流程)
3. [API 测试用例](#3-api-测试用例)
4. [测试工具](#4-测试工具)
5. [常见问题](#5-常见问题)

---

## 1. 测试环境

### 1.1 环境配置

| 环境 | 基础 URL | 用途 |
|------|---------|------|
| 本地开发 | `http://localhost:8080` | 开发调试 |
| 测试环境 | `https://test-api.snail.com` | 集成测试 |
| 预发布 | `https://staging-api.snail.com` | 验收测试 |
| 生产环境 | `https://api.snail.com` | 线上验证 |

### 1.2 测试账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 普通用户 | testuser | Test123456 | 基础功能测试 |
| 管理员 | admin | Admin123456 | 管理功能测试 |
| 禁用用户 | disabled | Test123456 | 权限测试 |

### 1.3 测试数据清理

每次测试前执行：
```bash
# 清理测试数据
curl -X DELETE http://localhost:8080/admin/test/cleanup \
  -H "Authorization: Bearer <admin-token>"
```

---

## 2. 认证流程

### 2.1 获取 Token

#### 注册新用户
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123456",
    "nickname": "Test User"
  }'
```

**响应示例：**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "nickname": "Test User",
    "role": "USER",
    "enabled": true
  }
}
```

#### 登录
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test123456"
  }'
```

### 2.2 使用 Token

所有需要认证的 API 请求：
```bash
curl -X GET http://localhost:8080/api/tasks \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 2.3 Token 过期处理

Token 过期后返回 401：
```json
{
  "status": 401,
  "code": "AUTHENTICATION_FAILED",
  "message": "Token expired",
  "path": "/api/tasks"
}
```

需要重新登录获取新 Token。

---

## 3. API 测试用例

### 3.1 健康检查 API

#### 测试服务健康状态
```bash
curl -X GET http://localhost:8080/public/health
```

**预期响应：**
```json
{
  "status": "UP",
  "service": "snail-backend",
  "timestamp": 1711776000000
}
```

**测试要点：**
- [ ] 返回状态码 200
- [ ] status 字段为 "UP"
- [ ] timestamp 字段存在且为数字
- [ ] 无需认证即可访问

---

### 3.2 认证 API

#### 测试注册 - 成功场景
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "Test123456",
    "nickname": "New User"
  }'
```

**验证点：**
- [ ] 状态码 200
- [ ] 返回 token
- [ ] 用户信息正确
- [ ] 数据库中创建了新用户

#### 测试注册 - 用户名重复
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "existinguser",
    "email": "newemail@example.com",
    "password": "Test123456"
  }'
```

**预期响应：**
```json
{
  "status": 401,
  "code": "AUTHENTICATION_FAILED",
  "message": "Username already exists",
  "path": "/auth/register"
}
```

#### 测试注册 - 邮箱重复
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "existing@example.com",
    "password": "Test123456"
  }'
```

**预期响应：**
```json
{
  "status": 401,
  "code": "AUTHENTICATION_FAILED",
  "message": "Email already exists",
  "path": "/auth/register"
}
```

#### 测试注册 - 验证失败
```bash
# 用户名太短
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ab",
    "email": "test@example.com",
    "password": "Test123456"
  }'

# 邮箱格式错误
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "invalid-email",
    "password": "Test123456"
  }'

# 密码太短
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "123"
  }'
```

**预期响应：**
```json
{
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "Validation failed",
  "path": "/auth/register",
  "errors": {
    "username": "Username must be between 3 and 50 characters",
    "email": "Email should be valid",
    "password": "Password must be at least 6 characters"
  }
}
```

#### 测试登录 - 成功场景
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test123456"
  }'
```

#### 测试登录 - 密码错误
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "WrongPassword"
  }'
```

**预期响应：**
```json
{
  "status": 401,
  "code": "AUTHENTICATION_FAILED",
  "message": "Invalid username or password",
  "path": "/auth/login"
}
```

---

### 3.3 任务 API

#### 获取活跃任务列表
```bash
curl -X GET http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <token>"
```

**预期响应：**
```json
[
  {
    "id": 1,
    "title": "每日签到",
    "description": "每日签到获取积分",
    "type": "DAILY",
    "status": "ACTIVE",
    "rewardPoints": 10,
    "imageUrl": "https://...",
    "startDate": "2026-03-30T00:00:00",
    "endDate": "2026-12-31T23:59:59"
  }
]
```

#### 获取任务详情
```bash
curl -X GET http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer <token>"
```

#### 按类型获取任务
```bash
# 获取每日任务
curl -X GET http://localhost:8080/api/tasks/type/DAILY \
  -H "Authorization: Bearer <token>"

# 获取一次性任务
curl -X GET http://localhost:8080/api/tasks/type/ONE_TIME \
  -H "Authorization: Bearer <token>"
```

#### 获取用户任务进度
```bash
curl -X GET http://localhost:8080/api/tasks/user/100 \
  -H "Authorization: Bearer <token>"
```

**预期响应：**
```json
[
  {
    "id": 1,
    "userId": 100,
    "taskId": 1,
    "status": "PENDING",
    "completedAt": null,
    "pointsEarned": null,
    "completionProof": null
  }
]
```

#### 开始任务
```bash
curl -X POST "http://localhost:8080/api/tasks/1/start?userId=100" \
  -H "Authorization: Bearer <token>"
```

**预期响应：**
```json
{
  "id": 1,
  "userId": 100,
  "taskId": 1,
  "status": "PENDING"
}
```

#### 完成任务
```bash
curl -X POST "http://localhost:8080/api/tasks/1/complete?userId=100" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "completionProof": "screenshot.jpg"
  }'
```

**预期响应：**
```json
{
  "id": 1,
  "userId": 100,
  "taskId": 1,
  "status": "COMPLETED",
  "completedAt": "2026-03-30T10:30:00",
  "pointsEarned": 10,
  "completionProof": "screenshot.jpg"
}
```

#### 创建任务（管理员）
```bash
curl -X POST http://localhost:8080/api/tasks/create \
  -H "Authorization: Bearer <admin-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "新用户任务",
    "description": "完成新手引导",
    "type": "ONE_TIME",
    "rewardPoints": 100,
    "maxCompletions": 1
  }'
```

---

### 3.4 积分 API

#### 获取用户余额
```bash
curl -X GET http://localhost:8080/api/points/balance/100 \
  -H "Authorization: Bearer <token>"
```

**预期响应：**
```json
{
  "userId": 100,
  "balance": 1000
}
```

#### 获取账户详情
```bash
curl -X GET http://localhost:8080/api/points/account/100 \
  -H "Authorization: Bearer <token>"
```

**预期响应：**
```json
{
  "id": 1,
  "userId": 100,
  "balance": 1000,
  "totalEarned": 1500,
  "totalSpent": 500,
  "frozenPoints": 0,
  "version": 1
}
```

#### 获取交易历史
```bash
curl -X GET http://localhost:8080/api/points/transactions/100 \
  -H "Authorization: Bearer <token>"
```

**预期响应：**
```json
[
  {
    "id": 1,
    "userId": 100,
    "points": 100,
    "type": "EARN",
    "category": "TASK_REWARD",
    "description": "完成每日签到",
    "referenceId": 1,
    "balanceAfter": 1000,
    "createdAt": "2026-03-30T10:00:00"
  },
  {
    "id": 2,
    "userId": 100,
    "points": 50,
    "type": "SPEND",
    "category": "PURCHASE",
    "description": "购买商品",
    "referenceId": 10,
    "balanceAfter": 950,
    "createdAt": "2026-03-30T11:00:00"
  }
]
```

#### 增加积分
```bash
curl -X POST "http://localhost:8080/api/points/add?userId=100&points=500&category=TASK_REWARD&description=完成任务&referenceId=1" \
  -H "Authorization: Bearer <token>"
```

**预期响应：**
```json
{
  "id": 1,
  "userId": 100,
  "balance": 1500,
  "totalEarned": 2000,
  "totalSpent": 500
}
```

#### 消费积分
```bash
curl -X POST "http://localhost:8080/api/points/spend?userId=100&points=300&category=PURCHASE&description=购买商品&referenceId=10" \
  -H "Authorization: Bearer <token>"
```

**预期响应：**
```json
{
  "id": 1,
  "userId": 100,
  "balance": 700,
  "totalEarned": 1500,
  "totalSpent": 800
}
```

#### 调整积分（管理员）
```bash
# 增加积分
curl -X POST "http://localhost:8080/api/points/adjust?userId=100&points=200&description=管理员奖励" \
  -H "Authorization: Bearer <admin-token>"

# 扣减积分
curl -X POST "http://localhost:8080/api/points/adjust?userId=100&points=-100&description=违规处罚" \
  -H "Authorization: Bearer <admin-token>"
```

---

## 4. 测试工具

### 4.1 cURL 测试脚本

创建测试脚本 `test-api.sh`：

```bash
#!/bin/bash

BASE_URL="http://localhost:8080"

# 测试健康检查
echo "Testing health check..."
curl -s "$BASE_URL/public/health" | jq .

# 测试注册
echo "Testing registration..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123456",
    "nickname": "Test User"
  }')

echo "$REGISTER_RESPONSE" | jq .

# 提取 Token
TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.token')

# 测试获取任务
echo "Testing get tasks..."
curl -s -X GET "$BASE_URL/api/tasks" \
  -H "Authorization: Bearer $TOKEN" | jq .

# 测试登录
echo "Testing login..."
curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test123456"
  }' | jq .
```

### 4.2 Postman 集合

导入 Postman 集合文件（见 `POSTMAN_COLLECTION.json`）。

### 4.3 自动化测试

运行单元测试：
```bash
cd backend
mvn test
```

运行集成测试：
```bash
mvn test -Dtest="*IntegrationTest"
```

生成覆盖率报告：
```bash
mvn clean test jacoco:report
```

查看报告：
```bash
open backend/target/site/jacoco/index.html
```

---

## 5. 常见问题

### 5.1 认证问题

**Q: Token 过期怎么办？**
A: Token 有效期为 24 小时，过期后需要重新登录获取新 Token。

**Q: 如何测试需要认证的接口？**
A: 先调用登录接口获取 Token，然后在请求头中添加 `Authorization: Bearer <token>`。

### 5.2 验证问题

**Q: 注册时提示验证失败？**
A: 检查以下要求：
- 用户名：3-50 个字符
- 邮箱：有效的邮箱格式
- 密码：至少 6 个字符

### 5.3 数据问题

**Q: 如何清理测试数据？**
A: 使用管理员账号调用清理接口，或直接操作数据库。

**Q: 积分余额不对？**
A: 检查交易历史记录，确认所有积分变动都有记录。

### 5.4 性能问题

**Q: 如何测试 API 性能？**
A: 使用工具如 Apache Bench (ab) 或 JMeter：

```bash
# 使用 ab 测试
ab -n 1000 -c 10 -H "Authorization: Bearer <token>" \
  http://localhost:8080/api/tasks
```

---

## 6. 测试检查清单

### 6.1 功能测试
- [ ] 用户可以正常注册
- [ ] 用户可以正常登录
- [ ] Token 认证正常工作
- [ ] 任务列表可以正常获取
- [ ] 任务可以开始和完成
- [ ] 积分可以正常增减
- [ ] 交易记录正确记录

### 6.2 验证测试
- [ ] 用户名长度验证
- [ ] 邮箱格式验证
- [ ] 密码长度验证
- [ ] 重复用户名检测
- [ ] 重复邮箱检测

### 6.3 错误处理
- [ ] 无效 Token 返回 401
- [ ] 资源不存在返回 404
- [ ] 验证失败返回 400
- [ ] 服务器错误返回 500

### 6.4 安全测试
- [ ] 密码加密存储
- [ ] Token 安全传输
- [ ] SQL 注入防护
- [ ] XSS 防护

---

*文档版本：1.0*  
*最后更新：2026-03-30*  
*维护者：Snail 测试团队*
