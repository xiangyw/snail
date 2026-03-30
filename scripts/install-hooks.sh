#!/bin/bash
# Snail 项目 Git Hooks 安装脚本
# 用法：./scripts/install-hooks.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
HOOKS_DIR="$PROJECT_ROOT/.git/hooks"

echo "🔧 安装 Git Hooks 到 $HOOKS_DIR"

# 复制 pre-push hook
cp "$SCRIPT_DIR/git-hooks/pre-push" "$HOOKS_DIR/pre-push"
chmod +x "$HOOKS_DIR/pre-push"

echo "✅ Git Hooks 安装完成"
echo ""
echo "已安装的 hooks:"
echo "  - pre-push: 防止直接推送到 main/test 分支"
echo ""
echo "测试：尝试 push 到 main 分支将被拒绝"
