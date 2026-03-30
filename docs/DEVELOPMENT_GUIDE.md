# 🐌 Snail 开发环境搭建指南

> 版本：v1.0.0 | 更新日期：2026-03-30 | 适用：前端/后端/运维开发

---

## 📋 目录

1. [环境要求](#环境要求)
2. [快速开始](#快速开始)
3. [前端开发环境](#前端开发环境)
4. [后端开发环境](#后端开发环境)
5. [数据库环境](#数据库环境)
6. [Docker 开发环境](#docker 开发环境)
7. [IDE 配置](#ide 配置)
8. [常见问题](#常见问题)

---

## 环境要求

### 必需软件

| 软件 | 版本 | 用途 | 下载地址 |
|------|------|------|----------|
| Node.js | >= 18.0 | 前端开发 | https://nodejs.org/ |
| Java | 17 | 后端开发 | https://adoptium.net/ |
| Maven | >= 3.8 | 后端构建 | https://maven.apache.org/ |
| Git | >= 2.40 | 版本控制 | https://git-scm.com/ |
| Docker | >= 24.0 | 容器化 | https://docker.com/ |
| Docker Compose | >= 2.20 | 容器编排 | 随 Docker 安装 |

### 推荐工具

| 工具 | 用途 |
|------|------|
| IntelliJ IDEA | Java 开发 |
| VS Code | 前端开发 |
| MySQL Workbench | 数据库管理 |
| Redis Desktop Manager | Redis 管理 |
| Postman / Apifox | API 调试 |
| Navicat | 数据库管理 |

---

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/xiangyw/snail.git
cd snail
```

### 2. 一键安装依赖

```bash
# 执行安装脚本
chmod +x scripts/install-all.sh
./scripts/install-all.sh
```

### 3. 启动开发环境

```bash
# 启动数据库和中间件
docker-compose -f deploy/docker/docker-compose.dev.yml up -d

# 验证服务状态
docker-compose -f deploy/docker/docker-compose.dev.yml ps
```

### 4. 验证环境

```bash
# 前端访问：http://localhost:5173
# 后端访问：http://localhost:8080
# API 文档：http://localhost:8080/swagger-ui.html
```

---

## 前端开发环境

### 1. 安装 Node.js

**macOS:**
```bash
brew install node@18
```

**Windows:**
下载安装包：https://nodejs.org/dist/v18.x/node-v18.x.x-x64.msi

**Linux:**
```bash
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
```

### 2. 验证安装

```bash
node -v  # 应输出 v18.x.x
npm -v   # 应输出 9.x.x 或更高
```

### 3. 配置 npm 镜像 (可选，加速下载)

```bash
# 使用淘宝镜像
npm config set registry https://registry.npmmirror.com

# 验证配置
npm config get registry
```

### 4. 安装前端依赖

```bash
cd frontend

# 安装依赖
npm install

# 或使用 pnpm (推荐)
pnpm install
```

### 5. 启动开发服务器

```bash
# 开发模式 (热重载)
npm run dev

# 访问地址：http://localhost:5173
```

### 6. 构建生产版本

```bash
# 构建
npm run build

# 预览构建结果
npm run preview
```

### 7. 前端项目结构

```
frontend/
├── public/              # 静态资源
├── src/
│   ├── assets/         # 项目资源
│   ├── components/     # 公共组件
│   ├── composables/    # 组合式函数
│   ├── layouts/        # 布局组件
│   ├── router/         # 路由配置
│   ├── stores/         # Pinia 状态管理
│   ├── styles/         # 全局样式
│   ├── types/          # TypeScript 类型
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   └── main.ts         # 入口文件
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
└── .env                # 环境变量
```

### 8. 环境变量配置

创建 `.env` 文件：

```bash
# 开发环境
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_APP_TITLE=Snail
VITE_APP_VERSION=1.0.0
```

---

## 后端开发环境

### 1. 安装 JDK 17

**macOS:**
```bash
brew install openjdk@17
```

**Windows:**
下载：https://adoptium.net/temurin/releases/?version=17

**Linux:**
```bash
sudo apt-get install -y openjdk-17-jdk
```

### 2. 配置 JAVA_HOME

**macOS/Linux:**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH
```

**Windows:**
系统属性 → 高级 → 环境变量 → 新建系统变量：
```
变量名：JAVA_HOME
变量值：C:\Program Files\Eclipse Adoptium\jdk-17.x.x
```

### 3. 验证安装

```bash
java -version
# 应输出：openjdk version "17.x.x"

javac -version
# 应输出：javac 17.x.x
```

### 4. 安装 Maven

**macOS:**
```bash
brew install maven
```

**Windows:**
下载：https://maven.apache.org/download.cgi

**Linux:**
```bash
sudo apt-get install -y maven
```

### 5. 配置 Maven 镜像 (加速下载)

编辑 `~/.m2/settings.xml`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings>
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <name>Aliyun Maven</name>
      <url>https://maven.aliyun.com/repository/public</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

### 6. 安装后端依赖

```bash
cd backend

# 清理并安装依赖
mvn clean install -DskipTests

# 或只下载依赖
mvn dependency:resolve
```

### 7. 配置开发环境

创建 `backend/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/snail_dev?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: snail123456
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  redis:
    host: localhost
    port: 6379
    password: snail123456
    database: 0

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080

logging:
  level:
    com.snail: debug
    org.springframework.security: debug
```

### 8. 启动后端服务

```bash
cd backend

# 开发模式启动
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 或打包后运行
mvn clean package -DskipTests
java -jar target/snail-backend-0.1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

### 9. 后端项目结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/snail/
│   │   │   ├── SnailApplication.java    # 启动类
│   │   │   ├── config/                  # 配置类
│   │   │   ├── controller/              # 控制器
│   │   │   ├── service/                 # 服务层
│   │   │   │   ├── impl/               # 服务实现
│   │   │   ├── repository/              # 数据访问层
│   │   │   ├── entity/                  # 实体类
│   │   │   ├── dto/                     # 数据传输对象
│   │   │   ├── vo/                      # 视图对象
│   │   │   ├── common/                  # 公共类
│   │   │   │   ├── exception/          # 异常处理
│   │   │   │   ├── response/           # 响应封装
│   │   │   │   └── utils/              # 工具类
│   │   │   └── security/                # 安全相关
│   │   └── resources/
│   │       ├── application.yml          # 主配置
│   │       ├── application-dev.yml      # 开发环境
│   │       ├── application-test.yml     # 测试环境
│   │       ├── application-prod.yml     # 生产环境
│   │       └── mapper/                  # MyBatis 映射
│   └── test/                            # 测试代码
├── pom.xml
└── Dockerfile
```

---

## 数据库环境

### 1. 使用 Docker 启动 (推荐)

```bash
# 启动 MySQL 和 Redis
docker-compose -f deploy/docker/docker-compose.dev.yml up -d mysql redis

# 查看状态
docker-compose -f deploy/docker/docker-compose.dev.yml ps
```

### 2. MySQL 连接信息

```yaml
host: localhost
port: 3306
database: snail_dev
username: root
password: snail123456
```

### 3. 初始化数据库

```bash
# 进入 MySQL 容器
docker exec -it snail-mysql mysql -uroot -psnail123456

# 创建数据库
CREATE DATABASE IF NOT EXISTS snail_dev 
  DEFAULT CHARACTER SET utf8mb4 
  DEFAULT COLLATE utf8mb4_unicode_ci;

# 退出
exit;
```

### 4. Redis 连接信息

```yaml
host: localhost
port: 6379
password: snail123456
database: 0
```

### 5. 使用客户端工具连接

**MySQL Workbench:**
- Host: localhost
- Port: 3306
- Username: root
- Password: snail123456

**Redis Desktop Manager:**
- Host: localhost
- Port: 6379
- Auth: snail123456

---

## Docker 开发环境

### 1. 完整开发环境

```bash
# 启动所有服务 (MySQL, Redis, Nginx)
docker-compose -f deploy/docker/docker-compose.dev.yml up -d

# 查看日志
docker-compose -f deploy/docker/docker-compose.dev.yml logs -f

# 停止所有服务
docker-compose -f deploy/docker/docker-compose.dev.yml down

# 重启服务
docker-compose -f deploy/docker/docker-compose.dev.yml restart
```

### 2. 服务清单

| 服务 | 容器名 | 端口 | 说明 |
|------|--------|------|------|
| MySQL | snail-mysql | 3306 | 数据库 |
| Redis | snail-redis | 6379 | 缓存 |
| Nginx | snail-nginx | 80/443 | 反向代理 |
| Prometheus | snail-prometheus | 9090 | 监控 |
| Grafana | snail-grafana | 3000 | 可视化 |

### 3. 查看容器状态

```bash
# 查看所有容器
docker ps -a

# 查看容器日志
docker logs snail-mysql

# 进入容器
docker exec -it snail-mysql bash
```

### 4. 清理数据 (谨慎使用)

```bash
# 停止并删除所有容器和数据卷
docker-compose -f deploy/docker/docker-compose.dev.yml down -v

# 重新初始化
docker-compose -f deploy/docker/docker-compose.dev.yml up -d
```

---

## IDE 配置

### IntelliJ IDEA (后端)

1. **安装插件:**
   - Lombok
   - MyBatisX
   - Spring Boot Assistant
   - GitToolBox

2. **配置 JDK:**
   - File → Project Structure → SDKs → 添加 JDK 17

3. **配置 Maven:**
   - Settings → Build → Maven → 设置本地仓库和镜像

4. **开启 Lombok:**
   - Settings → Annotation Processors → Enable

5. **代码风格:**
   - 导入项目根目录的 `.editorconfig`

### VS Code (前端)

1. **安装插件:**
   ```
   - Volar (Vue 开发)
   - ESLint
   - Prettier
   - Vite
   - TypeScript
   - GitLens
   ```

2. **配置 settings.json:**
   ```json
   {
     "editor.formatOnSave": true,
     "editor.defaultFormatter": "esbenp.prettier-vscode",
     "editor.codeActionsOnSave": {
       "source.fixAll.eslint": true
     },
     "typescript.tsdk": "node_modules/typescript/lib"
   }
   ```

3. **配置调试:**
   创建 `.vscode/launch.json`:
   ```json
   {
     "version": "0.2.0",
     "configurations": [
       {
         "type": "chrome",
         "request": "launch",
         "name": "Debug Frontend",
         "url": "http://localhost:5173",
         "webRoot": "${workspaceFolder}/src"
       }
     ]
   }
   ```

---

## 常见问题

### 1. Node.js 版本不兼容

**问题:** `ERR_UNSUPPORTED_NODE_VERSION`

**解决:**
```bash
# 使用 nvm 管理 Node 版本
nvm install 18
nvm use 18
```

### 2. Maven 依赖下载失败

**问题:** `Could not resolve dependencies`

**解决:**
```bash
# 清理本地仓库
rm -rf ~/.m2/repository/com/snail

# 强制更新
mvn clean install -U -DskipTests
```

### 3. Docker 容器启动失败

**问题:** `port already in use`

**解决:**
```bash
# 查看占用端口的进程
lsof -i :3306

# 停止占用进程或修改端口
# 修改 docker-compose.dev.yml 中的端口映射
```

### 4. Java 版本错误

**问题:** `Unsupported class file major version`

**解决:**
```bash
# 确认 JAVA_HOME 配置正确
echo $JAVA_HOME

# 重新配置 JDK 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

### 5. 前端跨域问题

**问题:** `Access-Control-Allow-Origin`

**解决:**
```bash
# 前端配置代理 (vite.config.ts)
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

### 6. Git 凭证问题

**问题:** `Authentication failed`

**解决:**
```bash
# 清除缓存凭证
git credential-cache exit

# 重新配置
git config --global credential.helper store
```

### 7. 数据库连接失败

**问题:** `Communications link failure`

**解决:**
```bash
# 检查 Docker 容器状态
docker-compose -f deploy/docker/docker-compose.dev.yml ps

# 查看 MySQL 日志
docker logs snail-mysql

# 重启 MySQL
docker-compose -f deploy/docker/docker-compose.dev.yml restart mysql
```

---

## 开发检查清单

开发环境搭建完成后，请确认以下项目：

- [ ] Node.js >= 18 已安装
- [ ] Java 17 已安装并配置 JAVA_HOME
- [ ] Maven >= 3.8 已安装
- [ ] Git 已安装并配置用户信息
- [ ] Docker 已安装并启动
- [ ] MySQL 容器运行正常
- [ ] Redis 容器运行正常
- [ ] 前端依赖安装完成 (`npm install`)
- [ ] 后端依赖安装完成 (`mvn dependency:resolve`)
- [ ] 前端开发服务器可访问 (http://localhost:5173)
- [ ] 后端服务可访问 (http://localhost:8080)
- [ ] Swagger 文档可访问 (http://localhost:8080/swagger-ui.html)
- [ ] IDE 插件已安装并配置

---

*开发环境搭建完成！开始编码吧！🚀*

*最后更新：2026-03-30*
