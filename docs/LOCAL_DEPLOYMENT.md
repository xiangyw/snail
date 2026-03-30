# Snail 项目 - 本地部署指南

**适用场景**: 开发测试环境（不使用 Docker）

---

## 📦 环境要求

| 软件 | 版本 | 用途 |
|------|------|------|
| MySQL | 8.0+ | 数据库 |
| Redis | 7.0+ | 缓存 |
| Java | 17+ | 后端运行 |
| Node.js | 18+ | 前端运行 |
| Maven | 3.8+ | 后端构建 |

---

## 🔧 快速启动脚本

### 1. 启动数据库

```bash
# 如果已安装 MySQL
sudo systemctl start mysql

# 初始化数据库
mysql -u root -p < db/schema.sql
mysql -u root -p < db/data.sql
```

### 2. 启动 Redis

```bash
# 如果已安装 Redis
sudo systemctl start redis
```

### 3. 启动后端

```bash
cd backend
nohup mvn spring-boot:run -Dspring-boot.run.profiles=dev > /tmp/backend.log 2>&1 &

# 查看日志
tail -f /tmp/backend.log
```

### 4. 启动前端

```bash
cd frontend
nohup npm run dev -- --host 0.0.0.0 > /tmp/frontend.log 2>&1 &

# 查看日志
tail -f /tmp/frontend.log
```

---

## 🌐 访问地址

| 服务 | 地址 |
|------|------|
| 前端 H5 | http://localhost:5173 |
| 后端 API | http://localhost:8080 |
| Swagger | http://localhost:8080/api/swagger-ui.html |
| MySQL | localhost:3306 |
| Redis | localhost:6379 |

---

## 📝 配置文件

### 后端配置 (application-dev.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/snail
    username: root
    password: 你的密码
  redis:
    host: localhost
    port: 6379
```

### 前端配置 (vite.config.ts)

```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

---

## 🔍 常见问题

### MySQL 未安装

```bash
# Ubuntu/Debian
sudo apt-get install mysql-server

# CentOS/RHEL
sudo yum install mysql-server
```

### Redis 未安装

```bash
# Ubuntu/Debian
sudo apt-get install redis-server

# CentOS/RHEL
sudo yum install redis
```

### Java 未安装

```bash
# Ubuntu/Debian
sudo apt-get install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk
```

---

## 📊 服务管理

### 查看服务状态

```bash
# MySQL
sudo systemctl status mysql

# Redis
sudo systemctl status redis

# 后端
ps aux | grep java | grep snail

# 前端
ps aux | grep node | grep vite
```

### 停止服务

```bash
# 后端
pkill -f "snail-backend"

# 前端
pkill -f "vite"
```

---

*本地部署指南 - 适用于开发测试*
