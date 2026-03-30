#!/bin/bash
# Snail 项目 - 一键部署脚本 (演示环境)

set -e
cd "$(dirname "${BASH_SOURCE[0]}")"

echo "🐌 Snail 项目 - 演示环境部署"
echo "=============================="

docker-compose -f docker-compose.dev.yml up -d

echo ""
echo "✅ 部署完成!"
echo ""
echo "📱 访问地址:"
echo "   前端：http://localhost:5173"
echo "   后端：http://localhost:8080"
echo "   Swagger: http://localhost:8080/api/swagger-ui.html"
echo ""
echo "🔑 演示账号：admin / admin123"
