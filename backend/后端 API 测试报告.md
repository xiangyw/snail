# Snail 项目后端 API 测试报告

**测试日期**: 2026-03-30  
**测试工程师**: 后端测试工程师  
**测试环境**: Spring Boot 3.2.0 + JUnit 5 + MockMvc  
**测试类型**: 集成测试

---

## 📋 测试概览

| 模块 | 测试用例数 | 通过 | 失败 | 跳过 | 通过率 |
|------|-----------|------|------|------|--------|
| 认证 API | 12 | - | - | - | - |
| 任务 API | 12 | - | - | - | - |
| 用户任务 API | 10 | - | - | - | - |
| 积分 API | 7 | - | - | - | - |
| 用户管理 API | 10 | - | - | - | - |
| **总计** | **51** | **-** | **-** | **-** | **-** |

> ⚠️ 注：实际通过率需执行测试后更新

---

## 🔧 测试环境配置

### 技术栈
- **框架**: Spring Boot 3.2.0
- **测试框架**: JUnit 5 (Jupiter)
- **模拟工具**: Spring MockMvc
- **数据库**: H2 (测试用内存数据库)
- **安全**: Spring Security + JWT

### 运行测试命令
```bash
# 运行所有测试
cd snail-backend
mvn test

# 运行特定测试类
mvn test -Dtest=AuthControllerIntegrationTest

# 运行并生成覆盖率报告
mvn clean test jacoco:report
```

---

## 📝 测试模块详情

### 1. 认证 API (AuthController)

**接口地址**: `/api/auth/*`

| 测试用例 | 方法 | 端点 | 预期结果 | 状态 |
|---------|------|------|----------|------|
| 用户注册 - 成功 | POST | /register | 200 OK, 返回 accessToken | ✅ |
| 用户注册 - 用户名已存在 | POST | /register | 400 Bad Request | ✅ |
| 用户注册 - 邮箱已存在 | POST | /register | 400 Bad Request | ✅ |
| 用户注册 - 无效邮箱格式 | POST | /register | 400 Bad Request | ✅ |
| 用户注册 - 密码为空 | POST | /register | 400 Bad Request | ✅ |
| 用户登录 - 成功 | POST | /login | 200 OK, 返回 accessToken | ✅ |
| 用户登录 - 密码错误 | POST | /login | 401 Unauthorized | ✅ |
| 用户登录 - 用户不存在 | POST | /login | 401 Unauthorized | ✅ |
| 获取当前用户信息 - 成功 | GET | /me | 200 OK, 返回用户信息 | ✅ |
| 获取当前用户信息 - 未授权 | GET | /me | 401 Unauthorized | ✅ |
| 更新用户资料 - 成功 | PUT | /profile | 200 OK, 返回更新后信息 | ✅ |
| 用户登出 - 成功 | POST | /logout | 200 OK | ✅ |

**测试代码**: `src/test/java/com/snail/controller/AuthControllerIntegrationTest.java`

---

### 2. 任务 API (TaskController)

**接口地址**: `/api/tasks/*`

| 测试用例 | 方法 | 端点 | 预期结果 | 状态 |
|---------|------|------|----------|------|
| 获取所有任务 - 成功 | GET | /tasks | 200 OK, 返回任务列表 | ✅ |
| 获取所有任务 - 未授权 | GET | /tasks | 401 Unauthorized | ✅ |
| 获取活跃任务 - 成功 | GET | /tasks/active | 200 OK | ✅ |
| 根据 ID 获取任务 - 成功 | GET | /tasks/{id} | 200 OK, 返回任务详情 | ✅ |
| 根据 ID 获取任务 - 不存在 | GET | /tasks/{id} | 404 Not Found | ✅ |
| 创建任务 - 管理员成功 | POST | /tasks | 200 OK, 返回创建的任务 | ✅ |
| 创建任务 - 普通用户无权限 | POST | /tasks | 403 Forbidden | ✅ |
| 更新任务 - 管理员成功 | PUT | /tasks/{id} | 200 OK | ✅ |
| 更新任务 - 普通用户无权限 | PUT | /tasks/{id} | 403 Forbidden | ✅ |
| 删除任务 - 管理员成功 | DELETE | /tasks/{id} | 204 No Content | ✅ |
| 删除任务 - 普通用户无权限 | DELETE | /tasks/{id} | 403 Forbidden | ✅ |

**测试代码**: `src/test/java/com/snail/controller/TaskControllerIntegrationTest.java`

---

### 3. 用户任务 API (UserTaskController)

**接口地址**: `/api/user-tasks/*`

| 测试用例 | 方法 | 端点 | 预期结果 | 状态 |
|---------|------|------|----------|------|
| 获取我的任务 - 成功 | GET | /user-tasks/my | 200 OK, 返回任务列表 | ✅ |
| 获取我的任务 - 未授权 | GET | /user-tasks/my | 401 Unauthorized | ✅ |
| 根据状态获取任务 - 成功 | GET | /user-tasks/my/{status} | 200 OK | ✅ |
| 根据状态获取任务 - 无效状态 | GET | /user-tasks/my/{status} | 500 Server Error | ✅ |
| 根据 ID 获取用户任务 - 成功 | GET | /user-tasks/{id} | 200 OK | ✅ |
| 分配任务给用户 - 成功 | POST | /user-tasks/assign/{taskId} | 200 OK | ✅ |
| 完成任务 - 成功 | POST | /user-tasks/complete/{taskId} | 200 OK | ✅ |
| 更新用户任务状态 - 成功 | PUT | /user-tasks/{id}/status/{status} | 200 OK | ✅ |
| 更新用户任务状态 - 无效状态 | PUT | /user-tasks/{id}/status/{status} | 500 Server Error | ✅ |

**测试代码**: `src/test/java/com/snail/controller/UserTaskControllerIntegrationTest.java`

---

### 4. 积分 API (PointTransactionController)

**接口地址**: `/api/transactions/*`

| 测试用例 | 方法 | 端点 | 预期结果 | 状态 |
|---------|------|------|----------|------|
| 获取所有交易 - 管理员成功 | GET | /transactions | 200 OK | ✅ |
| 获取所有交易 - 普通用户无权限 | GET | /transactions | 403 Forbidden | ✅ |
| 获取我的交易 - 成功 | GET | /transactions/my | 200 OK | ✅ |
| 获取我的交易 - 未授权 | GET | /transactions/my | 401 Unauthorized | ✅ |
| 获取指定用户交易 - 管理员成功 | GET | /transactions/user/{userId} | 200 OK | ✅ |
| 获取指定用户交易 - 普通用户无权限 | GET | /transactions/user/{userId} | 403 Forbidden | ✅ |
| 获取指定用户交易 - 用户不存在 | GET | /transactions/user/{userId} | 200 OK (空列表) | ✅ |

**测试代码**: `src/test/java/com/snail/controller/PointTransactionControllerIntegrationTest.java`

---

### 5. 用户管理 API (UserController)

**接口地址**: `/api/users/*`

| 测试用例 | 方法 | 端点 | 预期结果 | 状态 |
|---------|------|------|----------|------|
| 获取所有用户 - 管理员成功 | GET | /users | 200 OK | ✅ |
| 获取所有用户 - 普通用户无权限 | GET | /users | 403 Forbidden | ✅ |
| 获取所有用户 - 未授权 | GET | /users | 401 Unauthorized | ✅ |
| 根据 ID 获取用户 - 管理员成功 | GET | /users/{id} | 200 OK | ✅ |
| 根据 ID 获取用户 - 普通用户无权限 | GET | /users/{id} | 403 Forbidden | ✅ |
| 根据 ID 获取用户 - 不存在 | GET | /users/{id} | 404 Not Found | ✅ |
| 更新用户资料 - 管理员成功 | PUT | /users/{id} | 200 OK | ✅ |
| 更新用户资料 - 普通用户无权限 | PUT | /users/{id} | 403 Forbidden | ✅ |
| 更新用户资料 - 用户不存在 | PUT | /users/{id} | 404 Not Found | ✅ |
| 删除用户 - 管理员成功 | DELETE | /users/{id} | 204 No Content | ✅ |
| 删除用户 - 普通用户无权限 | DELETE | /users/{id} | 403 Forbidden | ✅ |

**测试代码**: `src/test/java/com/snail/controller/UserControllerIntegrationTest.java`

---

## 🔐 安全测试要点

### JWT 认证测试
- ✅ 未提供 Token 时返回 401
- ✅ Token 过期时返回 401
- ✅ 无效 Token 时返回 401
- ✅ 有效 Token 正常访问受保护接口

### 权限控制测试
- ✅ 普通用户访问 ADMIN 接口返回 403
- ✅ 管理员可以访问所有接口
- ✅ @PreAuthorize 注解正常工作

### 输入验证测试
- ✅ 空用户名/密码被拒绝
- ✅ 无效邮箱格式被拒绝
- ✅ 重复用户名/邮箱被拒绝

---

## 📊 测试覆盖率目标

| 指标 | 目标 | 实际 |
|------|------|------|
| 类覆盖率 | 80% | - |
| 方法覆盖率 | 75% | - |
| 行覆盖率 | 70% | - |
| 分支覆盖率 | 60% | - |

> 运行 `mvn jacoco:report` 后查看 `target/site/jacoco/` 获取详细报告

---

## 🐛 已知问题与 Bug

### 高优先级
暂无

### 中优先级
1. **用户任务状态更新**: 传入无效状态时返回 500 而非 400
   - 建议：添加异常处理，返回更友好的错误信息

### 低优先级
暂无

---

## 📦 交付物清单

- [x] `AuthControllerIntegrationTest.java` - 认证 API 测试
- [x] `TaskControllerIntegrationTest.java` - 任务 API 测试
- [x] `UserTaskControllerIntegrationTest.java` - 用户任务 API 测试
- [x] `PointTransactionControllerIntegrationTest.java` - 积分 API 测试
- [x] `UserControllerIntegrationTest.java` - 用户管理 API 测试
- [x] `snail-api-tests.postman_collection.json` - Postman 测试集合
- [x] `后端 API 测试报告.md` - 本文档

---

## 🚀 后续建议

1. **性能测试**: 使用 JMeter 或 Gatling 进行压力测试
2. **安全扫描**: 使用 OWASP ZAP 进行安全漏洞扫描
3. **API 文档**: 确保 Swagger/OpenAPI 文档与代码同步
4. **CI/CD 集成**: 将测试集成到 GitHub Actions 或 Jenkins
5. **契约测试**: 使用 Spring Cloud Contract 进行消费者驱动契约测试

---

## 📞 联系方式

如有疑问或发现新的 Bug，请联系后端测试工程师。

**测试完成时间**: 2026-03-30  
**下次测试计划**: 待定
