import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Login from '../views/Login/index.vue'
import { showToast } from 'vant'

describe('Login 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染页面结构', () => {
    const wrapper = mount(Login)
    
    expect(wrapper.find('.login-page').exists()).toBe(true)
  })

  it('组件应该有正确的初始状态', () => {
    const wrapper = mount(Login)
    
    expect(wrapper.vm.phone).toBe('')
    expect(wrapper.vm.code).toBe('')
  })

  it('sendCode 方法应该存在', () => {
    const wrapper = mount(Login)
    
    expect(typeof wrapper.vm.sendCode).toBe('function')
  })

  it('onSubmit 方法应该存在', () => {
    const wrapper = mount(Login)
    
    expect(typeof wrapper.vm.onSubmit).toBe('function')
  })

  it('sendCode - 手机号为空时应该提示', async () => {
    const wrapper = mount(Login)
    
    wrapper.vm.phone = ''
    wrapper.vm.sendCode()
    
    expect(showToast).toHaveBeenCalledWith('请输入手机号')
  })

  it('sendCode - 手机号有效时应该发送验证码', async () => {
    const wrapper = mount(Login)
    
    wrapper.vm.phone = '13800138000'
    wrapper.vm.sendCode()
    
    expect(showToast).toHaveBeenCalledWith('验证码已发送')
  })

  it('onSubmit 应该显示登录成功', async () => {
    const wrapper = mount(Login)
    const backSpy = vi.fn()
    wrapper.vm.$router.back = backSpy
    
    wrapper.vm.phone = '13800138000'
    wrapper.vm.code = '123456'
    wrapper.vm.onSubmit()
    
    expect(showToast).toHaveBeenCalledWith('登录成功')
    expect(backSpy).toHaveBeenCalled()
  })

  it('组件应该使用 vue-router', () => {
    const wrapper = mount(Login)
    
    expect(wrapper.vm.$router).toBeDefined()
  })

  it('phone 应该是响应式数据', () => {
    const wrapper = mount(Login)
    
    wrapper.vm.phone = '13900139000'
    expect(wrapper.vm.phone).toBe('13900139000')
  })

  it('code 应该是响应式数据', () => {
    const wrapper = mount(Login)
    
    wrapper.vm.code = '654321'
    expect(wrapper.vm.code).toBe('654321')
  })
})
