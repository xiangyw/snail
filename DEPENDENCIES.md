# 🐌 Snail 项目 - 第三方依赖清单

**更新日期**: 2026-03-30  
**用途**: 统一安装，避免开发阻塞

---

## 前端依赖 (H5 App)

### 核心依赖
```bash
cd frontend
npm install
```

### package.json 已包含
```json
{
  "dependencies": {
    "vue": "^3.4.0",
    "vue-router": "^4.3.0",
    "pinia": "^2.1.7",
    "axios": "^1.6.0",
    "vant": "^4.8.0",
    "weixin-js-sdk": "^1.6.0",
    "@vant/use": "^1.6.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.0",
    "typescript": "^5.3.0",
    "vite": "^5.1.0",
    "vue-tsc": "^2.0.0",
    "vite-plugin-pwa": "^0.19.0",
    "workbox-window": "^7.0.0",
    "postcss-px-to-viewport": "^1.1.1",
    "autoprefixer": "^10.4.0",
    "postcss": "^8.4.0"
  }
}
```

---

## 后端依赖 (Spring Boot)

### Maven 自动下载
```bash
cd backend
mvn dependency:resolve
```

### 核心依赖 (pom.xml)
- Spring Boot 3.2.4
- Spring Security
- Spring Data JPA
- MySQL Connector
- PostgreSQL Driver
- Redis
- Lombok
- JWT (jjwt)
- Swagger/OpenAPI

---

## 运维依赖

### Docker 镜像
```bash
# 开发环境
docker-compose -f deploy/docker/docker-compose.dev.yml up -d

# 生产环境
docker-compose -f deploy/docker/docker-compose.prod.yml up -d
```

### 需要拉取的镜像
- mysql:8.0
- redis:7-alpine
- nginx:alpine
- prom/prometheus
- grafana/grafana
- loki/loki

---

## 快速安装脚本

```bash
#!/bin/bash
# scripts/install-all.sh

set -e

echo "🔧 安装前端依赖..."
cd frontend
npm install

echo "☕ 安装后端依赖..."
cd ../backend
mvn dependency:resolve

echo "✅ 全部依赖安装完成"
```

---

## 环境要求

| 工具 | 版本 | 用途 |
|------|------|------|
| Node.js | >=18 | 前端开发 |
| Java | 17 | 后端开发 |
| Maven | 3.8+ | 后端构建 |
| Docker | 24+ | 容器化 |
| Git | 2.40+ | 版本控制 |

---

*开发前确保所有依赖安装完成*
