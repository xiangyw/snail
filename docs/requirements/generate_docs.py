#!/usr/bin/env python3
"""
根据 XMind 分析结果生成需求文档
"""

import json
import os

def load_analysis():
    """加载分析结果"""
    base_dir = "/home/openclaw/.openclaw/workspace/projects/snail/docs/requirements"
    with open(os.path.join(base_dir, 'analysis_result.json'), 'r', encoding='utf-8') as f:
        return json.load(f)

def get_topics_by_file(analysis, filename):
    """获取指定文件的所有主题"""
    file_data = analysis.get(filename, {})
    return file_data.get('topics', [])

def build_topic_tree(topics):
    """将扁平的主题列表转换为树形结构"""
    tree = {}
    
    for topic in topics:
        path = topic['path']
        parts = path.split(' > ')
        
        current = tree
        for i, part in enumerate(parts):
            if part not in current:
                current[part] = {'_info': topic, '_children': {}}
            current = current[part]['_children']
    
    return tree

def format_tree_to_markdown(tree, level=0, filename=""):
    """将树形结构转换为 Markdown 格式"""
    lines = []
    indent = "  " * level
    
    for name, data in tree.items():
        if name == '_info':
            continue
        
        info = data.get('_info', {})
        title = info.get('title', name)
        
        if level == 0:
            lines.append(f"\n## {title}")
        elif level == 1:
            lines.append(f"\n### {title}")
        elif level == 2:
            lines.append(f"\n#### {title}")
        else:
            lines.append(f"\n{indent}**{title}**")
        
        # 递归处理子节点
        children = data.get('_children', {})
        child_items = [(k, v) for k, v in children.items() if k != '_info']
        
        if child_items:
            if level >= 3:
                lines.append("")
            for child_name, child_data in child_items:
                lines.extend(format_tree_to_markdown({child_name: child_data}, level + 1, filename))
    
    return lines

def generate_requirements_doc(analysis):
    """生成完整需求文档"""
    
    doc = """# 🐌 蜗牛项目需求文档

> 本文档基于 XMind 需求图谱整理，包含 APP、官方网站、平台后台、商户后台的完整需求说明。

---

## 项目概述

蜗牛项目是一个综合性生活服务平台，主要包含以下模块：

- **APP 端**：移动端应用（本需求聚焦 H5 App）
- **官方网站**：Web 端展示
- **平台后台管理系统**：总平台运营管理
- **商户后台管理系统**：商户自主管理

---
"""
    
    # 处理每个模块
    modules = {
        '蜗牛 all.xmind': '完整需求概览',
        '蜗牛 app.xmind': 'APP 端需求',
        '蜗牛 backend.xmind': '后台管理系统需求'
    }
    
    for filename, module_name in modules.items():
        topics = get_topics_by_file(analysis, filename)
        if not topics:
            continue
        
        tree = build_topic_tree(topics)
        
        doc += f"\n\n## {module_name}\n"
        doc += f"\n> 来源：{filename}\n"
        
        # 获取根节点下的第一级分类
        root_children = {}
        for name, data in tree.items():
            if name == '🐌':
                root_children = data.get('_children', {})
                break
        
        for category, category_data in root_children.items():
            cat_info = category_data.get('_info', {})
            doc += f"\n### {category}\n"
            
            # 获取二级分类
            sub_categories = category_data.get('_children', {})
            for sub_name, sub_data in sub_categories.items():
                doc += f"\n#### {sub_name}\n"
                
                # 获取功能列表
                features = sub_data.get('_children', {})
                for feat_name, feat_data in features.items():
                    doc += f"\n- **{feat_name}**\n"
                    
                    # 获取详细功能点
                    details = feat_data.get('_children', {})
                    for detail_name, detail_data in details.items():
                        doc += f"  - {detail_name}\n"
                        
                        # 更深层级
                        sub_details = detail_data.get('_children', {})
                        for sd_name, sd_data in sub_details.items():
                            doc += f"    - {sd_name}\n"
    
    return doc

def generate_h5_spec(analysis):
    """生成 H5 App 技术规格书"""
    
    # 提取 APP 相关需求
    app_topics = get_topics_by_file(analysis, '蜗牛 app.xmind')
    
    spec = """# 🐌 蜗牛 H5 App 技术规格书

> 本文档定义 H5 App 的技术实现规范，适配移动端浏览器和微信内置浏览器。

---

## 1. 技术选型

### 1.1 前端框架
- **推荐**: Vue 3 + Vite 或 React 18
- **UI 库**: Vant UI / NutUI (移动端优先)
- **状态管理**: Pinia / Redux Toolkit

### 1.2 PWA 支持
- Service Worker 离线缓存
- Web App Manifest
- 推送通知 (Push API)

### 1.3 适配要求
- 移动端浏览器 (iOS Safari, Chrome Mobile)
- 微信内置浏览器 (X5 内核)
- 响应式设计 (320px - 768px)

---

## 2. 功能模块

"""
    
    # 构建 APP 功能树
    tree = build_topic_tree(app_topics)
    
    # 找到 APP 节点
    app_node = None
    for name, data in tree.items():
        if name == '🐌':
            app_node = data.get('_children', {}).get('APP', {})
            break
    
    if app_node:
        # 提取 Android 和 iOS 的功能（H5 需要兼容）
        platforms = app_node.get('_children', {})
        
        for platform, platform_data in platforms.items():
            spec += f"\n### 2.{list(platforms.keys()).index(platform) + 1} {platform} 功能\n"
            spec += f"\n> 注：H5 App 需兼容以下功能需求\n\n"
            
            features = platform_data.get('_children', {})
            for feat_name, feat_data in features.items():
                spec += f"#### {feat_name}\n"
                
                details = feat_data.get('_children', {})
                for detail_name, detail_data in details.items():
                    spec += f"- {detail_name}\n"
                    
                    # 更深层级
                    sub_details = detail_data.get('_children', {})
                    for sd_name, sd_data in sub_details.items():
                        spec += f"  - {sd_name}\n"
                        
                        # 第三层级
                        third = sd_data.get('_children', {})
                        for td_name, td_data in third.items():
                            spec += f"    - {td_name}\n"
                
                spec += "\n"
    
    spec += """
---

## 3. 微信适配

### 3.1 微信 SDK 集成
- JSSDK 配置（签名、权限验证）
- 分享功能（自定义标题、描述、图标）
- 支付功能（微信支付）
- 获取用户信息（需用户授权）

### 3.2 注意事项
- 避免使用 alert，使用自定义弹窗
- 图片上传使用微信选择器
- 地理位置使用微信定位 API
- 扫码功能使用微信扫 QR

---

## 4. PWA 特性

### 4.1 离线访问
- 缓存核心页面和资源
- 离线提示页面
- 网络状态检测

### 4.2 推送通知
- 使用 Web Push API
- 支持微信模板消息（备选）
- 通知权限请求引导

### 4.3 添加到桌面
- 配置 manifest.json
- 自定义图标和启动画面
- 引导用户添加

---

## 5. 性能优化

### 5.1 加载优化
- 资源压缩 (Gzip/Brotli)
- 图片懒加载
- 路由懒加载
- CDN 加速

### 5.2 渲染优化
- 虚拟列表 (长列表)
- 防抖节流
- 避免重排重绘

### 5.3 缓存策略
- HTTP 缓存头配置
- LocalStorage/IndexedDB
- Service Worker 缓存

---

## 6. 安全要求

### 6.1 认证安全
- JWT Token 认证
- Token 刷新机制
- 敏感操作二次验证

### 6.2 数据安全
- HTTPS 强制
- 敏感数据加密存储
- XSS 防护
- CSRF 防护

### 6.3 接口安全
- 请求签名
- 频率限制
- 参数校验

---

## 7. 兼容性要求

| 平台 | 最低版本 | 目标版本 |
|------|----------|----------|
| iOS Safari | 12+ | 15+ |
| Android Chrome | 80+ | 100+ |
| 微信内置浏览器 | 7.0+ | 8.0+ |

---

## 8. 开发规范

### 8.1 代码规范
- ESLint + Prettier
- TypeScript 类型检查
- 组件化开发

### 8.2 目录结构
```
src/
├── assets/          # 静态资源
├── components/      # 公共组件
├── views/           # 页面组件
├── store/           # 状态管理
├── utils/           # 工具函数
├── api/             # API 接口
├── router/          # 路由配置
└── styles/          # 全局样式
```

### 8.3 Git 规范
- feature/* 功能分支
- bugfix/* 修复分支
- release/* 发布分支
- main 主分支

---

*文档版本：1.0*
*最后更新：2026-03-30*
"""
    
    return spec

def generate_mvp_scope(analysis):
    """生成 MVP 范围定义"""
    
    mvp = """# 🐌 蜗牛 H5 App - MVP 范围定义

> 第一阶段最小可行产品 (MVP) 功能范围

---

## MVP 目标

快速验证核心业务模式，上线可用版本，收集用户反馈。

**目标周期**: 4-6 周
**目标平台**: H5 (移动端浏览器 + 微信)

---

## 核心功能 (P0 - 必须有)

### 1. 用户系统
- [ ] 注册/登录（手机号 + 验证码）
- [ ] 个人中心基础信息
- [ ] 退出登录

### 2. 首页
- [ ] 天气显示
- [ ] 搜索功能
- [ ] 公告展示
- [ ] 核心功能入口

### 3. 任务系统
- [ ] 任务列表展示
- [ ] 任务详情
- [ ] 任务完成提交
- [ ] 任务奖励查看

### 4. 积分/钱包
- [ ] 积分余额显示
- [ ] 积分明细
- [ ] 提现功能（基础）

### 5. 内容发布
- [ ] 发布动态/内容
- [ ] 图片上传
- [ ] 内容审核状态

### 6. 后台管理 (基础版)
- [ ] 用户管理
- [ ] 内容审核
- [ ] 数据统计（基础）

---

## 次要功能 (P1 - 应该有)

### 1. 社交功能
- [ ] 关注/粉丝
- [ ] 点赞/评论
- [ ] 消息通知

### 2. 电商功能
- [ ] 商品浏览
- [ ] 购物车
- [ ] 订单管理
- [ ] 支付集成

### 3. 直播功能
- [ ] 直播观看
- [ ] 直播互动

### 4. 推广系统
- [ ] 邀请好友
- [ ] 推广奖励
- [ ] 推广数据

---

## 后续功能 (P2 - 可以有)

### 1. 高级功能
- [ ] 旅游服务
- [ ] 广场舞社区
- [ ] 老年大学课程
- [ ] 直播开播

### 2. 商户功能
- [ ] 商户入驻
- [ ] 商品管理
- [ ] 订单处理
- [ ] 数据分析

### 3. 平台运营
- [ ] 活动管理
- [ ] 广告投放
- [ ] 用户分层运营
- [ ] 精细化数据分析

---

## 技术 MVP

### 必须实现
- [ ] PWA 基础支持（离线缓存）
- [ ] 微信适配
- [ ] 响应式设计
- [ ] 基础性能优化
- [ ] HTTPS 部署

### 可延后
- [ ] 推送通知
- [ ] 完整 PWA 功能
- [ ] 多语言支持
- [ ] 高级动画效果

---

## 排除范围 (本次不做)

- ❌ 原生 Android/iOS App
- ❌ 小程序（后续独立项目）
- ❌ PC 端 Web
- ❌ 复杂 AI 功能
- ❌ 区块链/虚拟货币

---

## 里程碑

| 阶段 | 时间 | 目标 |
|------|------|------|
| M1 | 第 1-2 周 | 用户系统 + 首页完成 |
| M2 | 第 3-4 周 | 任务系统 + 积分系统完成 |
| M3 | 第 5 周 | 后台管理 + 联调测试 |
| M4 | 第 6 周 | 上线发布 |

---

## 成功标准

1. **功能完整**: P0 功能 100% 完成
2. **性能达标**: 首屏加载 < 3s
3. **兼容性**: 主流浏览器正常运行
4. **用户体验**: 无明显阻塞性 bug

---

*文档版本：1.0*
*最后更新：2026-03-30*
"""
    
    return mvp

def main():
    base_dir = "/home/openclaw/.openclaw/workspace/projects/snail/docs/requirements"
    
    print("加载分析结果...")
    analysis = load_analysis()
    
    # 生成完整需求文档
    print("生成 REQUIREMENTS.md...")
    req_doc = generate_requirements_doc(analysis)
    with open(os.path.join(base_dir, 'REQUIREMENTS.md'), 'w', encoding='utf-8') as f:
        f.write(req_doc)
    
    # 生成 H5 技术规格书
    print("生成 H5_SPEC.md...")
    h5_spec = generate_h5_spec(analysis)
    with open(os.path.join(base_dir, 'H5_SPEC.md'), 'w', encoding='utf-8') as f:
        f.write(h5_spec)
    
    # 生成 MVP 范围定义
    print("生成 MVP_SCOPE.md...")
    mvp_doc = generate_mvp_scope(analysis)
    with open(os.path.join(base_dir, 'MVP_SCOPE.md'), 'w', encoding='utf-8') as f:
        f.write(mvp_doc)
    
    print("\n✅ 文档生成完成!")
    print(f"  - REQUIREMENTS.md")
    print(f"  - H5_SPEC.md")
    print(f"  - MVP_SCOPE.md")

if __name__ == "__main__":
    main()
