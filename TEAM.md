# 🐌 Snail - 研发团队组织

**项目管理者**: Project Commander (AI 技术合伙人)  
**创建日期**: 2026-03-30  
**仓库**: https://github.com/xiangyw/snail.git

---

## 👥 团队组成

| 角色 | 负责人 | 职责 | 状态 |
|------|--------|------|------|
| **项目管理** | Project Commander | 整体协调、进度跟踪、风险管理 | ✅ 已就位 |
| **UI/UX 设计** | [待招募] | 界面设计、交互原型、设计规范 | ⏳ 待组建 |
| **前端开发** | [待招募] | Vue 开发、组件库、状态管理 | ⏳ 待组建 |
| **后端开发** | [待招募] | Java 服务、API 设计、数据库 | ⏳ 待组建 |
| **测试工程师** | [待招募] | 单元测试、集成测试、质量保障 | ⏳ 待组建 |
| **运维工程师** | [待招募] | Docker 部署、CI/CD、监控 | ⏳ 待组建 |

---

## 🔄 Git 工作流 (2026-03-30 更新)

### 分支策略

```
main (生产分支，🔒 严格保护)
  ↑
test (测试分支，🔒 严格保护)
  ↑
develop (开发集成分支)
  ↑
feature/* (功能分支，开发人员自建)
```

### 开发流程

1. **开发人员创建功能分支**
   ```bash
   git checkout develop
   git checkout -b feature/user-login
   ```

2. **开发并提交代码**
   ```bash
   git add .
   git commit -m "feat: 实现用户登录"
   git push -u origin feature/user-login
   ```

3. **提交 PR 到 develop**
   - GitHub 创建 Pull Request
   - 至少 1 人审查批准
   - CI 检查通过

4. **合并到 develop**
   - 删除功能分支

5. **阶段完成后 PR: develop → test**
   - 测试团队验证

6. **测试通过后 PR: test → main**
   - 项目负责人审批
   - 打版本标签
   - 发布生产

### 保护规则

| 分支 | 直接 Push | PR 审查 | CI 检查 |
|------|-----------|---------|---------|
| `main` | ❌ 禁止 | ✅ 1 人 | ✅ 必须 |
| `test` | ❌ 禁止 | ✅ 1 人 | ✅ 必须 |
| `develop` | ⚠️ 限制 | ✅ 推荐 | ✅ 推荐 |
| `feature/*` | ✅ 自由 | - | - |

### 本地保护 (Git Hooks)

克隆项目后安装 hooks:
```bash
./scripts/install-hooks.sh
```

这将防止意外推送到 main/test 分支。

---

## 📋 团队职责详解

### 🎨 UI/UX 设计
- 产品界面视觉设计
- 用户交互流程设计
- 设计系统/组件库规范
- 切图与资源交付
- 设计稿评审

### 💻 前端开发 (Vue)
- Vue 3 + TypeScript 开发
- 组件库开发与维护
- 状态管理 (Pinia/Vuex)
- API 对接与数据渲染
- 性能优化与适配

### ☕ 后端开发 (Java)
- Spring Boot 服务开发
- RESTful API 设计与实现
- 数据库设计与优化
- 缓存策略 (Redis)
- 安全认证 (JWT/OAuth2)

### 🧪 测试工程师
- 单元测试 (JUnit/Vitest)
- 集成测试
- E2E 测试 (Playwright/Cypress)
- 测试用例编写
- Bug 追踪与回归测试

### 🚀 运维工程师
- Docker 容器化
- Docker Compose / K8s 编排
- CI/CD 流水线 (GitHub Actions)
- 服务监控与告警
- 日志管理

---

## 📁 项目结构

```
snail/
├── docs/                 # 项目文档
│   ├── requirements/     # 需求文档
│   ├── design/           # 设计文档
│   ├── api/              # API 文档
│   ├── GIT_WORKFLOW.md   # Git 工作流规范
│   └── STATUS.md         # 项目状态看板
├── design/               # 设计资源
├── frontend/             # 前端 (Vue)
├── backend/              # 后端 (Java)
├── tests/                # 测试
├── deploy/               # 部署配置
└── scripts/              # 工具脚本
    ├── install-hooks.sh  # Git hooks 安装
    └── git-hooks/        # Git hooks 脚本
```

---

## 🛠️ 技术栈

| 领域 | 技术选型 |
|------|----------|
| 前端 | Vue 3 + TypeScript + Vite + Pinia |
| UI 库 | Element Plus / Ant Design Vue |
| 后端 | Java 17 + Spring Boot 3 |
| 数据库 | MySQL 8 / PostgreSQL |
| 缓存 | Redis |
| 容器 | Docker + Docker Compose |
| CI/CD | GitHub Actions |

---

## 📞 沟通机制

- **每日站会**: 同步进度、阻塞问题
- **周会**: 里程碑回顾、计划调整
- **即时沟通**: 技术问题快速响应
- **文档沉淀**: 决策记录、技术方案

---

## 📚 相关文档

- [Git 工作流规范](./docs/GIT_WORKFLOW.md)
- [GitHub 分支保护配置](./docs/GITHUB_BRANCH_PROTECTION.md)
- [项目状态看板](./docs/STATUS.md)

---

*最后更新：2026-03-30*
