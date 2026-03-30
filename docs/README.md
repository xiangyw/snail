# 🐌 Snail 项目文档中心

> 完整的开发文档体系 | 版本：v1.0.0 | 更新日期：2026-03-30

---

## 📚 文档导航

### 🚀 快速入门

| 文档 | 说明 | 适合人群 |
|------|------|----------|
| [README](../README.md) | 项目介绍、快速开始 | 所有人 |
| [开发环境搭建指南](DEVELOPMENT_GUIDE.md) | 环境配置、IDE 设置 | 新入职开发 |
| [依赖清单](../DEPENDENCIES.md) | 第三方依赖说明 | 开发、运维 |

---

### 📖 需求文档

| 文档 | 说明 | 状态 |
|------|------|------|
| [需求规格说明](requirements/REQUIREMENTS.md) | 完整功能需求 | ✅ 已完成 |
| [MVP 范围](requirements/MVP_SCOPE.md) | 最小可行产品 | ✅ 已完成 |
| [H5 规格说明](requirements/H5_SPEC.md) | H5 App 详细设计 | ✅ 已完成 |
| [需求分析结果](requirements/analysis_result.json) | XMind 需求分析 | ✅ 已完成 |

---

### 🏗️ 设计文档

| 文档 | 说明 | 状态 |
|------|------|------|
| [系统架构设计](design/ARCHITECTURE.md) | 技术架构设计 | 🔄 待完善 |
| [数据库设计](design/DATABASE.md) | 表结构设计 | 🔄 待完善 |
| [接口设计规范](design/API_DESIGN.md) | API 设计规范 | 🔄 待完善 |

---

### 🔌 API 文档

| 文档 | 说明 | 状态 |
|------|------|------|
| [API 接口文档](api/API_REFERENCE.md) | 完整 API 说明 | ✅ 已完成 |
| [OpenAPI 规范](api/openapi.yaml) | Swagger 定义文件 | 🔄 待完善 |
| [Swagger UI](http://localhost:8080/swagger-ui.html) | 在线 API 文档 | - |

---

### 📝 开发规范

| 文档 | 说明 | 必读 |
|------|------|------|
| [代码规范文档](CODE_STYLE_GUIDE.md) | 编码规范、最佳实践 | ⭐ 必读 |
| [Git 分支管理](GIT_WORKFLOW.md) | 分支策略、合并规则 | ⭐ 必读 |
| [Git 提交规范](GIT_COMMIT_GUIDE.md) | Commit 格式、示例 | ⭐ 必读 |
| [GitHub 分支保护](GITHUB_BRANCH_PROTECTION.md) | 分支保护配置 | ⭐ 必读 |

---

### 🚢 部署运维

| 文档 | 说明 | 适合人群 |
|------|------|----------|
| [部署检查清单](DEPLOYMENT_CHECKLIST.md) | 部署流程、验证清单 | 运维、开发 |
| [Docker 部署指南](../deploy/docker/README.md) | Docker 部署说明 | 运维 |
| [运维手册](../deploy/docker/运维手册.md) | 日常运维操作 | 运维 |
| [监控配置](../deploy/docker/grafana/README.md) | Prometheus+Grafana | 运维 |

---

### 👥 团队管理

| 文档 | 说明 |
|------|------|
| [团队组织](../TEAM.md) | 团队架构、角色职责 |
| [项目状态](STATUS.md) | 项目进度、里程碑 |

---

## 🎯 按角色查看文档

### 新入职开发

```
1. README.md - 了解项目
2. DEVELOPMENT_GUIDE.md - 搭建环境
3. CODE_STYLE_GUIDE.md - 学习规范
4. GIT_WORKFLOW.md - 了解分支管理
5. GIT_COMMIT_GUIDE.md - 学习提交规范
6. API_REFERENCE.md - 熟悉接口
```

### 前端开发

```
1. DEVELOPMENT_GUIDE.md#前端开发环境
2. CODE_STYLE_GUIDE.md#前端代码规范
3. API_REFERENCE.md - 接口文档
4. requirements/H5_SPEC.md - 需求规格
```

### 后端开发

```
1. DEVELOPMENT_GUIDE.md#后端开发环境
2. CODE_STYLE_GUIDE.md#后端代码规范
3. API_REFERENCE.md - 接口文档
4. design/DATABASE.md - 数据库设计
5. requirements/REQUIREMENTS.md - 业务需求
```

### 测试工程师

```
1. requirements/REQUIREMENTS.md - 功能需求
2. API_REFERENCE.md - 接口文档
3. DEPLOYMENT_CHECKLIST.md - 部署验证
```

### 运维工程师

```
1. DEPLOYMENT_CHECKLIST.md - 部署流程
2. deploy/docker/README.md - Docker 部署
3. deploy/docker/运维手册.md - 日常运维
4. deploy/docker/grafana/README.md - 监控配置
```

### 产品经理

```
1. requirements/REQUIREMENTS.md - 完整需求
2. requirements/MVP_SCOPE.md - MVP 范围
3. STATUS.md - 项目进度
```

---

## 📊 文档状态

### ✅ 已完成

- [x] README.md - 项目说明
- [x] DEVELOPMENT_GUIDE.md - 开发环境搭建
- [x] CODE_STYLE_GUIDE.md - 代码规范
- [x] GIT_COMMIT_GUIDE.md - Git 提交规范
- [x] GIT_WORKFLOW.md - Git 分支管理
- [x] API_REFERENCE.md - API 接口文档
- [x] DEPLOYMENT_CHECKLIST.md - 部署检查清单
- [x] REQUIREMENTS.md - 需求规格说明
- [x] MVP_SCOPE.md - MVP 范围
- [x] H5_SPEC.md - H5 规格说明

### 🔄 待完善

- [ ] design/ARCHITECTURE.md - 系统架构设计
- [ ] design/DATABASE.md - 数据库设计
- [ ] design/API_DESIGN.md - 接口设计规范
- [ ] api/openapi.yaml - OpenAPI 规范文件
- [ ] tests/README.md - 测试指南

### 📅 计划中

- [ ] 性能优化指南
- [ ] 安全加固手册
- [ ] 故障排查手册
- [ ] 最佳实践案例

---

## 🔧 文档维护

### 文档规范

- 使用 Markdown 格式
- 标题层级清晰 (H1 → H2 → H3)
- 代码块标注语言类型
- 表格对齐美观
- 添加适当的 Emoji 增强可读性

### 更新流程

1. 修改文档
2. 提交 Commit (`docs(scope): 更新 XXX 文档`)
3. 代码审查
4. 合并到 develop 分支

### 文档评审

- 新文档创建后需经过评审
- 评审人：技术负责人 + 相关开发
- 评审要点：准确性、完整性、可读性

---

## 📞 问题反馈

发现文档问题？

- 提交 Issue: https://github.com/xiangyw/snail/issues
- 联系文档维护者

---

<div align="center">

**🐌 Snail 项目文档中心**

[↑ 返回顶部](#-snail-项目文档中心)

*Last updated: 2026-03-30*

</div>
