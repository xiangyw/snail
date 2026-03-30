#!/bin/bash

# Snail Project Test Runner
# 版本：1.0
# 用途：运行所有测试并生成报告

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Java 和 Maven
check_prerequisites() {
    print_info "检查环境依赖..."
    
    if ! command -v java &> /dev/null; then
        print_error "Java 未安装，请先安装 Java 17+"
        exit 1
    fi
    
    if ! command -v mvn &> /dev/null; then
        print_error "Maven 未安装，请先安装 Maven 3.8+"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        print_error "Java 版本过低，需要 Java 17+，当前版本：$JAVA_VERSION"
        exit 1
    fi
    
    print_success "环境检查通过"
}

# 运行单元测试
run_unit_tests() {
    print_info "运行单元测试..."
    cd backend
    
    mvn test -Dtest="*ServiceTest,*ControllerTest,*EntityTest,*DtoTest" \
        -DfailIfNoTests=false
    
    cd ..
    print_success "单元测试完成"
}

# 运行集成测试
run_integration_tests() {
    print_info "运行集成测试..."
    cd backend
    
    mvn test -Dtest="*IntegrationTest" \
        -DfailIfNoTests=false
    
    cd ..
    print_success "集成测试完成"
}

# 运行 Repository 测试
run_repository_tests() {
    print_info "运行 Repository 测试..."
    cd backend
    
    mvn test -Dtest="*RepositoryTest" \
        -DfailIfNoTests=false
    
    cd ..
    print_success "Repository 测试完成"
}

# 运行所有测试
run_all_tests() {
    print_info "运行所有测试..."
    cd backend
    
    mvn clean test
    
    cd ..
    print_success "所有测试完成"
}

# 生成覆盖率报告
generate_coverage_report() {
    print_info "生成测试覆盖率报告..."
    cd backend
    
    mvn clean test jacoco:report
    
    cd ..
    
    print_success "覆盖率报告已生成"
    print_info "报告位置：backend/target/site/jacoco/index.html"
    
    # 尝试在浏览器中打开报告
    if command -v open &> /dev/null; then
        open backend/target/site/jacoco/index.html
    elif command -v xdg-open &> /dev/null; then
        xdg-open backend/target/site/jacoco/index.html
    fi
}

# 运行特定测试类
run_specific_test() {
    local test_class=$1
    
    if [ -z "$test_class" ]; then
        print_error "请指定测试类名"
        echo "用法：$0 specific <TestClass>"
        exit 1
    fi
    
    print_info "运行测试类：$test_class"
    cd backend
    
    mvn test -Dtest="$test_class"
    
    cd ..
    print_success "测试完成：$test_class"
}

# 显示帮助信息
show_help() {
    echo "Snail 项目测试运行器"
    echo ""
    echo "用法：$0 <command>"
    echo ""
    echo "命令:"
    echo "  all          运行所有测试"
    echo "  unit         运行单元测试"
    echo "  integration  运行集成测试"
    echo "  repository   运行 Repository 测试"
    echo "  coverage     运行测试并生成覆盖率报告"
    echo "  specific     运行特定测试类 (需要指定类名)"
    echo "  help         显示帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 all"
    echo "  $0 unit"
    echo "  $0 coverage"
    echo "  $0 specific AuthServiceTest"
    echo ""
}

# 主函数
main() {
    local command=${1:-help}
    
    case $command in
        all)
            check_prerequisites
            run_all_tests
            ;;
        unit)
            check_prerequisites
            run_unit_tests
            ;;
        integration)
            check_prerequisites
            run_integration_tests
            ;;
        repository)
            check_prerequisites
            run_repository_tests
            ;;
        coverage)
            check_prerequisites
            generate_coverage_report
            ;;
        specific)
            check_prerequisites
            run_specific_test "$2"
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            print_error "未知命令：$command"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
