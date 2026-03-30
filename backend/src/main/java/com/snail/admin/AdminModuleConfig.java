package com.snail.admin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.snail.admin")
public class AdminModuleConfig {
    // 管理后台模块配置
    // 该配置确保Spring能够扫描到admin包下的所有组件
}