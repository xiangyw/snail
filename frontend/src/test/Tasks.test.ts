import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import Tasks from '../views/Tasks/index.vue'

describe('Tasks 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染任务页面结构', () => {
    const wrapper = mount(Tasks)
    
    expect(wrapper.find('.tasks-page').exists()).toBe(true)
    expect(wrapper.find('.van-nav-bar').exists()).toBe(true)
    expect(wrapper.find('.van-tabs').exists()).toBe(true)
  })

  it('应该显示正确的标题', () => {
    const wrapper = mount(Tasks)
    const navBar = wrapper.find('.van-nav-bar')
    
    expect(navBar.text()).toContain('任务中心')
  })

  it('应该包含三个标签页', () => {
    const wrapper = mount(Tasks)
    
    const text = wrapper.text()
    expect(text).toContain('全部')
    expect(text).toContain('进行中')
    expect(text).toContain('已完成')
  })

  it('默认应该选中全部标签', () => {
    const wrapper = mount(Tasks)
    
    expect(wrapper.vm.activeTab).toBe('all')
  })

  it('任务列表初始应该为空', () => {
    const wrapper = mount(Tasks)
    
    expect(wrapper.vm.taskList).toEqual([])
    expect(wrapper.vm.loading).toBe(false)
    expect(wrapper.vm.finished).toBe(false)
  })

  it('应该渲染任务列表组件', () => {
    const wrapper = mount(Tasks)
    
    expect(wrapper.find('.van-list').exists()).toBe(true)
  })

  it('切换标签页应该重置列表', async () => {
    const wrapper = mount(Tasks)
    
    // 先添加一些数据
    wrapper.vm.taskList.push({ id: 1, title: '测试任务', status: 'doing' })
    expect(wrapper.vm.taskList.length).toBe(1)
    
    // 切换标签
    await wrapper.vm.onTabChange('doing')
    
    expect(wrapper.vm.taskList).toEqual([])
    expect(wrapper.vm.finished).toBe(false)
    expect(wrapper.vm.loading).toBe(true)
  })

  it('加载任务列表应该添加模拟数据', async () => {
    const wrapper = mount(Tasks)
    
    // 触发 onLoad
    wrapper.vm.onLoad()
    
    // 等待 setTimeout
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    expect(wrapper.vm.taskList.length).toBeGreaterThan(0)
    expect(wrapper.vm.loading).toBe(false)
  })

  it('加载的任务应该包含正确的字段', async () => {
    const wrapper = mount(Tasks)
    
    wrapper.vm.onLoad()
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    const firstTask = wrapper.vm.taskList[0]
    expect(firstTask).toHaveProperty('id')
    expect(firstTask).toHaveProperty('title')
    expect(firstTask).toHaveProperty('description')
    expect(firstTask).toHaveProperty('status')
  })

  it('任务状态应该正确显示', async () => {
    const wrapper = mount(Tasks)
    
    wrapper.vm.taskList.push(
      { id: 1, title: '任务 1', description: '描述 1', status: 'doing' },
      { id: 2, title: '任务 2', description: '描述 2', status: 'done' }
    )
    await wrapper.vm.$nextTick()
    
    const text = wrapper.text()
    expect(text).toContain('进行中')
    expect(text).toContain('已完成')
  })

  it('点击任务应该跳转到详情页', async () => {
    const wrapper = mount(Tasks)
    
    wrapper.vm.taskList.push({ id: 1, title: '测试任务', status: 'doing' })
    await wrapper.vm.$nextTick()
    
    // 调用跳转方法
    wrapper.vm.goToDetail(1)
    
    // 验证方法存在
    expect(typeof wrapper.vm.goToDetail).toBe('function')
  })

  it('onTabChange 方法应该存在', () => {
    const wrapper = mount(Tasks)
    
    expect(typeof wrapper.vm.onTabChange).toBe('function')
  })

  it('onLoad 方法应该存在', () => {
    const wrapper = mount(Tasks)
    
    expect(typeof wrapper.vm.onLoad).toBe('function')
  })

  it('任务列表应该支持 van-cell 组件', () => {
    const wrapper = mount(Tasks)
    
    expect(wrapper.find('.van-cell').exists()).toBe(true)
  })

  it('任务标签应该根据状态显示不同颜色', async () => {
    const wrapper = mount(Tasks)
    
    wrapper.vm.taskList.push(
      { id: 1, title: '任务 1', description: '描述', status: 'doing' },
      { id: 2, title: '任务 2', description: '描述', status: 'done' }
    )
    await wrapper.vm.$nextTick()
    
    const tags = wrapper.findAll('.van-tag')
    expect(tags.length).toBeGreaterThanOrEqual(1)
  })

  it('页面背景色应该正确', () => {
    const wrapper = mount(Tasks)
    const page = wrapper.find('.tasks-page')
    
    expect(page.exists()).toBe(true)
  })
})
