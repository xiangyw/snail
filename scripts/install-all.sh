#!/bin/bash
# Snail 项目 - 快速依赖安装脚本
# 用法：./scripts/install-all.sh

set -e

echo "🐌 Snail 项目 - 依赖安装"
echo "========================"

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# 前端依赖
echo ""
echo "📦 [1/3] 安装前端依赖..."
cd frontend
if [ -f "package.json" ]; then
    npm install --registry=https://registry.npmmirror.com
    echo "✅ 前端依赖安装完成"
else
    echo "⚠️ 前端 package.json 不存在"
fi

# 后端依赖
echo ""
echo "☕ [2/3] 安装后端依赖..."
cd ../backend
if [ -f "pom.xml" ]; then
    mvn dependency:resolve -q
    echo "✅ 后端依赖安装完成"
else
    echo "⚠️ 后端 pom.xml 不存在"
fi

# Docker 镜像预拉取
echo ""
echo "🐳 [3/3] 预拉取 Docker 镜像..."
cd ../deploy/docker
if [ -f "docker-compose.dev.yml" ]; then
    docker-compose -f docker-compose.dev.yml pull || echo "⚠️ Docker 不可用，跳过"
    echo "✅ Docker 镜像准备完成"
else
    echo "⚠️ docker-compose 文件不存在"
fi

echo ""
echo "========================"
echo "✅ 全部依赖安装完成!"
echo ""
echo "下一步:"
echo "  前端：cd frontend && npm run dev"
echo "  后端：cd backend && mvn spring-boot:run"
echo "  数据库：docker-compose -f docker-compose.dev.yml up -d"
