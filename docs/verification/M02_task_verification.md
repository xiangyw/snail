# M02 任务管理模块验证报告

> 验证日期: 2026-03-30  
> 验证人: 需求验证工程师  
> 模块: 任务管理模块  
> 状态: ⚠️ 需要修复

---

## 1. 需求覆盖情况

### 1.1 MVP 需求对照

| 需求项 | 状态 | 备注 |
|--------|------|------|
| 任务列表（分类展示） | ✅ 已实现 | 后端 API 存在，前端使用模拟数据 |
| 任务详情 | ✅ 已实现 | GET /api/tasks/{taskId} |
| 任务完成提交 | ⚠️ API 命名不一致 | 使用 `/complete` 而非 `/submit` |
| 任务奖励发放 | ✅ 已实现 | completeTask 中设置 pointsEarned |
| 任务记录查询 | ✅ 已实现 | GET /api/tasks/user/{userId} |

### 1.2 需求文档引用

来自 `docs/requirements/MVP_SCOPE.md` P0 功能定义:

> #### 2.3 任务系统
> - [ ] 任务列表（分类展示）
> - [ ] 任务详情
> - [ ] 任务完成提交
> - [ ] 任务奖励发放
> - [ ] 任务记录查询

---

## 2. API 实现情况

### 2.1 后端 API 清单

| API 端点 | 方法 | 状态 | 说明 |
|----------|------|------|------|
| /api/tasks | GET | ✅ 正常 | 获取活跃任务列表 |
| /api/tasks/{taskId} | GET | ✅ 正常 | 获取任务详情 |
| /api/tasks/type/{type} | GET | ✅ 正常 | 按类型筛选任务 |
| /api/tasks/user/{userId} | GET | ✅ 正常 | 获取用户任务进度 |
| /api/tasks/{taskId}/start | POST | ✅ 正常 | 开始/领取任务 |
| /api/tasks/{taskId}/complete | POST | ✅ 正常 | 完成任务 |
| /api/tasks/create | POST | ✅ 正常 | 管理员创建任务 |

### 2.2 需求要求 vs 实际实现

| 需求要求 API | 实际实现 API | 状态 |
|--------------|--------------|------|
| GET /api/tasks | GET /api/tasks | ✅ 匹配 |
| GET /api/tasks/{id} | GET /api/tasks/{taskId} | ✅ 匹配 |
| POST /api/tasks/{id}/claim | POST /api/tasks/{taskId}/start | ⚠️ 命名不一致 |
| POST /api/tasks/{id}/submit | POST /api/tasks/{taskId}/complete | ⚠️ 命名不一致 |

**问题**: API 命名与需求文档不一致。需求文档使用 `claim`（领取）和 `submit`（提交），但实现使用 `start`（开始）和 `complete`（完成）。

---

## 3. 前后端联调情况

### 3.1 前端 API 调用分析

**文件**: `frontend/src/views/Tasks/index.vue`

| 功能 | 状态 | 说明 |
|------|------|------|
| 任务列表加载 | ❌ 未实现 | 使用模拟数据，未调用 API |
| 任务详情跳转 | ✅ 已实现 | 跳转到 `/tasks/${id}` |
| 任务领取 | ❌ 未实现 | 无领取按钮或 API 调用 |
| 任务提交 | ❌ 未实现 | 无提交按钮或 API 调用 |

### 3.2 前端代码问题

```javascript
// 当前实现 - 使用模拟数据
const onLoad = () => {
  // TODO: 调用 API 加载任务列表
  setTimeout(() => {
    taskList.value.push(
      { id: 1, title: '每日签到', description: '完成每日签到任务', status: 'doing' },
      // ...
    )
  }, 1000)
}
```

**问题**:
1. 前端未调用实际 API (`GET /api/tasks`)
2. 使用硬编码模拟数据，无法与后端联调
3. 缺少任务领取功能（应调用 `/api/tasks/{id}/start`）
4. 缺少任务提交功能（应调用 `/api/tasks/{id}/complete`）

---

## 4. 发现的问题和 Bug

### 4.1 高优先级问题

| # | 问题 | 严重程度 | 类型 |
|---|------|----------|------|
| 1 | 前端任务列表使用模拟数据，未调用后端 API | 🔴 高 | 联调缺失 |
| 2 | 前端缺少任务领取功能（claim/start） | 🔴 高 | 功能缺失 |
| 3 | 前端缺少任务提交功能（submit/complete） | 🔴 高 | 功能缺失 |
| 4 | API 命名与需求文档不一致（claim vs start, submit vs complete） | 🟡 中 | 规范问题 |

### 4.2 中低优先级问题

| # | 问题 | 严重程度 | 类型 |
|---|------|----------|------|
| 5 | 前端任务状态字段映射可能不匹配后端 | 🟡 中 | 数据格式 |
| 6 | 缺少错误处理和 Loading 状态 UI | 🟢 低 | 体验优化 |
| 7 | 任务分类筛选（进行中/已完成）未实现 | 🟢 低 | 功能缺失 |

---

## 5. 修改建议

### 5.1 立即修复（阻塞 MVP）

#### 5.1.1 前端 API 集成

**文件**: `frontend/src/views/Tasks/index.vue`

```typescript
// 添加 API 调用
import axios from 'axios'

const fetchTasks = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/tasks')
    taskList.value = response.data
  } catch (error) {
    console.error('Failed to fetch tasks:', error)
  } finally {
    loading.value = false
  }
}

// 修改 onLoad
const onLoad = () => {
  fetchTasks()
}
```

#### 5.1.2 添加任务领取功能

```typescript
const claimTask = async (taskId: number) => {
  try {
    await axios.post(`/api/tasks/${taskId}/start`, null, {
      params: { userId: currentUserId }
    })
    // 刷新任务列表
    await fetchTasks()
  } catch (error) {
    console.error('Failed to claim task:', error)
  }
}
```

#### 5.1.3 添加任务提交功能

```typescript
const submitTask = async (taskId: number, completionProof?: string) => {
  try {
    await axios.post(`/api/tasks/${taskId}/complete`, 
      { completionProof },
      { params: { userId: currentUserId } }
    )
    // 刷新任务列表
    await fetchTasks()
  } catch (error) {
    console.error('Failed to submit task:', error)
  }
}
```

### 5.2 建议优化

#### 5.2.1 API 命名统一（选其一）

**方案 A**: 修改后端 API 名称以匹配需求文档
- `/api/tasks/{id}/claim` → 新增 endpoint
- `/api/tasks/{id}/submit` → 新增 endpoint

**方案 B**: 保持当前命名，更新需求文档
- 在 `docs/requirements/MVP_SCOPE.md` 中注明实际 API 名称

**推荐**: 方案 A，保持需求文档的权威性

#### 5.2.2 数据字段映射

后端返回字段:
- `Task.status` - 枚举: ACTIVE, PENDING, COMPLETED, EXPIRED
- `UserTask.status` - 枚举: PENDING, COMPLETED, CLAIMED

前端期望:
- `task.status` - 字符串: 'doing', 'done'

需要添加映射函数:
```typescript
const mapStatus = (status: string) => {
  const mapping: Record<string, string> = {
    'PENDING': 'doing',
    'COMPLETED': 'done',
    'CLAIMED': 'doing',
    'ACTIVE': 'doing'
  }
  return mapping[status] || 'doing'
}
```

---

## 6. 验证结论

| 维度 | 状态 | 评分 |
|------|------|------|
| 需求覆盖 | ⚠️ 部分覆盖 | 60% |
| 后端 API 实现 | ✅ 已实现 | 85% |
| 前端功能实现 | ❌ 未完成 | 20% |
| 前后端联调 | ❌ 未联调 | 0% |

### 验证结果: 🚫 不通过

**原因**:
1. 前端任务管理页面使用模拟数据，未与后端 API 联调
2. 缺少任务领取和提交的核心功能
3. API 命名与需求文档存在不一致

**下一步行动**:
1. 前端集成后端 API 调用
2. 实现任务领取和提交功能
3. 统一 API 命名规范
4. 进行完整的前后端联调测试

---

*报告结束*