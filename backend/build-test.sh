#!/bin/bash

echo "开始构建Snail商城模块..."

# 进入项目目录
cd "$(dirname "$0")"

echo "执行Maven编译..."
mvn compile -q

if [ $? -eq 0 ]; then
    echo "✓ 编译成功"
else
    echo "✗ 编译失败"
    exit 1
fi

echo "执行单元测试..."
mvn test -q

if [ $? -eq 0 ]; then
    echo "✓ 测试通过"
else
    echo "✗ 测试失败"
    exit 1
fi

echo "打包应用..."
mvn package -DskipTests -q

if [ $? -eq 0 ]; then
    echo "✓ 打包成功"
    echo "构建产物: $(find target -name "*.jar" | head -1)"
else
    echo "✗ 打包失败"
    exit 1
fi

echo "Snail商城模块构建完成！"