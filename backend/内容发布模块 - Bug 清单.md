# 内容发布模块 Bug 清单

## Bug 统计

| 严重程度 | 数量 | 已修复 | 待修复 | 修复率 |
|---------|------|--------|--------|--------|
| P0 - 严重 | 0 | 0 | 0 | - |
| P1 - 重要 | 1 | 0 | 1 | 0% |
| P2 - 次要 | 2 | 0 | 2 | 0% |
| **总计** | **3** | **0** | **3** | **0%** |

---

## Bug 详情

### 🔴 P1 - 重要 Bug

#### BUG-001: ContentDetail 权限检查未实现

**基本信息**:
- **发现日期**: 2026-03-30
- **发现阶段**: 测试阶段
- **模块**: 前端 - ContentDetail.vue
- **负责人**: 待分配
- **状态**: 🟡 待修复

**问题描述**:
ContentDetail 组件中的 `isOwner` 计算属性被硬编码为返回 `true`，导致无法正确判断内容所有权。非所有者用户也能看到删除和编辑按钮。

**复现步骤**:
1. 以普通用户 A 身份登录
2. 创建一条内容
3. 以普通用户 B 身份登录
4. 访问用户 A 创建的内容详情页
5. 观察到删除和编辑按钮仍然显示

**预期行为**:
- 非所有者不应该看到删除和编辑按钮
- 只有内容创建者或管理员才能看到操作按钮

**实际行为**:
- 所有用户都能看到删除和编辑按钮

**影响范围**:
- 安全风险：用户可能误删他人内容
- 用户体验：显示不可用的操作按钮

**修复建议**:
```javascript
// ContentDetail.vue
import { useUserStore } from '../stores/user'

const userStore = useUserStore()

const isOwner = computed(() => {
  const currentUser = userStore.currentUser
  if (!currentUser) return false
  
  // 管理员可以操作所有内容
  if (currentUser.role === 'ADMIN') return true
  
  // 所有者可以操作自己的内容
  return currentUser.id === content.value?.userId
})
```

**测试用例**:
```javascript
it('非所有者不应该看到删除按钮', async () => {
  wrapper.vm.isOwner = false
  await flushPromises()
  
  const deleteButton = wrapper.find('button:has-text("删除")')
  expect(deleteButton.exists()).toBe(false)
})
```

---

### 🟡 P2 - 次要 Bug

#### BUG-002: PublishView 测试用例设计缺陷

**基本信息**:
- **发现日期**: 2026-03-30
- **发现阶段**: 测试阶段
- **模块**: 前端测试 - PublishView.test.js
- **负责人**: 测试工程师
- **状态**: 🟡 待修复

**问题描述**:
测试用例 "填写后发布按钮启用" 设计有误，只填写了标题字段就期望发布按钮启用，但实际上还有其他必填字段。

**复现步骤**:
1. 运行前端测试：`npm run test`
2. 观察 PUB-006 测试结果
3. 测试失败

**预期行为**:
- 测试应该填写所有必填字段后验证按钮启用

**实际行为**:
- 测试只填写了标题字段
- 发布按钮仍然禁用（这是正确的业务逻辑）
- 测试误报失败

**影响范围**:
- 测试报告准确率
- 开发调试时间

**修复建议**:
```javascript
// 修改测试用例
it('填写所有必填字段后发布按钮应启用', async () => {
  // 填写所有必填字段
  wrapper.vm.taskForm.title = '测试任务'
  wrapper.vm.taskForm.description = '测试描述'
  wrapper.vm.taskForm.points = '100'
  wrapper.vm.taskForm.dueDate = '2026-04-15'
  await flushPromises()
  
  const publishButton = wrapper.find('.van-button')
  expect(publishButton.attributes('disabled')).toBeUndefined()
})
```

**优先级**: 低
**预计修复时间**: 30 分钟

---

#### BUG-003: ContentList 空状态测试需要 mock 支持

**基本信息**:
- **发现日期**: 2026-03-30
- **发现阶段**: 测试阶段
- **模块**: 前端测试 - ContentList.test.js
- **负责人**: 测试工程师
- **状态**: 🟡 待改进

**问题描述**:
ContentList 的空状态测试需要 API mock 返回空数组，但当前测试没有正确设置 mock，导致测试不完整。

**复现步骤**:
1. 查看 ContentList.test.js 中的空状态测试
2. 测试依赖外部数据状态

**预期行为**:
- 测试应该完全 mock，不依赖外部数据
- 空状态应该能稳定复现

**实际行为**:
- 测试需要额外的 mock 设置
- 测试稳定性不足

**影响范围**:
- 测试可靠性
- CI/CD 稳定性

**修复建议**:
```javascript
it('空列表时应该显示空状态', async () => {
  const { contentApi } = await import('../api')
  contentApi.getAll.mockResolvedValue({ data: [] })
  
  await wrapper.vm.loadContents()
  await flushPromises()
  
  expect(wrapper.find('.van-empty').exists()).toBe(true)
  expect(wrapper.find('.van-empty').html()).toContain('暂无内容')
})
```

**优先级**: 低
**预计修复时间**: 1 小时

---

## Bug 趋势分析

### 按模块分布

```
前端组件测试    ████████████████░░  2 个 (67%)
前端业务逻辑    ████████░░░░░░░░░░  1 个 (33%)
后端            ░░░░░░░░░░░░░░░░░░  0 个 (0%)
```

### 按严重程度分布

```
P0 - 严重   ░░░░░░░░░░  0 个
P1 - 重要   ██████████  1 个
P2 - 次要   ██████████  2 个
```

---

## 修复计划

### 第一阶段（立即修复）
- [ ] BUG-001: ContentDetail 权限检查
  - 预计时间：2 小时
  - 负责人：前端开发
  - 截止日期：2026-03-31

### 第二阶段（测试迭代）
- [ ] BUG-002: PublishView 测试用例修复
  - 预计时间：30 分钟
  - 负责人：测试工程师
  - 截止日期：2026-03-30

- [ ] BUG-003: ContentList 测试改进
  - 预计时间：1 小时
  - 负责人：测试工程师
  - 截止日期：2026-03-30

---

## 已知限制

1. **E2E 测试数据依赖**
   - 当前 E2E 测试依赖真实后端数据
   - 建议：实现测试数据工厂或 mock 服务

2. **权限测试不完整**
   - 需要完整的用户认证 mock
   - 建议：实现测试用户工厂

3. **性能测试覆盖不足**
   - 当前只有基础响应时间测试
   - 建议：增加负载和压力测试

---

## 变更日志

| 日期 | 操作 | 描述 | 操作人 |
|------|------|------|--------|
| 2026-03-30 | 创建 | 初始 Bug 清单 | 测试工程师 |

---

**文档版本**: 1.0
**最后更新**: 2026-03-30
**维护人**: 测试工程师 - 内容模块
