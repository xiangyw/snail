# Snail 项目后端 - 用户认证模块

## 概述

这是 Snail 项目的后端用户认证模块，提供了完整的用户注册、登录、身份验证和用户管理功能。

## 功能特性

- 用户注册/登录
- JWT 身份验证
- 用户资料管理
- 角色权限控制
- RESTful API
- Swagger 文档

## 技术栈

- Spring Boot 3.2+
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA
- MySQL
- Swagger/OpenAPI

## 项目结构

```
src/
├── main/
│   ├── java/com/snail/
│   │   ├── entity/           # 实体类
│   │   ├── repository/       # 数据访问层
│   │   ├── service/          # 业务逻辑层
│   │   ├── controller/       # 控制器层
│   │   ├── dto/             # 数据传输对象
│   │   └── security/        # 安全相关组件
│   └── resources/
│       └── application.properties
└── test/                    # 单元测试
```

## API 接口

### 认证接口 (/api/auth)

- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/me` - 获取当前用户信息
- `PUT /api/auth/profile` - 更新用户资料

### 用户管理接口 (/api/users) - 管理员专用

- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 获取指定用户
- `PUT /api/users/{id}` - 更新用户资料
- `DELETE /api/users/{id}` - 删除用户

## 配置

在 `application.properties` 中配置以下参数：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/snail_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT 配置
jwt.secret=your_secret_key_here
jwt.expiration=3600
```

## 运行项目

1. 确保已安装 Java 17+ 和 Maven
2. 配置数据库连接
3. 运行 `mvn spring-boot:run`

## 测试

运行单元测试：
```bash
mvn test
```

## API 文档

启动应用后，访问 `http://localhost:8080/swagger-ui.html` 查看交互式 API 文档。

## 安全性

- 使用 BCrypt 加密存储密码
- JWT 令牌进行身份验证
- Spring Security 提供授权控制
- 防止常见安全漏洞