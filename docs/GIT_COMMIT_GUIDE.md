# 🐌 Snail Git 提交规范示例

> 基于 Conventional Commits | 版本：v1.0.0 | 更新日期：2026-03-30

---

## 📋 目录

1. [提交格式](#提交格式)
2. [Type 类型说明](#type 类型说明)
3. [Scope 范围说明](#scope 范围说明)
4. [完整示例](#完整示例)
5. [实际场景示例](#实际场景示例)
6. [工具配置](#工具配置)

---

## 提交格式

### 标准格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### 格式说明

| 部分 | 必填 | 说明 |
|------|------|------|
| `type` | ✅ | 提交类型 (feat, fix, docs, etc.) |
| `scope` | ⭕ | 影响范围 (模块名) |
| `subject` | ✅ | 简短描述 (不超过 50 字符) |
| `body` | ⭕ | 详细描述 (可选) |
| `footer` | ⭕ | 关联 Issue、BREAKING CHANGE |

### 一行提交 (简单变更)

```bash
git commit -m "feat(auth): 添加短信验证码登录"
```

### 多行提交 (复杂变更)

```bash
git commit
```

然后在编辑器中：

```
feat(auth): 实现 JWT 认证机制

- 添加 JWT Token 生成逻辑
- 实现 Token 验证过滤器
- 添加登录/注册 API 接口
- 配置 Token 过期时间 (2 小时)
- 添加刷新 Token 接口

Closes #123
```

---

## Type 类型说明

### 主要类型

| Type | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | feat(auth): 添加微信登录 |
| `fix` | Bug 修复 | fix(order): 修复订单金额计算错误 |
| `docs` | 文档更新 | docs(readme): 更新部署说明 |
| `style` | 代码格式 | style(format): 格式化代码 |
| `refactor` | 重构 | refactor(user): 重构用户服务 |
| `test` | 测试相关 | test(auth): 添加登录测试用例 |
| `chore` | 构建/工具 | chore(deps): 更新依赖版本 |

### 次要类型

| Type | 说明 | 示例 |
|------|------|------|
| `perf` | 性能优化 | perf(query): 优化用户查询性能 |
| `ci` | CI 配置 | ci(github): 添加 GitHub Actions |
| `build` | 构建系统 | build(webpack): 优化打包配置 |
| `revert` | 回滚提交 | revert: 回滚 "feat: 添加分享功能" |

---

## Scope 范围说明

### 前端 Scope

| Scope | 说明 | 示例 |
|-------|------|------|
| `auth` | 认证模块 | feat(auth): 添加短信登录 |
| `user` | 用户模块 | fix(user): 修复头像上传 |
| `live` | 直播模块 | feat(live): 添加礼物系统 |
| `shop` | 商城模块 | feat(shop): 添加购物车 |
| `order` | 订单模块 | fix(order): 修复订单状态 |
| `message` | 消息模块 | feat(message): 添加私信功能 |
| `ui` | UI 组件 | style(ui): 优化按钮样式 |
| `router` | 路由 | fix(router): 修复路由守卫 |
| `store` | 状态管理 | refactor(store): 重构用户 store |

### 后端 Scope

| Scope | 说明 | 示例 |
|-------|------|------|
| `auth` | 认证模块 | feat(auth): JWT 认证 |
| `user` | 用户服务 | fix(user): 用户查询优化 |
| `order` | 订单服务 | feat(order): 创建订单 |
| `payment` | 支付服务 | feat(payment): 微信支付 |
| `product` | 商品服务 | fix(product): 库存扣减 |
| `live` | 直播服务 | feat(live): 直播推流 |
| `config` | 配置 | chore(config): 环境配置 |
| `security` | 安全 | fix(security): XSS 防护 |

### 通用 Scope

| Scope | 说明 | 示例 |
|-------|------|------|
| `deps` | 依赖更新 | chore(deps): 更新 Vue 版本 |
| `ci` | CI/CD | ci(github): 添加工作流 |
| `docker` | Docker | chore(docker): 优化镜像 |
| `readme` | README | docs(readme): 更新文档 |
| `release` | 发布 | chore(release): v1.0.0 |

---

## 完整示例

### 新功能提交

```bash
feat(auth): 添加手机号一键登录

实现本机号码一键登录功能:
- 集成中国移动认证 SDK
- 添加授权页面
- 实现登录回调处理
- 添加失败降级方案 (短信验证码)

Closes #45
```

### Bug 修复提交

```bash
fix(order): 修复订单金额计算精度问题

问题：订单总金额在多次优惠叠加时出现精度丢失

原因：使用 double 类型进行金额计算

解决：
- 改用 BigDecimal 进行金额计算
- 添加金额工具类 (MoneyUtils)
- 更新所有金额相关字段

Fixes #78
```

### 重构提交

```bash
refactor(user): 重构用户信息服务

优化用户信息查询逻辑:
- 拆分 UserService 为多个小类
- 添加缓存层 (Redis)
- 实现异步加载
- 添加性能监控

性能提升：查询耗时从 200ms 降至 50ms
```

### 文档更新提交

```bash
docs(api): 更新订单 API 文档

- 补充订单创建接口参数说明
- 添加请求/响应示例
- 更新错误码说明
- 添加调用频率限制说明
```

### 样式修复提交

```bash
style(format): 格式化前端代码

执行 Prettier 格式化:
- 统一缩进 (2 空格)
- 统一引号 (单引号)
- 移除多余空行
- 排序 import 语句

无功能变更
```

### 测试提交

```bash
test(auth): 添加登录模块单元测试

测试用例:
- 手机号格式校验
- 验证码发送
- 验证码校验
- Token 生成
- Token 验证

覆盖率：85%
```

### 构建配置提交

```bash
chore(deps): 更新项目依赖

前端依赖:
- vue: 3.3.4 → 3.4.0
- pinia: 2.1.6 → 2.1.7
- vant: 4.7.0 → 4.8.0

后端依赖:
- spring-boot: 3.2.2 → 3.2.3
- jjwt: 0.12.3 → 0.12.5
```

### CI 配置提交

```bash
ci(github): 添加自动化测试工作流

- 添加单元测试 GitHub Actions
- 配置代码覆盖率检查
- 添加 PR 自动检查
- 配置测试失败通知
```

### Breaking Change 提交

```bash
feat(auth)!: 重构认证接口

变更:
- 移除 /auth/login/password 接口
- 统一使用 /auth/login 接口
- 修改请求参数格式
- 修改响应数据结构

BREAKING CHANGE: 
旧的密码登录接口已移除，请迁移到新的统一登录接口

迁移指南：
旧：POST /auth/login/password {password: "xxx"}
新：POST /auth/login {verifyCode: "123456"}

Refs: #100
```

---

## 实际场景示例

### 场景 1: 开发用户登录功能

```bash
# 1. 创建登录页面
git commit -m "feat(auth): 添加登录页面"

# 2. 实现登录 API 调用
git commit -m "feat(auth): 实现登录 API 调用"

# 3. 添加表单验证
git commit -m "feat(auth): 添加登录表单验证"

# 4. 修复手机号验证 bug
git commit -m "fix(auth): 修复手机号格式验证错误"

# 5. 添加登录 loading 状态
git commit -m "style(auth): 优化登录按钮交互"

# 6. 添加登录测试
git commit -m "test(auth): 添加登录功能测试用例"
```

### 场景 2: 开发购物车功能

```bash
# 1. 创建购物车数据结构
git commit -m "feat(shop): 添加购物车数据结构"

# 2. 实现添加到购物车
git commit -m "feat(shop): 实现添加到购物车功能"

# 3. 实现购物车列表展示
git commit -m "feat(shop): 实现购物车列表展示"

# 4. 实现数量修改
git commit -m "feat(shop): 实现购物车数量修改"

# 5. 实现删除商品
git commit -m "feat(shop): 实现删除购物车商品"

# 6. 性能优化
git commit -m "perf(shop): 优化购物车渲染性能"

# 7. 修复 bug
git commit -m "fix(shop): 修复购物车数量同步问题"
```

### 场景 3: 数据库优化

```bash
# 1. 添加索引
git commit -m "perf(db): 为 users 表添加 username 索引"

# 2. 优化查询
git commit -m "perf(user): 优化用户查询 SQL"

# 3. 添加缓存
git commit -m "perf(user): 添加用户信息 Redis 缓存"

# 4. 更新文档
git commit -m "docs(db): 更新数据库索引说明"
```

### 场景 4: 上线前准备

```bash
# 1. 更新版本号
git commit -m "chore(release): 准备发布 v1.0.0"

# 2. 更新 CHANGELOG
git commit -m "docs(changelog): 更新 v1.0.0 变更日志"

# 3. 更新 README
git commit -m "docs(readme): 更新 README 版本信息"

# 4. 更新部署文档
git commit -m "docs(deploy): 更新部署说明"
```

---

## 工具配置

### Commitlint 配置

安装：
```bash
npm install -D @commitlint/cli @commitlint/config-conventional
```

创建 `commitlint.config.js`:
```javascript
module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'type-enum': [
      2,
      'always',
      [
        'feat',
        'fix',
        'docs',
        'style',
        'refactor',
        'perf',
        'test',
        'chore',
        'ci',
        'build',
        'revert'
      ]
    ],
    'subject-full-stop': [2, 'never', '.'],
    'subject-case': [2, 'never', ['sentence-case', 'start-case', 'pascal-case', 'upper-case']],
    'header-max-length': [2, 'always', 100]
  }
}
```

### Commitizen 配置

安装：
```bash
npm install -D commitizen cz-conventional-changelog
```

配置 `package.json`:
```json
{
  "config": {
    "commitizen": {
      "path": "cz-conventional-changelog"
    }
  }
}
```

使用：
```bash
# 交互式提交
git cz

# 或配置别名
alias git-cz='git cz'
```

### Husky 配置

安装：
```bash
npm install -D husky
npx husky install
npx husky add .husky/commit-msg 'npx --no -- commitlint --edit $1'
```

### VS Code 插件

推荐安装：
- **GitLens** - Git 增强
- **Conventional Commits** - 提交规范提示

### 提交模板

创建 `.gitmessage`:
```bash
# <type>(<scope>): <subject>
# |<----  使用最多 50 个字符  ---->|

# 为什么做这个改动？
# |<----  使用最多 72 个字符每行  ---->|

# 关联 Issue
# Closes #123
```

配置 Git 使用模板:
```bash
git config commit.template .gitmessage
```

---

## 最佳实践

### ✅ 推荐做法

1. **原子提交** - 每个提交只做一件事
2. **及时提交** - 完成一个小功能就提交
3. **清晰描述** - 说明为什么做，而不只是做了什么
4. **使用现在时** - "添加功能" 而非 "添加了功能"
5. **首字母小写** - "添加功能" 而非 "添加功能"
6. **不使用句号** - 主题行末尾不加句号

### ❌ 避免做法

1. **提交太大** - 一次提交包含多个功能
2. **描述模糊** - "修复 bug", "更新代码"
3. **无意义提交** - "修改", "测试", "临时提交"
4. **提交注释** - "修复上一个提交的错误"
5. **格式混乱** - 不遵循 Conventional Commits

### 提交频率建议

```
✅ 好的提交历史:
feat(auth): 添加登录页面
feat(auth): 实现登录 API 调用
fix(auth): 修复手机号验证
test(auth): 添加登录测试
docs(auth): 更新登录文档

❌ 糟糕的提交历史:
更新代码
修改 bug
临时提交
修复上一个问题
终于好了
```

---

## 检查清单

提交前请确认：

- [ ] 遵循 Conventional Commits 格式
- [ ] Type 类型正确
- [ ] Scope 范围准确
- [ ] Subject 简洁清晰 (<=50 字符)
- [ ] 使用现在时态
- [ ] 首字母小写
- [ ] 无末尾句号
- [ ] Body 说明原因 (复杂变更时)
- [ ] 关联 Issue (如有)
- [ ] 代码已通过测试
- [ ] 代码已格式化

---

*好的提交历史是项目的最佳文档！*

*最后更新：2026-03-30*
