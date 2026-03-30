#!/bin/bash
# Snail 项目 - 本地快速启动脚本
# 用法：./scripts/start-local.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "🐌 Snail 项目 - 本地部署启动"
echo "=============================="
echo ""

# 检查 MySQL
echo "📊 [1/4] 检查 MySQL..."
if command -v mysql &> /dev/null; then
    echo "✅ MySQL 已安装"
else
    echo "❌ MySQL 未安装，请先安装 MySQL"
    exit 1
fi

# 检查 Redis
echo "📊 [2/4] 检查 Redis..."
if command -v redis-cli &> /dev/null; then
    if redis-cli ping &> /dev/null; then
        echo "✅ Redis 已启动"
    else
        echo "⚠️ Redis 未启动，尝试启动..."
        sudo systemctl start redis 2>/dev/null || echo "请手动启动 Redis: sudo systemctl start redis"
    fi
else
    echo "❌ Redis 未安装，请先安装 Redis"
    exit 1
fi

# 启动后端
echo "☕ [3/4] 启动后端服务..."
cd "$PROJECT_ROOT/backend"
if [ -f "target/classes/com/snail/SnailApplication.class" ]; then
    echo "✅ 后端已编译，直接运行..."
else
    echo "📦 编译后端..."
    mvn compile -q
fi

nohup mvn spring-boot:run -Dspring-boot.run.profiles=local > /tmp/snail-backend.log 2>&1 &
BACKEND_PID=$!
echo "✅ 后端已启动 (PID: $BACKEND_PID)"

# 等待后端启动
echo "⏳ 等待后端启动..."
sleep 10

# 检查后端
if curl -s http://localhost:8080/api/public/health > /dev/null 2>&1; then
    echo "✅ 后端运行正常"
else
    echo "⚠️ 后端可能启动失败，查看日志：tail -f /tmp/snail-backend.log"
fi

# 启动前端
echo "🎨 [4/4] 启动前端服务..."
cd "$PROJECT_ROOT/frontend"
nohup npm run dev -- --host 0.0.0.0 > /tmp/snail-frontend.log 2>&1 &
FRONTEND_PID=$!
echo "✅ 前端已启动 (PID: $FRONTEND_PID)"

# 等待前端启动
sleep 5

# 检查前端
if curl -s http://localhost:5173 > /dev/null 2>&1; then
    echo "✅ 前端运行正常"
else
    echo "⚠️ 前端可能启动失败，查看日志：tail -f /tmp/snail-frontend.log"
fi

echo ""
echo "=============================="
echo "✅ 全部服务已启动!"
echo ""
echo "📱 访问地址:"
echo "   前端：http://localhost:5173"
echo "   后端：http://localhost:8080"
echo "   Swagger: http://localhost:8080/api/swagger-ui.html"
echo ""
echo "📋 进程 ID:"
echo "   后端：$BACKEND_PID"
echo "   前端：$FRONTEND_PID"
echo ""
echo "🛑 停止服务:"
echo "   kill $BACKEND_PID  # 停止后端"
echo "   kill $FRONTEND_PID # 停止前端"
echo ""
