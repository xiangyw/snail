# 🐌 Snail 部署检查清单

> 版本：v1.0.0 | 更新日期：2026-03-30 | 适用：开发/测试/生产环境

---

## 📋 目录

1. [部署前检查](#部署前检查)
2. [开发环境部署](#开发环境部署)
3. [测试环境部署](#测试环境部署)
4. [生产环境部署](#生产环境部署)
5. [部署后验证](#部署后验证)
6. [回滚流程](#回滚流程)
7. [应急联系](#应急联系)

---

## 部署前检查

### 代码检查清单

- [ ] 所有代码已合并到目标分支
- [ ] 代码审查 (Code Review) 已通过
- [ ] 单元测试通过率 100%
- [ ] 集成测试通过率 100%
- [ ] 代码覆盖率 >= 80%
- [ ] 无严重/阻塞级别 Bug
- [ ] 性能测试达标
- [ ] 安全扫描通过

### 文档检查清单

- [ ] README 已更新
- [ ] API 文档已更新
- [ ] 数据库变更文档已更新
- [ ] 部署文档已更新
- [ ] 用户手册已更新 (如有功能变更)

### 配置检查清单

- [ ] 环境变量配置完成
- [ ] 数据库连接配置完成
- [ ] Redis 连接配置完成
- [ ] 第三方服务配置完成
- [ ] 敏感信息已加密 (不在代码中明文存储)

### 依赖检查清单

- [ ] 前端依赖已锁定版本 (package-lock.json)
- [ ] 后端依赖已锁定版本 (pom.xml)
- [ ] Docker 镜像版本已确认
- [ ] 第三方服务 API 版本兼容

---

## 开发环境部署

### 前置条件

- [ ] Docker 已安装并启动
- [ ] Node.js >= 18 已安装
- [ ] Java 17 已安装
- [ ] Maven >= 3.8 已安装
- [ ] Git 已安装

### 部署步骤

#### 1. 克隆代码

```bash
git clone https://github.com/xiangyw/snail.git
cd snail
git checkout develop
```

- [ ] 代码克隆成功
- [ ] 切换到 develop 分支

#### 2. 安装依赖

```bash
# 前端依赖
cd frontend
npm install

# 后端依赖
cd ../backend
mvn dependency:resolve
```

- [ ] 前端依赖安装成功
- [ ] 后端依赖安装成功

#### 3. 启动中间件

```bash
cd deploy/docker
docker-compose -f docker-compose.dev.yml up -d
```

- [ ] MySQL 启动成功
- [ ] Redis 启动成功
- [ ] 其他中间件启动成功

#### 4. 初始化数据库

```bash
# 创建数据库
docker exec -it snail-mysql mysql -uroot -psnail123456 -e "CREATE DATABASE IF NOT EXISTS snail_dev DEFAULT CHARACTER SET utf8mb4;"
```

- [ ] 数据库创建成功
- [ ] 表结构初始化成功

#### 5. 启动服务

```bash
# 前端 (新终端)
cd frontend
npm run dev

# 后端 (新终端)
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

- [ ] 前端服务启动成功 (http://localhost:5173)
- [ ] 后端服务启动成功 (http://localhost:8080)
- [ ] Swagger 文档可访问 (http://localhost:8080/swagger-ui.html)

#### 6. 功能验证

- [ ] 登录功能正常
- [ ] 主要业务流程正常
- [ ] 无控制台错误
- [ ] 无后端异常日志

---

## 测试环境部署

### 前置条件

- [ ] 测试服务器已就绪
- [ ] SSH 访问权限已配置
- [ ] 域名已解析
- [ ] SSL 证书已准备

### 部署步骤

#### 1. 代码准备

```bash
# 切换到测试分支
git checkout test
git pull origin test
```

- [ ] 代码已更新到最新版本
- [ ] 版本号已确认

#### 2. 构建前端

```bash
cd frontend

# 安装依赖
npm install

# 构建生产版本
npm run build

# 检查构建结果
ls -la dist/
```

- [ ] 构建成功
- [ ] 无构建错误
- [ ] 构建产物完整

#### 3. 构建后端

```bash
cd backend

# 清理并打包
mvn clean package -DskipTests

# 检查构建结果
ls -la target/*.jar
```

- [ ] 构建成功
- [ ] JAR 包生成
- [ ] 无编译错误

#### 4. 推送部署包

```bash
# 方式 1: SCP 传输
scp target/snail-backend-*.jar user@test-server:/opt/snail/

# 方式 2: Git 拉取
ssh user@test-server
cd /opt/snail
git pull origin test
```

- [ ] 部署包传输成功

#### 5. 更新配置

```bash
# 编辑生产配置
vi /opt/snail/backend/src/main/resources/application-test.yml

# 确认配置
# - 数据库连接
# - Redis 连接
# - API 密钥
# - 日志级别
```

- [ ] 数据库配置正确
- [ ] Redis 配置正确
- [ ] 第三方服务配置正确
- [ ] 日志级别适当 (INFO)

#### 6. 停止旧服务

```bash
# 查找进程
ps aux | grep snail

# 停止服务
pkill -f snail-backend

# 或使用 systemd
systemctl stop snail-backend
```

- [ ] 旧服务已停止

#### 7. 启动新服务

```bash
# 方式 1: 直接运行
nohup java -jar snail-backend-0.1.0.jar --spring.profiles.active=test > app.log 2>&1 &

# 方式 2: 使用 systemd
systemctl start snail-backend

# 方式 3: Docker 运行
docker-compose -f docker-compose.test.yml up -d
```

- [ ] 新服务已启动
- [ ] 进程运行正常

#### 8. 前端部署

```bash
# 上传到 Nginx 目录
scp -r dist/* user@test-server:/var/www/snail/

# 或使用 Docker
docker-compose -f docker-compose.test.yml up -d frontend
```

- [ ] 前端文件已上传
- [ ] Nginx 配置正确

#### 9. 服务验证

```bash
# 检查服务状态
curl http://test-api.snail.com/health

# 检查 API 文档
curl http://test-api.snail.com/swagger-ui.html

# 检查前端
curl http://test.snail.com
```

- [ ] 健康检查通过
- [ ] API 文档可访问
- [ ] 前端页面可访问

---

## 生产环境部署

### ⚠️ 重要提醒

- [ ] 已通知相关人员 (产品、测试、运营)
- [ ] 已选择低峰时段 (建议凌晨 2:00-5:00)
- [ ] 已备份数据库
- [ ] 已准备回滚方案
- [ ] 已通知客服团队

### 部署审批

- [ ] 产品经理审批
- [ ] 技术负责人审批
- [ ] 测试负责人审批

### 部署步骤

#### 1. 预部署检查 (T-1 小时)

```bash
# 检查系统资源
df -h          # 磁盘空间
free -h        # 内存使用
top            # CPU 使用
docker stats   # 容器资源

# 检查服务状态
systemctl status snail-backend
systemctl status nginx
docker-compose ps
```

- [ ] 磁盘空间充足 (>30%)
- [ ] 内存充足 (>50% 可用)
- [ ] CPU 负载正常 (<70%)
- [ ] 所有服务运行正常

#### 2. 数据库备份

```bash
# 完整备份
mysqldump -h localhost -u root -p snail_prod > backup_$(date +%Y%m%d_%H%M%S).sql

# 验证备份
ls -lh backup_*.sql
```

- [ ] 数据库备份完成
- [ ] 备份文件验证成功
- [ ] 备份文件已转移至安全位置

#### 3. 代码冻结

- [ ] 停止代码合并 (代码冻结)
- [ ] 确认生产分支版本
- [ ] 打版本标签

```bash
git checkout main
git pull origin main
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0
```

#### 4. 构建生产版本

```bash
# 前端构建
cd frontend
npm install --production
npm run build

# 后端构建
cd backend
mvn clean package -DskipTests -Pprod
```

- [ ] 前端构建成功
- [ ] 后端构建成功
- [ ] 构建产物已验证

#### 5. 灰度发布 (可选)

```bash
# 先部署到 10% 的服务器
# 监控 30 分钟
# 无异常后全量发布
```

- [ ] 灰度部署完成
- [ ] 监控指标正常
- [ ] 用户反馈正常

#### 6. 全量发布

```bash
# 逐台更新 (滚动发布)
for server in server1 server2 server3; do
  echo "Deploying to $server..."
  ssh $server "systemctl stop snail-backend"
  scp target/*.jar $server:/opt/snail/
  ssh $server "systemctl start snail-backend"
  ssh $server "curl http://localhost:8080/health"
  if [ $? -ne 0 ]; then
    echo "Failed on $server, rolling back..."
    break
  fi
  sleep 60  # 等待服务稳定
done
```

- [ ] 所有服务器部署完成
- [ ] 健康检查通过

#### 7. 更新负载均衡

```bash
# 如果使用 Nginx 负载均衡
# 逐台加入负载均衡池

# 如果使用云负载均衡
# 更新目标组
```

- [ ] 负载均衡配置更新
- [ ] 流量分发正常

#### 8. CDN 刷新 (如有静态资源更新)

```bash
# 刷新 CDN 缓存
# 根据 CDN 提供商操作
```

- [ ] CDN 缓存已刷新
- [ ] 新资源已生效

---

## 部署后验证

### 功能验证清单

#### 核心功能

- [ ] 用户登录/注册
- [ ] 手机号验证
- [ ] 首页加载
- [ ] 直播列表
- [ ] 商品列表
- [ ] 购物车
- [ ] 订单创建
- [ ] 支付流程
- [ ] 消息发送
- [ ] 个人中心

#### 关键 API 验证

```bash
# 健康检查
curl https://api.snail.com/health

# 登录接口
curl -X POST https://api.snail.com/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","verifyCode":"123456"}'

# 用户信息
curl https://api.snail.com/api/v1/users/me \
  -H "Authorization: Bearer <token>"

# 商品列表
curl https://api.snail.com/api/v1/shop/products
```

- [ ] 所有关键 API 响应正常
- [ ] 响应时间 < 500ms
- [ ] 无 5xx 错误

### 性能验证

```bash
# 使用 ab 或 wrk 进行压力测试
ab -n 1000 -c 100 https://api.snail.com/health

# 监控指标
# - QPS
# - 响应时间
# - 错误率
# - CPU/内存使用率
```

- [ ] QPS 达标
- [ ] 响应时间正常 (P95 < 500ms)
- [ ] 错误率 < 0.1%
- [ ] 系统资源使用正常

### 监控验证

- [ ] Prometheus 数据采集正常
- [ ] Grafana 仪表盘显示正常
- [ ] 告警规则生效
- [ ] 日志采集正常 (Loki/ELK)
- [ ] 链路追踪正常 (SkyWalking/Jaeger)

### 安全验证

- [ ] HTTPS 证书有效
- [ ] 无敏感信息泄露
- [ ] 接口认证正常
- [ ] 防 SQL 注入有效
- [ ] 防 XSS 攻击有效

### 业务验证

- [ ] 订单流程完整
- [ ] 支付流程完整
- [ ] 数据一致性检查
- [ ] 对账数据准确

---

## 回滚流程

### 回滚触发条件

出现以下情况立即回滚：

- [ ] 核心功能不可用 (>5 分钟)
- [ ] 数据错误或丢失
- [ ] 严重安全漏洞
- [ ] 错误率 > 5%
- [ ] 响应时间 > 5 秒

### 快速回滚步骤

#### 1. 停止新服务

```bash
# 停止应用
systemctl stop snail-backend

# 或 Docker
docker-compose down
```

#### 2. 恢复数据库 (如需要)

```bash
# 恢复备份
mysql -h localhost -u root -p snail_prod < backup_20260330_020000.sql
```

- [ ] 数据库恢复完成

#### 3. 恢复旧版本

```bash
# 切换代码版本
git checkout <previous-version>

# 重新构建部署
mvn clean package -DskipTests
systemctl start snail-backend
```

- [ ] 旧版本部署完成
- [ ] 服务启动成功

#### 4. 验证回滚

- [ ] 核心功能验证通过
- [ ] 监控指标恢复正常
- [ ] 用户反馈正常

#### 5. 回滚通知

- [ ] 通知产品团队
- [ ] 通知客服团队
- [ ] 通知运营团队
- [ ] 记录回滚原因

---

## 应急联系

### 关键联系人

| 角色 | 姓名 | 电话 | 微信 |
|------|------|------|------|
| 技术负责人 | - | - | - |
| 后端负责人 | - | - | - |
| 前端负责人 | - | - | - |
| 运维负责人 | - | - | - |
| 产品负责人 | - | - | - |

### 应急流程

```
1. 发现问题
   ↓
2. 评估影响 (严重/一般/轻微)
   ↓
3. 通知相关人员
   ↓
4. 紧急修复或回滚
   ↓
5. 验证修复
   ↓
6. 事后复盘
```

### 问题上报模板

```
【生产问题上报】

时间：2026-03-30 10:30
发现人：张三
影响范围：用户无法登录
严重程度：🔴 严重

问题描述：
用户反馈无法登录，登录接口返回 500 错误

影响用户数：约 1000 人

已采取措施：
1. 已通知技术团队
2. 已准备回滚

需要支持：
紧急开发资源支持
```

---

## 部署检查表 (打印版)

### 部署前

```
[ ] 代码审查通过
[ ] 测试全部通过
[ ] 文档已更新
[ ] 备份已完成
[ ] 人员已通知
[ ] 回滚方案准备
```

### 部署中

```
[ ] 旧服务停止
[ ] 新版本部署
[ ] 配置更新
[ ] 服务启动
[ ] 健康检查
```

### 部署后

```
[ ] 功能验证
[ ] 性能验证
[ ] 监控验证
[ ] 业务验证
[ ] 通知相关人员
```

---

## 附录

### 常用命令速查

```bash
# 查看服务状态
systemctl status snail-backend

# 查看日志
journalctl -u snail-backend -f

# 重启服务
systemctl restart snail-backend

# Docker 相关
docker-compose ps
docker-compose logs -f
docker-compose restart

# 数据库备份
mysqldump -h localhost -u root -p snail_prod > backup.sql

# 性能监控
top
htop
df -h
free -h
```

### 监控指标阈值

| 指标 | 警告阈值 | 严重阈值 |
|------|----------|----------|
| CPU 使用率 | > 70% | > 90% |
| 内存使用率 | > 80% | > 95% |
| 磁盘使用率 | > 80% | > 95% |
| 接口响应时间 | > 500ms | > 2000ms |
| 错误率 | > 1% | > 5% |
| QPS | - | 突增/突降 50% |

---

*部署有风险，操作需谨慎！*

*最后更新：2026-03-30*
