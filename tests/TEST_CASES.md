# 🐌 Snail 项目测试用例清单

> 版本：1.0 | 更新日期：2026-03-30 | 测试工程师：AI Agent

---

## 📋 目录

1. [单元测试](#1-单元测试)
2. [集成测试](#2-集成测试)
3. [API 测试](#3-api-测试)
4. [测试覆盖率目标](#4-测试覆盖率目标)

---

## 1. 单元测试

### 1.1 服务层测试

#### AuthService Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| AUTH-SVC-001 | register_Success | 正常注册新用户 | 返回包含 token 的 AuthResponse | ✅ |
| AUTH-SVC-002 | register_UsernameExists_ThrowsException | 用户名已存在 | 抛出 AuthenticationException | ✅ |
| AUTH-SVC-003 | register_EmailExists_ThrowsException | 邮箱已存在 | 抛出 AuthenticationException | ✅ |
| AUTH-SVC-004 | register_ShouldEncodePassword | 密码加密 | 保存的密码是加密后的 | ✅ |
| AUTH-SVC-005 | login_Success | 正常登录 | 返回包含 token 的 AuthResponse | ✅ |
| AUTH-SVC-006 | login_InvalidCredentials_ThrowsException | 密码错误 | 抛出 AuthenticationException | ✅ |
| AUTH-SVC-007 | login_UserNotFound_ThrowsException | 用户不存在 | 抛出 ResourceNotFoundException | ✅ |
| AUTH-SVC-008 | register_NullNickname_Success | 昵称为空注册 | 注册成功，nickname 为 null | ✅ |
| AUTH-SVC-009 | login_TokenGeneration_WithCorrectUserDetails | Token 生成 | Token 包含正确的用户信息 | ✅ |

#### UserDetailsServiceImpl Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| UDS-SVC-001 | loadUserByUsername_Success | 正常加载用户 | 返回 CustomUserDetails | ✅ |
| UDS-SVC-002 | loadUserByUsername_UserNotFound_ThrowsException | 用户不存在 | 抛出 UsernameNotFoundException | ✅ |
| UDS-SVC-003 | loadUserByUsername_AdminUser | 管理员用户 | 返回带 ROLE_ADMIN 权限的用户 | ✅ |
| UDS-SVC-004 | loadUserByUsername_DisabledUser | 禁用用户 | 返回 enabled=false 的用户 | ✅ |
| UDS-SVC-005 | loadUserByUsername_SuperAdminUser | 超级管理员 | 返回带 ROLE_SUPER_ADMIN 权限的用户 | ✅ |
| UDS-SVC-006 | loadUserByUsername_NullUsername | 用户名为 null | 抛出 UsernameNotFoundException | ✅ |
| UDS-SVC-007 | loadUserByUsername_EmptyUsername | 用户名为空字符串 | 抛出 UsernameNotFoundException | ✅ |
| UDS-SVC-008 | loadUserByUsername_SpecialCharacters | 用户名含特殊字符 | 正常加载用户 | ✅ |

#### TaskService Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| TASK-SVC-001 | getActiveTasks_Success | 获取活跃任务 | 返回活跃任务列表 | ✅ |
| TASK-SVC-002 | getActiveTasks_Empty | 无活跃任务 | 返回空列表 | ✅ |
| TASK-SVC-003 | getTaskById_Found | 根据 ID 获取任务 | 返回任务详情 | ✅ |
| TASK-SVC-004 | getTaskById_NotFound | 任务不存在 | 抛出 ResourceNotFoundException | ✅ |
| TASK-SVC-005 | startTask_NewUserTask | 用户开始新任务 | 创建 PENDING 状态的用户任务 | ✅ |
| TASK-SVC-006 | startTask_ExistingUserTask | 用户重新开始任务 | 更新状态为 PENDING | ✅ |
| TASK-SVC-007 | startTask_MaxCompletionsReached | 达到最大完成次数 | 抛出 IllegalStateException | ✅ |
| TASK-SVC-008 | completeTask_Success | 完成任务 | 状态变为 COMPLETED，记录积分 | ✅ |
| TASK-SVC-009 | completeTask_UserTaskNotFound | 用户任务不存在 | 抛出 ResourceNotFoundException | ✅ |
| TASK-SVC-010 | completeTask_NotPendingStatus | 任务非待完成状态 | 抛出 IllegalStateException | ✅ |
| TASK-SVC-011 | createTask_Success | 创建新任务 | 返回创建的任务 | ✅ |
| TASK-SVC-012 | updateTaskStatus_Success | 更新任务状态 | 任务状态已更新 | ✅ |
| TASK-SVC-013 | expireOldTasks_Success | 过期旧任务 | 返回过期任务数量 | ✅ |
| TASK-SVC-014 | expireOldTasks_None | 无过期任务 | 返回 0 | ✅ |

#### PointsService Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| PTS-SVC-001 | getOrCreateAccount_Exists | 账户已存在 | 返回现有账户 | ✅ |
| PTS-SVC-002 | getOrCreateAccount_NotExists | 账户不存在 | 创建并返回新账户 | ✅ |
| PTS-SVC-003 | getBalance_Success | 获取余额 | 返回正确余额 | ✅ |
| PTS-SVC-004 | getBalance_NewUser | 新用户余额 | 返回 0 | ✅ |
| PTS-SVC-005 | addPoints_Success | 增加积分 | 余额增加，创建交易记录 | ✅ |
| PTS-SVC-006 | addPoints_NewUser | 新用户增加积分 | 创建账户并增加积分 | ✅ |
| PTS-SVC-007 | addPoints_NegativePoints | 负数积分 | 抛出 IllegalArgumentException | ✅ |
| PTS-SVC-008 | addPoints_ZeroPoints | 零积分 | 抛出 IllegalArgumentException | ✅ |
| PTS-SVC-009 | spendPoints_Success | 消费积分 | 余额减少，创建交易记录 | ✅ |
| PTS-SVC-010 | spendPoints_InsufficientBalance | 余额不足 | 抛出 IllegalStateException | ✅ |
| PTS-SVC-011 | spendPoints_AccountNotFound | 账户不存在 | 抛出 ResourceNotFoundException | ✅ |
| PTS-SVC-012 | spendPoints_NegativePoints | 负数消费 | 抛出 IllegalArgumentException | ✅ |
| PTS-SVC-013 | getTransactionHistory_Success | 获取交易历史 | 返回交易列表 | ✅ |
| PTS-SVC-014 | getTransactionHistory_Empty | 无交易记录 | 返回空列表 | ✅ |
| PTS-SVC-015 | adjustPoints_Positive | 正向调整积分 | 余额增加 | ✅ |
| PTS-SVC-016 | adjustPoints_Negative | 负向调整积分 | 余额减少 | ✅ |
| PTS-SVC-017 | adjustPoints_InsufficientBalance | 调整超出余额 | 抛出 IllegalStateException | ✅ |
| PTS-SVC-018 | getTransactionsByType_Success | 按类型获取交易 | 返回过滤后的交易列表 | ✅ |
| PTS-SVC-019 | getTransactionsInDateRange_Success | 按日期范围获取交易 | 返回指定范围内的交易 | ✅ |

### 1.2 控制器测试

#### AuthController Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| AUTH-CTL-001 | register_Success | 正常注册 | 返回 200 OK 和 token | ✅ |
| AUTH-CTL-002 | register_MissingUsername | 缺少用户名 | 返回 400 Bad Request | ✅ |
| AUTH-CTL-003 | register_InvalidEmail | 邮箱格式错误 | 返回 400 Bad Request | ✅ |
| AUTH-CTL-004 | register_ShortPassword | 密码过短 | 返回 400 Bad Request | ✅ |
| AUTH-CTL-005 | register_UsernameExists | 用户名已存在 | 返回 401 Unauthorized | ✅ |
| AUTH-CTL-006 | login_Success | 正常登录 | 返回 200 OK 和 token | ✅ |
| AUTH-CTL-007 | login_MissingUsername | 缺少用户名 | 返回 400 Bad Request | ✅ |
| AUTH-CTL-008 | login_InvalidCredentials | 凭证无效 | 返回 401 Unauthorized | ✅ |
| AUTH-CTL-009 | login_MissingPassword | 缺少密码 | 返回 400 Bad Request | ✅ |
| AUTH-CTL-010 | validate_UsernameLength | 用户名长度验证 | 返回 400 Bad Request | ✅ |
| AUTH-CTL-011 | validate_ValidRequest | 有效请求 | 返回 200 OK | ✅ |

#### HealthController Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| HEALTH-CTL-001 | health_Success | 健康检查 | 返回 200 OK 和 UP 状态 | ✅ |
| HEALTH-CTL-002 | health_TimestampIsLong | 时间戳类型 | 时间戳为数字类型 | ✅ |
| HEALTH-CTL-003 | health_NoAuthRequired | 无需认证 | 无需认证即可访问 | ✅ |

#### TaskController Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| TASK-CTL-001 | getActiveTasks_Success | 获取活跃任务 | 返回任务列表 | ✅ |
| TASK-CTL-002 | getActiveTasks_Empty | 无任务 | 返回空数组 | ✅ |
| TASK-CTL-003 | getTaskById_Success | 获取任务详情 | 返回任务对象 | ✅ |
| TASK-CTL-004 | getTasksByType_Success | 按类型获取任务 | 返回过滤后的任务列表 | ✅ |
| TASK-CTL-005 | getUserTasks_Success | 获取用户任务 | 返回用户任务列表 | ✅ |
| TASK-CTL-006 | startTask_Success | 开始任务 | 返回 PENDING 状态的用户任务 | ✅ |
| TASK-CTL-007 | completeTask_Success | 完成任务 | 返回 COMPLETED 状态的用户任务 | ✅ |
| TASK-CTL-008 | completeTask_WithoutProof | 无证明完成任务 | 完成任务 | ✅ |
| TASK-CTL-009 | createTask_Success | 创建任务 | 返回创建的任务 | ✅ |

#### PointsController Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| PTS-CTL-001 | getBalance_Success | 获取余额 | 返回用户余额 | ✅ |
| PTS-CTL-002 | getAccount_Success | 获取账户详情 | 返回账户对象 | ✅ |
| PTS-CTL-003 | getTransactions_Success | 获取交易历史 | 返回交易列表 | ✅ |
| PTS-CTL-004 | getTransactions_Empty | 无交易记录 | 返回空数组 | ✅ |
| PTS-CTL-005 | addPoints_Success | 增加积分 | 返回更新后的账户 | ✅ |
| PTS-CTL-006 | spendPoints_Success | 消费积分 | 返回更新后的账户 | ✅ |
| PTS-CTL-007 | adjustPoints_Success | 调整积分 | 返回更新后的账户 | ✅ |
| PTS-CTL-008 | adjustPoints_Negative | 扣减积分 | 返回更新后的账户 | ✅ |

### 1.3 实体测试

#### User Entity Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| USER-ENT-001 | builder_AllFields | 构建完整用户 | 所有字段正确设置 | ✅ |
| USER-ENT-002 | builder_DefaultRole | 默认角色 | 角色为 USER | ✅ |
| USER-ENT-003 | builder_DefaultEnabled | 默认启用状态 | enabled 为 true | ✅ |
| USER-ENT-004 | builder_AdminUser | 构建管理员 | 角色为 ADMIN | ✅ |
| USER-ENT-005 | builder_NullNickname | 昵称为空 | nickname 为 null | ✅ |
| USER-ENT-006 | roles_Exist | 角色枚举存在 | 三个角色都存在 | ✅ |
| USER-ENT-007 | roles_Count | 角色数量 | 共 3 个角色 | ✅ |
| USER-ENT-008 | roles_ValueOf | 字符串转角色 | 正确转换 | ✅ |
| USER-ENT-009 | equals_SameFields | 相等性测试 | 字段相同则相等 | ✅ |
| USER-ENT-010 | equals_DifferentId | ID 不同 | 不相等 | ✅ |
| USER-ENT-011 | toString_NotNull | 字符串表示 | 返回非空字符串 | ✅ |

### 1.4 DTO 测试

#### AuthRequest Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| AUTH-DTO-001 | username_Valid | 有效用户名 | 验证通过 | ✅ |
| AUTH-DTO-002 | username_Null | 用户名为 null | 验证失败 | ✅ |
| AUTH-DTO-003 | username_Empty | 用户名为空 | 验证失败 | ✅ |
| AUTH-DTO-004 | username_TooShort | 用户名过短 | 验证失败 | ✅ |
| AUTH-DTO-005 | username_TooLong | 用户名过长 | 验证失败 | ✅ |
| AUTH-DTO-006 | username_MinLength | 最小长度 | 验证通过 | ✅ |
| AUTH-DTO-007 | username_MaxLength | 最大长度 | 验证通过 | ✅ |
| AUTH-DTO-008 | password_Null | 密码为 null | 验证失败 | ✅ |
| AUTH-DTO-009 | password_Empty | 密码为空 | 验证失败 | ✅ |
| AUTH-DTO-010 | password_TooShort | 密码过短 | 验证失败 | ✅ |
| AUTH-DTO-011 | password_MinLength | 最小长度 | 验证通过 | ✅ |
| AUTH-DTO-012 | password_Long | 长密码 | 验证通过 | ✅ |
| AUTH-DTO-013 | builder_Success | Builder 创建 | 成功创建对象 | ✅ |
| AUTH-DTO-014 | noArgsConstructor | 无参构造 | 成功创建对象 | ✅ |
| AUTH-DTO-015 | equals_SameFields | 相等性 | 字段相同则相等 | ✅ |
| AUTH-DTO-016 | equals_DifferentUsername | 用户名不同 | 不相等 | ✅ |

#### RegisterRequest Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| REG-DTO-001 | username_Valid | 有效用户名 | 验证通过 | ✅ |
| REG-DTO-002 | username_Null | 用户名为 null | 验证失败 | ✅ |
| REG-DTO-003 | username_TooShort | 用户名过短 | 验证失败 | ✅ |
| REG-DTO-004 | email_Null | 邮箱为 null | 验证失败 | ✅ |
| REG-DTO-005 | email_InvalidFormat | 邮箱格式错误 | 验证失败 | ✅ |
| REG-DTO-006 | email_Empty | 邮箱为空 | 验证失败 | ✅ |
| REG-DTO-007 | email_Valid | 有效邮箱 | 验证通过 | ✅ |
| REG-DTO-008 | email_WithPlus | 邮箱含 + 号 | 验证通过 | ✅ |
| REG-DTO-009 | password_Null | 密码为 null | 验证失败 | ✅ |
| REG-DTO-010 | password_TooShort | 密码过短 | 验证失败 | ✅ |
| REG-DTO-011 | password_MinLength | 最小长度 | 验证通过 | ✅ |
| REG-DTO-012 | nickname_Null | 昵称为 null | 验证通过 | ✅ |
| REG-DTO-013 | nickname_Empty | 昵称为空 | 验证通过 | ✅ |
| REG-DTO-014 | nickname_Valid | 有效昵称 | 验证通过 | ✅ |
| REG-DTO-015 | allFields_Valid | 所有字段有效 | 验证通过 | ✅ |
| REG-DTO-016 | multipleFields_Invalid | 多字段无效 | 多个验证错误 | ✅ |
| REG-DTO-017 | builder_Success | Builder 创建 | 成功创建对象 | ✅ |
| REG-DTO-018 | builder_WithoutNickname | 无昵称创建 | 成功创建对象 | ✅ |

### 1.5 Repository 测试

#### UserRepository Tests

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| USER-REPO-001 | findByUsername_Found | 根据用户名查找 | 找到用户 | ✅ |
| USER-REPO-002 | findByUsername_NotFound | 用户名不存在 | 返回空 | ✅ |
| USER-REPO-003 | findByEmail_Found | 根据邮箱查找 | 找到用户 | ✅ |
| USER-REPO-004 | findByEmail_NotFound | 邮箱不存在 | 返回空 | ✅ |
| USER-REPO-005 | existsByUsername_Exists | 用户名存在检查 | 返回 true | ✅ |
| USER-REPO-006 | existsByUsername_NotExists | 用户名不存在检查 | 返回 false | ✅ |
| USER-REPO-007 | existsByEmail_Exists | 邮箱存在检查 | 返回 true | ✅ |
| USER-REPO-008 | existsByEmail_NotExists | 邮箱不存在检查 | 返回 false | ✅ |
| USER-REPO-009 | save_Success | 保存用户 | 成功保存 | ✅ |
| USER-REPO-010 | save_AutoGenerateId | 自动生成 ID | ID 自动生成 | ✅ |
| USER-REPO-011 | save_DefaultRole | 默认角色 | 角色为 USER | ✅ |
| USER-REPO-012 | save_DefaultEnabled | 默认启用 | enabled 为 true | ✅ |
| USER-REPO-013 | deleteById_Success | 根据 ID 删除 | 用户被删除 | ✅ |
| USER-REPO-014 | delete_Success | 删除实体 | 用户被删除 | ✅ |
| USER-REPO-015 | count_Success | 计数 | 返回正确数量 | ✅ |
| USER-REPO-016 | count_Empty | 空表计数 | 返回 0 | ✅ |

---

## 2. 集成测试

### 2.1 认证集成测试

| 测试 ID | 测试名称 | 测试场景 | 预期结果 | 状态 |
|--------|---------|---------|---------|------|
| AUTH-INT-001 | register_Success | 完整注册流程 | 用户保存到数据库，返回 token | ✅ |
| AUTH-INT-002 | register_DuplicateUsername | 重复用户名 | 返回 401，数据库无重复 | ✅ |
| AUTH-INT-003 | register_DuplicateEmail | 重复邮箱 | 返回 401，数据库无重复 | ✅ |
| AUTH-INT-004 | login_Success | 完整登录流程 | 返回有效 token | ✅ |
| AUTH-INT-005 | login_WrongPassword | 密码错误 | 返回 401 | ✅ |
| AUTH-INT-006 | login_UserNotFound | 用户不存在 | 返回 401 | ✅ |
| AUTH-INT-007 | registerThenLogin_Success | 注册后登录 | 两次请求都成功 | ✅ |

---

## 3. API 测试

### 3.1 认证 API

| 端点 | 方法 | 描述 | 测试状态 |
|------|------|------|---------|
| `/auth/register` | POST | 用户注册 | ✅ |
| `/auth/login` | POST | 用户登录 | ✅ |
| `/public/health` | GET | 健康检查 | ✅ |

### 3.2 任务 API

| 端点 | 方法 | 描述 | 测试状态 |
|------|------|------|---------|
| `/api/tasks` | GET | 获取活跃任务 | ✅ |
| `/api/tasks/{taskId}` | GET | 获取任务详情 | ✅ |
| `/api/tasks/type/{type}` | GET | 按类型获取任务 | ✅ |
| `/api/tasks/user/{userId}` | GET | 获取用户任务 | ✅ |
| `/api/tasks/{taskId}/start` | POST | 开始任务 | ✅ |
| `/api/tasks/{taskId}/complete` | POST | 完成任务 | ✅ |
| `/api/tasks/create` | POST | 创建任务 | ✅ |

### 3.3 积分 API

| 端点 | 方法 | 描述 | 测试状态 |
|------|------|------|---------|
| `/api/points/balance/{userId}` | GET | 获取余额 | ✅ |
| `/api/points/account/{userId}` | GET | 获取账户详情 | ✅ |
| `/api/points/transactions/{userId}` | GET | 获取交易历史 | ✅ |
| `/api/points/add` | POST | 增加积分 | ✅ |
| `/api/points/spend` | POST | 消费积分 | ✅ |
| `/api/points/adjust` | POST | 调整积分 | ✅ |

---

## 4. 测试覆盖率目标

### 4.1 覆盖率指标

| 指标 | 目标 | 当前 | 状态 |
|------|------|------|------|
| 行覆盖率 | ≥ 80% | - | 🔄 |
| 分支覆盖率 | ≥ 75% | - | 🔄 |
| 类覆盖率 | ≥ 90% | - | 🔄 |
| 方法覆盖率 | ≥ 85% | - | 🔄 |

### 4.2 待补充测试

- [ ] SecurityConfig 测试
- [ ] JwtUtil 测试
- [ ] JwtAuthenticationFilter 测试
- [ ] JwtAuthenticationProvider 测试
- [ ] GlobalExceptionHandler 测试
- [ ] Task 和 UserTask 实体测试
- [ ] PointsAccount 和 PointsTransaction 实体测试
- [ ] 所有 Repository 的完整测试
- [ ] 集成测试覆盖所有 API 端点
- [ ] 性能测试
- [ ] 安全测试

---

## 5. 测试执行命令

### 运行所有测试
```bash
cd backend
mvn test
```

### 运行特定测试类
```bash
mvn test -Dtest=AuthServiceTest
```

### 运行特定测试包
```bash
mvn test -Dtest="com.snail.service.*"
```

### 生成覆盖率报告
```bash
mvn clean test jacoco:report
```

### 运行集成测试
```bash
mvn test -Dtest="*IntegrationTest"
```

---

## 6. 测试数据

### 测试用户工厂

使用 `UserFactory` 创建测试数据：

```java
// 创建普通用户
User user = UserFactory.createUser();

// 创建管理员用户
User admin = UserFactory.createAdminUser();

// 创建禁用用户
User disabled = UserFactory.createDisabledUser();

// 创建注册请求
RegisterRequest request = UserFactory.createRegisterRequest();
```

---

*文档版本：1.0*  
*最后更新：2026-03-30*  
*维护者：Snail 测试团队*
