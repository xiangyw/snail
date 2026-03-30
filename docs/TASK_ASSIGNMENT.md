# 🐌 Snail 项目 - 模块开发任务分发清单

**分发时间**: 2026-03-30 10:40  
**版本**: v1.0  
**目标**: 今日完成全部 MVP 开发

---

## 📋 模块总览

| 模块 ID | 模块名称 | 负责人 | 类型 | 优先级 | 状态 |
|---------|----------|--------|------|--------|------|
| M01 | 用户认证模块 | subagent:56fa45ad | 后端 | P0 | 🟡 开发中 |
| M02 | 任务积分模块 | subagent:8259cc0f | 后端 | P0 | 🟡 开发中 |
| M03 | 任务管理前端 | subagent:94116f17 | 前端 | P0 | 🟡 开发中 |
| M04 | 商城用户前端 | subagent:4c53d80f | 前端 | P0 | 🟡 开发中 |

---

## 🔹 M01 - 用户认证模块 (后端)

**负责人**: subagent:56fa45ad  
**模型**: qwen3-coder-plus  
**优先级**: P0  
**预计完成**: 10:45

### 任务清单

| 任务 ID | 任务名称 | 文件路径 | 状态 |
|---------|----------|----------|------|
| M01-T01 | User 实体类 | backend/entity/User.java | 🟡 |
| M01-T02 | UserRepository | backend/repository/UserRepository.java | 🟡 |
| M01-T03 | UserService 接口 | backend/service/UserService.java | 🟡 |
| M01-T04 | UserServiceImpl | backend/service/impl/UserServiceImpl.java | 🟡 |
| M01-T05 | AuthController | backend/controller/AuthController.java | 🟡 |
| M01-T06 | RegisterRequest DTO | backend/dto/RegisterRequest.java | 🟡 |
| M01-T07 | LoginRequest DTO | backend/dto/LoginRequest.java | 🟡 |
| M01-T08 | AuthResponse DTO | backend/dto/AuthResponse.java | 🟡 |
| M01-T09 | JwtTokenProvider | backend/util/JwtTokenProvider.java | 🟡 |
| M01-T10 | SecurityConfig | backend/config/SecurityConfig.java | 🟡 |
| M01-T11 | 单元测试 | backend/src/test/... | 🟡 |

### API 接口

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/auth/register | 用户注册 | 公开 |
| POST | /api/auth/login | 用户登录 | 公开 |
| POST | /api/auth/logout | 用户登出 | 登录 |
| GET | /api/auth/me | 当前用户信息 | 登录 |
| PUT | /api/auth/profile | 更新个人资料 | 登录 |

### 交付标准
- [ ] 所有实体类创建完成
- [ ] 所有 API 接口可调用
- [ ] 单元测试通过率 100%
- [ ] Swagger 文档完整

---

## 🔹 M02 - 任务积分模块 (后端)

**负责人**: subagent:8259cc0f  
**模型**: qwen3-coder-plus  
**优先级**: P0  
**预计完成**: 10:45

### 任务清单

| 任务 ID | 任务名称 | 文件路径 | 状态 |
|---------|----------|----------|------|
| M02-T01 | Task 实体类 | backend/entity/Task.java | 🟡 |
| M02-T02 | UserTask 实体类 | backend/entity/UserTask.java | 🟡 |
| M02-T03 | Point 实体类 | backend/entity/Point.java | 🟡 |
| M02-T04 | PointTransaction 实体 | backend/entity/PointTransaction.java | 🟡 |
| M02-T05 | TaskRepository | backend/repository/TaskRepository.java | 🟡 |
| M02-T06 | UserTaskRepository | backend/repository/UserTaskRepository.java | 🟡 |
| M02-T07 | PointRepository | backend/repository/PointRepository.java | 🟡 |
| M02-T08 | TaskService | backend/service/TaskService.java | 🟡 |
| M02-T09 | UserTaskService | backend/service/UserTaskService.java | 🟡 |
| M02-T10 | PointService | backend/service/PointService.java | 🟡 |
| M02-T11 | TaskController | backend/controller/TaskController.java | 🟡 |
| M02-T12 | PointController | backend/controller/PointController.java | 🟡 |
| M02-T13 | DTO 类 | backend/dto/*.java | 🟡 |
| M02-T14 | 单元测试 | backend/src/test/... | 🟡 |

### API 接口

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /api/tasks | 任务列表 | 登录 |
| GET | /api/tasks/{id} | 任务详情 | 登录 |
| POST | /api/tasks | 创建任务 | 管理员 |
| POST | /api/tasks/{id}/claim | 领取任务 | 登录 |
| POST | /api/tasks/{id}/submit | 提交任务 | 登录 |
| GET | /api/points | 积分余额 | 登录 |
| GET | /api/points/transactions | 积分流水 | 登录 |

### 交付标准
- [ ] 所有实体类创建完成
- [ ] 所有 API 接口可调用
- [ ] 单元测试通过率 100%
- [ ] Swagger 文档完整

---

## 🔹 M03 - 任务管理前端

**负责人**: subagent:94116f17  
**模型**: qwen3-coder-plus  
**优先级**: P0  
**预计完成**: 10:50

### 任务清单

| 任务 ID | 任务名称 | 文件路径 | 状态 |
|---------|----------|----------|------|
| M03-T01 | TasksView.vue | frontend/src/views/Tasks/index.vue | 🟡 |
| M03-T02 | TaskDetailView.vue | frontend/src/views/Tasks/Detail.vue | 🟡 |
| M03-T03 | TaskSubmitView.vue | frontend/src/views/Tasks/Submit.vue | 🟡 |
| M03-T04 | TaskRecordView.vue | frontend/src/views/Tasks/Record.vue | 🟡 |
| M03-T05 | task Store | frontend/src/stores/task.js | 🟡 |
| M03-T06 | task API | frontend/src/api/task.js | 🟡 |
| M03-T07 | 路由配置 | frontend/src/router/index.ts | 🟡 |

### 页面功能

| 页面 | 功能要求 |
|------|----------|
| 任务列表 | 分类筛选、搜索、下拉刷新、上拉加载 |
| 任务详情 | 完整信息、奖励展示、开始任务按钮 |
| 任务提交 | 文本输入、图片上传 (9 张)、表单验证 |
| 任务记录 | 我的任务、状态筛选、奖励领取 |

### 交付标准
- [ ] 所有页面可正常访问
- [ ] 路由跳转正常
- [ ] API 调用正常
- [ ] 样式美观 (符合设计规范)

---

## 🔹 M04 - 商城用户前端

**负责人**: subagent:4c53d80f  
**模型**: qwen3-coder-plus  
**优先级**: P0  
**预计完成**: 10:50

### 任务清单

| 任务 ID | 任务名称 | 文件路径 | 状态 |
|---------|----------|----------|------|
| M04-T01 | MallHomeView.vue | frontend/src/views/Mall/Home.vue | 🟡 |
| M04-T02 | ProductDetailView.vue | frontend/src/views/Mall/ProductDetail.vue | 🟡 |
| M04-T03 | CartView.vue | frontend/src/views/Cart/index.vue | 🟡 |
| M04-T04 | OrderView.vue | frontend/src/views/Order/index.vue | 🟡 |
| M04-T05 | UserProfileView.vue | frontend/src/views/User/Profile.vue | 🟡 |
| M04-T06 | PointHistoryView.vue | frontend/src/views/User/PointHistory.vue | 🟡 |
| M04-T07 | user Store | frontend/src/stores/user.js | 🟡 |
| M04-T08 | mall Store | frontend/src/stores/mall.js | 🟡 |
| M04-T09 | user API | frontend/src/api/user.js | 🟡 |
| M04-T10 | mall API | frontend/src/api/mall.js | 🟡 |
| M04-T11 | 路由配置 | frontend/src/router/index.ts | 🟡 |

### 页面功能

| 页面 | 功能要求 |
|------|----------|
| 商城首页 | 分类导航、轮播广告、推荐商品 |
| 商品详情 | 图片轮播、详情展示、购买按钮 |
| 购物车 | 商品列表、数量增减、结算 |
| 订单页 | 订单列表、状态筛选、详情 |
| 个人中心 | 用户信息、积分余额、功能菜单 |
| 积分明细 | 流水列表、收入/支出筛选 |

### 交付标准
- [ ] 所有页面可正常访问
- [ ] 路由跳转正常
- [ ] API 调用正常
- [ ] 样式美观 (符合设计规范)

---

## 📞 协调说明

### 代码规范
- 所有代码遵循 `docs/CODE_STYLE_GUIDE.md`
- Git 提交遵循 `docs/GIT_COMMIT_GUIDE.md`
- API 设计遵循 `docs/api/API_REFERENCE.md`

### 依赖关系
```
M01 用户认证 → M03/M04 前端调用
M02 任务积分 → M03 前端调用
M03/M04 前端 → 依赖 M01/M02 后端 API
```

### 冲突避免
1. **后端**: 使用统一的 entity/service/controller 包结构
2. **前端**: 使用统一的 views/stores/api 目录结构
3. **路由**: 前端负责人统一合并路由配置

### 完成后的工作
1. 各团队自检代码
2. 提交到本地 Git
3. 项目经理统一合并
4. 运行测试验证
5. 推送到 GitHub

---

*任务分发完成，各团队开始执行*  
*下次更新：各团队完成后汇报*
