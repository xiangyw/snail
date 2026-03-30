# 🐌 Snail - Git 分支管理规范

**创建日期**: 2026-03-30  
**最后更新**: 2026-03-30

---

## 🌿 分支策略

```
main (生产分支，受保护)
  ↑
test (测试分支，受保护)
  ↑
develop (开发集成分支)
  ↑
feature/* (功能分支，开发人员自建)
  ↑
bugfix/* (修复分支)
```

---

## 📋 分支说明

| 分支 | 保护级别 | 用途 | 合并规则 |
|------|----------|------|----------|
| `main` | 🔒 严格保护 | 生产环境代码 | 仅从 test 合并，需 PR + 审查 |
| `test` | 🔒 严格保护 | 测试验证 | 仅从 develop 合并，需 PR + 审查 |
| `develop` | ⚠️ 限制推送 | 开发集成 | 仅从 feature/* 合并 |
| `feature/*` | 🔓 自由创建 | 功能开发 | 开发人员自建，PR 到 develop |
| `bugfix/*` | 🔓 自由创建 | Bug 修复 | 开发人员自建，PR 到 develop |
| `hotfix/*` | 🔓 紧急修复 | 生产问题 | PR 直接到 main (紧急流程) |

---

## 🔄 开发流程

### 标准功能开发流程

```
1. 从 develop 创建功能分支
   git checkout develop
   git checkout -b feature/user-login

2. 开发功能并提交
   git add .
   git commit -m "feat: 实现用户登录功能"

3. 推送到远程
   git push -u origin feature/user-login

4. 提交 PR 到 develop
   - GitHub: Pull Requests → New Pull Request
   - 标题：feat: 用户登录功能
   - 描述：功能说明、测试截图、关联 Issue

5. 代码审查 (至少 1 人批准)
   - 修复审查意见
   - 通过 CI 检查

6. 合并到 develop
   - Squash Merge 或 Create Merge Commit
   - 删除功能分支

7. 阶段完成后，PR 从 develop → test
   - 测试团队验证
   - 通过 QA 测试

8. 测试通过后，PR 从 test → main
   - 项目负责人审批
   - 打版本标签
   - 发布生产
```

---

## 🚫 分支保护规则

### main 分支
- ❌ 禁止直接 push
- ✅ 必须通过 PR 合并
- ✅ 至少 1 人审查批准
- ✅ CI 检查必须通过
- ✅ 只能从 test 分支合并

### test 分支
- ❌ 禁止直接 push
- ✅ 必须通过 PR 合并
- ✅ 至少 1 人审查批准
- ✅ CI 检查必须通过
- ✅ 只能从 develop 分支合并

### develop 分支
- ⚠️ 限制直接 push (仅项目负责人)
- ✅ 功能开发通过 PR 合并

---

## 📝 Commit 规范

采用 [Conventional Commits](https://www.conventionalcommits.org/)

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

### Type 类型

| 类型 | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | feat(auth): 添加用户登录 |
| `fix` | Bug 修复 | fix(api): 修复空指针异常 |
| `docs` | 文档更新 | docs(readme): 更新部署说明 |
| `style` | 代码格式 | style(format): 格式化代码 |
| `refactor` | 重构 | refactor(user): 重构用户服务 |
| `test` | 测试相关 | test(auth): 添加登录测试 |
| `chore` | 构建/工具 | chore(deps): 更新依赖版本 |

### 示例

```bash
feat(auth): 实现 JWT 认证

- 添加 JWT Token 生成
- 实现 Token 验证过滤器
- 添加登录/注册 API

Closes #123
```

---

## 🏷️ 版本标签

```bash
# 格式：v<主版本>.<次版本>.<修订版本>
git tag -a v1.0.0 -m "发布版本 1.0.0"
git push origin v1.0.0
```

---

## 🚨 紧急修复流程 (Hotfix)

```
1. 从 main 创建 hotfix 分支
   git checkout main
   git checkout -b hotfix/critical-bug

2. 修复问题并测试

3. PR 直接到 main (跳过 test)
   - 标注 [HOTFIX]
   - 紧急审批流程
   - 至少 1 人批准

4. 合并后同步到 develop
   git checkout develop
   git merge hotfix/critical-bug
```

---

## 🛠️ GitHub 设置 (需手动配置)

### 分支保护规则

1. **main 分支**
   - Settings → Branches → Add rule
   - Branch name pattern: `main`
   - ✅ Require a pull request before merging
   - ✅ Require approvals: 1
   - ✅ Require status checks to pass
   - ✅ Include administrators
   - ✅ Force pushes: ❌
   - ✅ Deletions: ❌

2. **test 分支**
   - 同上配置

3. **develop 分支**
   - ✅ Require a pull request before merging
   - ✅ Require status checks to pass

---

## 📊 流程图

```
feature/* ──PR──→ develop ──PR──→ test ──PR──→ main
     │              │             │            │
     │              │             │            └──→ 生产部署
     │              │             └──→ QA 测试
     │              └──→ 持续集成
     └──→ 功能开发
```

---

*所有团队成员必须遵守此规范*
