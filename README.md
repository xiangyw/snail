# 🐌 Snail

> 综合性生活服务平台 | 直播·商城·社交

[![Status](https://img.shields.io/badge/status-development-yellow)](https://github.com/xiangyw/snail)
[![Vue](https://img.shields.io/badge/Vue-3.4-green)](https://vuejs.org/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-ready-blue)](https://docker.com/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

---

## 📖 项目简介

Snail (蜗牛) 是一个综合性生活服务平台，整合了**直播互动**、**电商购物**、**社交娱乐**等多种功能，为用户提供一站式数字化生活体验。

### 核心功能

- 🎥 **直播互动** - 实时直播、弹幕互动、礼物打赏、直播带货
- 🛒 **电商平台** - 商品浏览、购物车、订单管理、积分商城
- 💬 **社交系统** - 私信聊天、关注粉丝、动态分享
- 📱 **多端支持** - H5 App、官方网站、管理后台

### 平台组成

| 端 | 技术栈 | 说明 |
|----|--------|------|
| **H5 App** | Vue 3 + TypeScript + Vant | 移动端应用 |
| **管理后台** | Vue 3 + Element Plus | 平台运营管理 |
| **后端服务** | Spring Boot 3 + JPA | RESTful API |
| **数据库** | MySQL 8.0 + Redis 7 | 数据存储与缓存 |

---

## 🚀 快速开始

### 前置要求

| 工具 | 版本 | 用途 |
|------|------|------|
| Node.js | >= 18 | 前端开发 |
| Java | 17 | 后端开发 |
| Maven | >= 3.8 | 后端构建 |
| Docker | >= 24 | 容器化部署 |
| Git | >= 2.40 | 版本控制 |

### 1. 克隆项目

```bash
git clone https://github.com/xiangyw/snail.git
cd snail
```

### 2. 安装依赖

```bash
# 一键安装所有依赖
chmod +x scripts/install-all.sh
./scripts/install-all.sh
```

### 3. 启动开发环境

```bash
# 启动数据库和中间件
docker-compose -f deploy/docker/docker-compose.dev.yml up -d

# 启动前端 (新终端)
cd frontend
npm run dev

# 启动后端 (新终端)
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. 访问应用

| 服务 | 地址 | 说明 |
|------|------|------|
| 前端 | http://localhost:5173 | H5 App |
| 后端 API | http://localhost:8080 | REST API |
| Swagger 文档 | http://localhost:8080/swagger-ui.html | API 文档 |
| MySQL | localhost:3306 | 数据库 |
| Redis | localhost:6379 | 缓存 |

---

## 🛠️ 技术架构

### 系统架构图

```
┌─────────────────────────────────────────────────────────┐
│                      Client Layer                        │
├─────────────┬─────────────┬─────────────────────────────┤
│   H5 App    │ Admin Panel │     Official Website        │
│  (Vue 3)    │  (Vue 3)    │        (Vue 3)              │
└──────┬──────┴──────┬──────┴──────────────┬──────────────┘
       │             │                      │
       └─────────────┴──────────────────────┘
                         │
                    ┌────▼────┐
                    │  Nginx  │  (反向代理/负载均衡)
                    └────┬────┘
                         │
       ┌─────────────────┼─────────────────┐
       │                 │                 │
  ┌────▼────┐      ┌────▼────┐      ┌────▼────┐
  │  Front  │      │  Back   │      │  Admin  │
  │  Server │      │  Server │      │  Server │
  │  (H5)   │      │ (API)   │      │(Manage) │
  └─────────┘      └────┬────┘      └─────────┘
                        │
       ┌────────────────┼────────────────┐
       │                │                │
  ┌────▼────┐     ┌─────▼─────┐    ┌────▼────┐
  │  MySQL  │     │   Redis   │    │  MinIO  │
  │  8.0    │     │    7      │    │  (OSS)  │
  └─────────┘     └───────────┘    └─────────┘
```

### 技术栈详情

#### 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4 | 核心框架 |
| TypeScript | 5.3 | 类型系统 |
| Vite | 5.1 | 构建工具 |
| Vue Router | 4.3 | 路由管理 |
| Pinia | 2.1 | 状态管理 |
| Vant | 4.8 | UI 组件库 |
| Axios | 1.6 | HTTP 客户端 |
| WebSocket | - | 实时通信 |

#### 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.2 | 核心框架 |
| Spring Security | - | 安全认证 |
| Spring Data JPA | - | 数据访问 |
| MySQL | 8.0 | 关系数据库 |
| Redis | 7 | 缓存 |
| JWT | 0.12.5 | Token 认证 |
| Lombok | - | 代码简化 |
| Swagger | - | API 文档 |

#### 运维技术栈

| 技术 | 用途 |
|------|------|
| Docker | 容器化 |
| Docker Compose | 容器编排 |
| Nginx | 反向代理 |
| GitHub Actions | CI/CD |
| Prometheus | 监控 |
| Grafana | 可视化 |

---

## 📁 项目结构

```
snail/
├── frontend/                  # 前端项目 (H5 App)
│   ├── public/               # 静态资源
│   ├── src/
│   │   ├── api/              # API 接口
│   │   ├── assets/           # 资源文件
│   │   ├── components/       # 公共组件
│   │   ├── composables/      # 组合式函数
│   │   ├── layouts/          # 布局组件
│   │   ├── router/           # 路由配置
│   │   ├── stores/           # Pinia 状态管理
│   │   ├── styles/           # 全局样式
│   │   ├── types/            # TypeScript 类型
│   │   ├── utils/            # 工具函数
│   │   ├── views/            # 页面组件
│   │   ├── App.vue           # 根组件
│   │   └── main.ts           # 入口文件
│   ├── package.json
│   └── vite.config.ts
│
├── backend/                   # 后端项目 (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/snail/
│   │   │   │   ├── config/   # 配置类
│   │   │   │   ├── controller/ # 控制器
│   │   │   │   ├── service/  # 服务层
│   │   │   │   ├── repository/ # 数据访问
│   │   │   │   ├── entity/   # 实体类
│   │   │   │   ├── dto/      # 数据传输对象
│   │   │   │   ├── vo/       # 视图对象
│   │   │   │   ├── common/   # 公共类
│   │   │   │   └── security/ # 安全相关
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── application-dev.yml
│   │   └── test/             # 测试代码
│   └── pom.xml
│
├── deploy/                    # 部署配置
│   ├── docker/               # Docker 配置
│   │   ├── docker-compose.dev.yml
│   │   └── docker-compose.prod.yml
│   ├── github-actions/       # GitHub Actions
│   └── k8s/                  # Kubernetes 配置
│
├── docs/                      # 项目文档
│   ├── api/                  # API 文档
│   ├── requirements/         # 需求文档
│   ├── design/               # 设计文档
│   └── DEVELOPMENT_GUIDE.md  # 开发指南
│
├── scripts/                   # 脚本工具
│   ├── install-all.sh        # 安装依赖
│   └── deploy.sh             # 部署脚本
│
├── tests/                     # 测试文件
│   ├── e2e/                  # 端到端测试
│   └── performance/          # 性能测试
│
├── .github/                   # GitHub 配置
│   └── workflows/            # CI/CD 工作流
│
├── README.md                  # 项目说明
├── TEAM.md                    # 团队组织
└── DEPENDENCIES.md           # 依赖清单
```

---

## 📚 文档导航

### 开发文档

| 文档 | 说明 |
|------|------|
| [开发环境搭建指南](docs/DEVELOPMENT_GUIDE.md) | 环境配置、IDE 设置 |
| [代码规范文档](docs/CODE_STYLE_GUIDE.md) | 编码规范、最佳实践 |
| [Git 提交规范](docs/GIT_COMMIT_GUIDE.md) | 提交格式、示例 |
| [API 接口文档](docs/api/API_REFERENCE.md) | API 详细说明 |

### 需求文档

| 文档 | 说明 |
|------|------|
| [需求规格说明](docs/requirements/REQUIREMENTS.md) | 完整功能需求 |
| [MVP 范围](docs/requirements/MVP_SCOPE.md) | 最小可行产品 |
| [H5 规格说明](docs/requirements/H5_SPEC.md) | H5 App 详细设计 |

### 设计文档

| 文档 | 说明 |
|------|------|
| [系统架构](docs/design/ARCHITECTURE.md) | 技术架构设计 |
| [数据库设计](docs/design/DATABASE.md) | 表结构设计 |
| [接口设计](docs/design/API_DESIGN.md) | API 设计规范 |

### 运维文档

| 文档 | 说明 |
|------|------|
| [部署指南](deploy/docker/README.md) | 部署流程 |
| [运维手册](deploy/docker/运维手册.md) | 日常运维 |
| [监控配置](deploy/docker/grafana/README.md) | 监控告警 |

---

## 🔧 开发指南

### 前端开发

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 开发模式
npm run dev

# 构建生产版本
npm run build

# 代码检查
npm run lint

# 运行测试
npm run test
```

### 后端开发

```bash
# 进入后端目录
cd backend

# 安装依赖
mvn dependency:resolve

# 开发模式运行
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 打包
mvn clean package -DskipTests

# 运行测试
mvn test

# 代码检查
mvn checkstyle:check
```

### 数据库迁移

```bash
# 查看迁移状态
mvn flyway:info

# 执行迁移
mvn flyway:migrate

# 清理迁移
mvn flyway:clean
```

---

## 🚢 部署

### Docker 部署

```bash
# 开发环境
docker-compose -f deploy/docker/docker-compose.dev.yml up -d

# 生产环境
docker-compose -f deploy/docker/docker-compose.prod.yml up -d

# 查看状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

### 生产环境部署

```bash
# 1. 构建前端
cd frontend
npm run build

# 2. 构建后端
cd backend
mvn clean package -DskipTests

# 3. 构建 Docker 镜像
docker build -t snail-backend:latest ./backend
docker build -t snail-frontend:latest ./frontend

# 4. 启动服务
docker-compose -f deploy/docker/docker-compose.prod.yml up -d
```

### Kubernetes 部署

```bash
# 应用配置
kubectl apply -f deploy/k8s/namespace.yml
kubectl apply -f deploy/k8s/configmap.yml
kubectl apply -f deploy/k8s/deployment.yml
kubectl apply -f deploy/k8s/service.yml
kubectl apply -f deploy/k8s/ingress.yml
```

---

## 🧪 测试

### 单元测试

```bash
# 前端测试
cd frontend
npm run test:unit

# 后端测试
cd backend
mvn test
```

### 端到端测试

```bash
cd tests/e2e
npm install
npm run test
```

### 性能测试

```bash
cd tests/performance
jmeter -n -t snail_api_test.jmx -l results.jtl
```

---

## 👥 团队组织

详见 [团队组织文档](TEAM.md)

### 核心角色

| 角色 | 职责 |
|------|------|
| 项目经理 | 项目规划、进度管理 |
| 技术负责人 | 架构设计、技术决策 |
| 前端开发 | H5 App、管理后台 |
| 后端开发 | API 服务、数据库 |
| UI/UX 设计 | 界面设计、用户体验 |
| 测试工程师 | 质量保证、测试用例 |
| 运维工程师 | 部署、监控、维护 |

---

## 📋 开发检查清单

### 提交前检查

- [ ] 代码符合规范
- [ ] 通过单元测试
- [ ] 通过代码检查 (lint)
- [ ] 更新相关文档
- [ ] Commit 信息规范

### 发布前检查

- [ ] 所有测试通过
- [ ] 性能测试达标
- [ ] 安全扫描通过
- [ ] 文档已更新
- [ ] 版本号已更新

---

## 🤝 贡献指南

### 开发流程

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交变更 (`git commit -m 'feat: 添加 AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

### 分支管理

详见 [Git 分支管理规范](docs/GIT_WORKFLOW.md)

```
main (生产)
  ↑
test (测试)
  ↑
develop (开发)
  ↑
feature/* (功能分支)
```

---

## 📄 开源协议

MIT License - 详见 [LICENSE](LICENSE) 文件

---

## 📞 联系方式

- **项目地址**: https://github.com/xiangyw/snail
- **问题反馈**: https://github.com/xiangyw/snail/issues
- **团队邮箱**: team@snail.com

---

## 🙏 致谢

感谢所有为 Snail 项目做出贡献的开发者！

---

<div align="center">

**🐌 Snail - 慢一点，稳一点**

[↑ 返回顶部](#-snail)

</div>
