# 🐌 Snail 项目 - 消息通知模块 Bug 清单

> **版本**: 1.0  
> **日期**: 2026-03-30  
> **测试工程师**: 消息通知模块测试负责人  
> **状态**: 🔄 跟踪中

---

## 📊 Bug 统计

| 严重程度 | 数量 | 已修复 | 待修复 | 修复率 |
|---------|------|--------|--------|--------|
| 🔴 高 (Critical) | 1 | 0 | 1 | 0% |
| 🟡 中 (Major) | 2 | 0 | 2 | 0% |
| 🟢 低 (Minor) | 1 | 0 | 1 | 0% |
| **总计** | **4** | **0** | **4** | **0%** |

---

## 🐛 Bug 详情

### Bug #001 - WebSocket 消息接收后未读计数未更新

| 属性 | 值 |
|------|-----|
| **ID** | MSG-BUG-001 |
| **标题** | WebSocket 消息接收后未读计数未更新 |
| **严重程度** | 🟡 中 (Major) |
| **优先级** | P0 |
| **状态** | 🔴 待修复 |
| **模块** | 前端 - MessageCenter |
| **发现日期** | 2026-03-30 |
| **发现人** | 测试工程师 |
| **指派给** | 前端开发 |

**描述：**  
当用户打开消息中心页面时，通过 WebSocket 接收到新消息推送后，页面上的未读计数没有实时更新，需要手动刷新页面才能看到正确的未读数量。

**复现步骤：**
1. 登录用户 A
2. 打开消息中心页面
3. 用户 B 发送一条新消息给用户 A
4. 观察消息中心页面上的未读计数

**预期结果：**  
未读计数自动 +1，对应标签显示红点

**实际结果：**  
未读计数不变，仍显示旧值

**影响范围：**  
- 用户体验下降，可能错过新消息
- 需要手动刷新才能看到最新状态

**技术定位：**
- 文件：`src/views/Message/MessageCenter.vue`
- 方法：`handleWebSocketMessage()`
- 问题：收到消息后未触发未读计数更新

**修复建议：**
```javascript
// 在 handleWebSocketMessage 方法中
handleWebSocketMessage(message) {
  // 添加消息到列表
  this.messages.unshift(message);
  
  // TODO: 更新未读计数
  if (!message.isRead) {
    this.unreadCount.total++;
    this.unreadCount[message.type]++;
  }
  
  // 或者重新调用 API 获取最新未读计数
  this.fetchUnreadCount();
}
```

**测试用例：**
- MC-015: WebSocket 消息接收
- WS-007: 未读计数更新
- INT-003: 未读计数同步

---

### Bug #002 - 图片消息多张图片布局错乱

| 属性 | 值 |
|------|-----|
| **ID** | MSG-BUG-002 |
| **标题** | 图片消息多张图片布局错乱 |
| **严重程度** | 🟡 中 (Major) |
| **优先级** | P1 |
| **状态** | 🔴 待修复 |
| **模块** | 前端 - MessageDetail |
| **发现日期** | 2026-03-30 |
| **发现人** | 测试工程师 |
| **指派给** | 前端开发 |

**描述：**  
在消息详情页面，当消息包含多张图片时，图片布局出现错乱，图片之间相互重叠，严重影响视觉效果。

**复现步骤：**
1. 用户 B 发送一条包含 3 张图片的消息
2. 用户 A 打开该消息详情
3. 观察图片显示区域

**预期结果：**  
图片以网格形式整齐排列（如 2 列或 3 列布局）

**实际结果：**  
图片重叠在一起，布局混乱

**截图：**
```
实际显示：
[图片 1][图片 2 重叠在图片 1 上]
[图片 3 重叠在图片 2 上]

预期显示：
[图片 1]  [图片 2]
[图片 3]
```

**影响范围：**  
- 用户体验差
- 图片内容可能无法正常查看

**技术定位：**
- 文件：`src/views/Message/MessageDetail.vue`
- 组件：`.message-images`
- 问题：CSS 布局样式缺失或错误

**修复建议：**
```css
/* 添加或修改 CSS */
.message-images {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 2 列布局 */
  gap: 8px;
  margin-top: 12px;
}

.message-images img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
}

/* 单张图片时占满全行 */
.message-images img:only-child {
  grid-column: 1 / -1;
  height: auto;
  max-height: 300px;
}
```

**测试用例：**
- MD-007: 图片消息渲染

---

### Bug #003 - 页面关闭时 WebSocket 未正确关闭

| 属性 | 值 |
|------|-----|
| **ID** | MSG-BUG-003 |
| **标题** | 页面关闭时 WebSocket 未正确关闭 |
| **严重程度** | 🟢 低 (Minor) |
| **优先级** | P2 |
| **状态** | 🔴 待修复 |
| **模块** | 前端 - WebSocket 服务 |
| **发现日期** | 2026-03-30 |
| **发现人** | 测试工程师 |
| **指派给** | 前端开发 |

**描述：**  
当用户关闭浏览器标签页或页面时，WebSocket 连接没有正确关闭，可能导致服务器端连接泄漏。

**复现步骤：**
1. 打开消息中心页面（建立 WebSocket 连接）
2. 直接关闭浏览器标签页
3. 检查服务器端连接状态

**预期结果：**  
WebSocket 连接正常关闭，服务器端连接数减少

**实际结果：**  
服务器端连接仍然存在，需要等待超时后才清理

**影响范围：**  
- 服务器资源浪费
- 长期运行可能导致连接数过多

**技术定位：**
- 文件：`src/utils/websocket.js`
- 事件：`beforeunload`
- 问题：事件监听器未正确绑定或处理

**修复建议：**
```javascript
// 在 WebSocketService 类中
class WebSocketService {
  constructor() {
    // 绑定页面关闭事件
    window.addEventListener('beforeunload', () => {
      this.disconnect();
    });
  }
  
  disconnect() {
    if (this.ws) {
      this.ws.close(1000, 'Normal Closure');
      this.ws = null;
    }
  }
}
```

**测试用例：**
- WS-018: 页面关闭清理

---

### Bug #004 - DELETE /messages/{id} 接口未实现

| 属性 | 值 |
|------|-----|
| **ID** | MSG-BUG-004 |
| **标题** | DELETE /messages/{id} 接口未实现 |
| **严重程度** | 🔴 高 (Critical) |
| **优先级** | P0 |
| **状态** | 🔴 待实现 |
| **模块** | 后端 - 消息 API |
| **发现日期** | 2026-03-30 |
| **发现人** | 测试工程师 |
| **指派给** | 后端开发 |

**描述：**  
消息删除功能的前端已实现，但后端对应的 DELETE /messages/{id} 接口尚未实现，导致用户无法删除消息。

**复现步骤：**
1. 打开消息列表
2. 点击某条消息的删除按钮
3. 观察网络请求和响应

**预期结果：**  
消息成功删除，从列表中移除

**实际结果：**  
接口返回 404 或 501 错误，删除失败

**影响范围：**  
- 用户无法删除消息
- 功能不完整

**技术定位：**
- 文件：`src/main/java/com/snail/controller/MessageController.java`
- 缺失方法：`deleteMessage(@PathVariable Long id)`

**修复建议：**
```java
// 在 MessageController 中添加
@DeleteMapping("/{id}")
@PreAuthorize("isAuthenticated()")
public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
    Long currentUserId = authenticationFacade.getCurrentUserId();
    
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("消息不存在"));
    
    // 只能删除自己的消息
    if (!message.getFromUserId().equals(currentUserId) && 
        !message.getToUserId().equals(currentUserId)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
    messageRepository.delete(message);
    
    return ResponseEntity.ok().build();
}
```

**测试用例：**
- MSG-API-016: 删除消息
- INT-006: 消息删除同步

---

## 📈 Bug 趋势

### 按模块分布

```
前端：███████ (3)
后端：██ (1)
```

### 按严重程度分布

```
高：██ (1)
中：████ (2)
低：██ (1)
```

### 按状态分布

```
待修复：████████ (4)
修复中： (0)
已修复： (0)
已验证： (0)
```

---

## 🔄 修复进度

### 第 1 周 (2026-03-30)

| Bug ID | 操作 | 操作人 | 日期 | 备注 |
|--------|------|--------|------|------|
| #001 | 创建 | 测试 | 03-30 | 初始报告 |
| #002 | 创建 | 测试 | 03-30 | 初始报告 |
| #003 | 创建 | 测试 | 03-30 | 初始报告 |
| #004 | 创建 | 测试 | 03-30 | 初始报告 |

---

## 📋 待办事项

### 高优先级 (P0)

- [ ] 修复 Bug #001 - WebSocket 消息接收后未读计数未更新
- [ ] 实现 Bug #004 - DELETE /messages/{id} 接口

### 中优先级 (P1)

- [ ] 修复 Bug #002 - 图片消息多张图片布局错乱

### 低优先级 (P2)

- [ ] 修复 Bug #003 - 页面关闭时 WebSocket 未正确关闭

---

## ✅ 验收标准

### Bug 修复验收

1. **功能验证**: 修复后功能按预期工作
2. **回归测试**: 相关测试用例全部通过
3. **代码审查**: 修复代码通过 Review
4. **性能验证**: 无性能退化

### 发布标准

- [ ] 所有 P0 Bug 已修复
- [ ] 所有 P1 Bug 已修复或接受
- [ ] 回归测试通过
- [ ] 性能测试通过

---

## 📝 备注

### 已知问题

1. Bug #004 需要后端开发配合实现
2. Bug #003 影响较小，可延后修复

### 建议

1. 建议优先修复 Bug #001，影响核心用户体验
2. Bug #004 建议尽快实现，完善 CRUD 功能
3. 建议添加自动化测试防止回归

---

*Bug 清单版本：1.0*  
*创建时间：2026-03-30 12:20*  
*最后更新：2026-03-30 12:20*  
*状态：🔄 跟踪中*
