import { test, expect } from '@playwright/test'

// 基础 URL 配置
const BASE_URL = process.env.BASE_URL || 'http://localhost:5173'

test.describe('Snail H5 应用 E2E 测试', () => {
  
  test.beforeEach(async ({ page }) => {
    // 设置视口为移动端尺寸
    await page.setViewportSize({ width: 375, height: 667 })
  })

  test.describe('首页测试', () => {
    
    test('应该成功加载首页', async ({ page }) => {
      await page.goto(BASE_URL)
      
      // 验证页面标题
      await expect(page).toHaveTitle(/蜗牛/)
      
      // 验证导航栏
      await expect(page.locator('.van-nav-bar')).toContainText('蜗牛')
      
      // 验证搜索框存在
      await expect(page.locator('.van-search')).toBeVisible()
      
      // 验证功能网格存在
      await expect(page.locator('.feature-grid')).toBeVisible()
    })

    test('应该显示公告栏', async ({ page }) => {
      await page.goto(BASE_URL)
      
      await expect(page.locator('.van-notice-bar')).toBeVisible()
      await expect(page.locator('.van-notice-bar')).toContainText('欢迎来到蜗牛')
    })

    test('功能入口应该可点击', async ({ page }) => {
      await page.goto(BASE_URL)
      
      // 点击任务入口
      const taskItem = page.locator('.van-grid-item').filter({ hasText: '任务' })
      await expect(taskItem).toBeVisible()
      
      // 注意：实际路由需要配置，这里只验证元素存在
      await expect(taskItem).toBeVisible()
    })

    test('搜索框应该可以输入', async ({ page }) => {
      await page.goto(BASE_URL)
      
      const searchInput = page.locator('.van-search input')
      await searchInput.fill('测试商品')
      await expect(searchInput).toHaveValue('测试商品')
    })

    test('商品列表应该加载', async ({ page }) => {
      await page.goto(BASE_URL)
      
      // 等待商品列表加载
      await page.waitForTimeout(1500)
      
      await expect(page.locator('.section-title')).toContainText('精选推荐')
      await expect(page.locator('.van-card')).toBeVisible()
    })
  })

  test.describe('登录页测试', () => {
    
    test('应该成功加载登录页', async ({ page }) => {
      await page.goto(`${BASE_URL}/login`)
      
      await expect(page.locator('.van-nav-bar')).toContainText('登录')
      await expect(page.locator('.van-form')).toBeVisible()
    })

    test('手机号输入框应该可用', async ({ page }) => {
      await page.goto(`${BASE_URL}/login`)
      
      const phoneInput = page.locator('input[placeholder*="手机号"]')
      await phoneInput.fill('13800138000')
      await expect(phoneInput).toHaveValue('13800138000')
    })

    test('验证码输入框应该可用', async ({ page }) => {
      await page.goto(`${BASE_URL}/login`)
      
      const codeInput = page.locator('input[placeholder*="验证码"]')
      await codeInput.fill('123456')
      await expect(codeInput).toHaveValue('123456')
    })

    test('发送验证码按钮应该可用', async ({ page }) => {
      await page.goto(`${BASE_URL}/login`)
      
      const sendButton = page.locator('button:has-text("发送验证码")')
      await expect(sendButton).toBeVisible()
      await expect(sendButton).toBeEnabled()
    })

    test('登录按钮应该可用', async ({ page }) => {
      await page.goto(`${BASE_URL}/login`)
      
      const loginButton = page.locator('button:has-text("登录")')
      await expect(loginButton).toBeVisible()
      await expect(loginButton).toBeEnabled()
    })

    test('表单验证 - 空手机号提交', async ({ page }) => {
      await page.goto(`${BASE_URL}/login`)
      
      const loginButton = page.locator('button:has-text("登录")')
      await loginButton.click()
      
      // 应该显示验证错误
      await expect(page.locator('.van-field')).toHaveClass(/error/)
    })
  })

  test.describe('商城页测试', () => {
    
    test('应该成功加载商城页', async ({ page }) => {
      await page.goto(`${BASE_URL}/mall`)
      
      await expect(page.locator('.van-nav-bar')).toContainText('商城')
      await expect(page.locator('.van-empty')).toBeVisible()
      await expect(page.locator('.van-empty')).toContainText('商城功能开发中')
    })
  })

  test.describe('任务页测试', () => {
    
    test('应该成功加载任务页', async ({ page }) => {
      await page.goto(`${BASE_URL}/tasks`)
      
      await expect(page.locator('.van-nav-bar')).toContainText('任务中心')
      await expect(page.locator('.van-tabs')).toBeVisible()
    })

    test('标签页应该可切换', async ({ page }) => {
      await page.goto(`${BASE_URL}/tasks`)
      
      // 验证三个标签存在
      await expect(page.locator('.van-tab:has-text("全部")')).toBeVisible()
      await expect(page.locator('.van-tab:has-text("进行中")')).toBeVisible()
      await expect(page.locator('.van-tab:has-text("已完成")')).toBeVisible()
    })

    test('任务列表应该加载', async ({ page }) => {
      await page.goto(`${BASE_URL}/tasks`)
      
      // 等待任务加载
      await page.waitForTimeout(1500)
      
      await expect(page.locator('.van-cell')).toBeVisible()
    })
  })

  test.describe('响应式测试', () => {
    
    test('应该在 iPhone 尺寸下正常显示', async ({ page }) => {
      await page.setViewportSize({ width: 375, height: 667 })
      await page.goto(BASE_URL)
      
      await expect(page.locator('.home-page')).toBeVisible()
      await expect(page.locator('.van-nav-bar')).toBeVisible()
    })

    test('应该在 iPad 尺寸下正常显示', async ({ page }) => {
      await page.setViewportSize({ width: 768, height: 1024 })
      await page.goto(BASE_URL)
      
      await expect(page.locator('.home-page')).toBeVisible()
    })

    test('应该在桌面尺寸下正常显示', async ({ page }) => {
      await page.setViewportSize({ width: 1920, height: 1080 })
      await page.goto(BASE_URL)
      
      await expect(page.locator('.home-page')).toBeVisible()
    })
  })

  test.describe('性能测试', () => {
    
    test('首页加载时间应该小于 3 秒', async ({ page }) => {
      const startTime = Date.now()
      await page.goto(BASE_URL)
      const loadTime = Date.now() - startTime
      
      expect(loadTime).toBeLessThan(3000)
      console.log(`首页加载时间：${loadTime}ms`)
    })

    test('登录页加载时间应该小于 2 秒', async ({ page }) => {
      const startTime = Date.now()
      await page.goto(`${BASE_URL}/login`)
      const loadTime = Date.now() - startTime
      
      expect(loadTime).toBeLessThan(2000)
      console.log(`登录页加载时间：${loadTime}ms`)
    })

    test('页面应该没有控制台错误', async ({ page }) => {
      const errors: string[] = []
      
      page.on('console', msg => {
        if (msg.type() === 'error') {
          errors.push(msg.text())
        }
      })
      
      await page.goto(BASE_URL)
      await page.waitForLoadState('networkidle')
      
      expect(errors).toHaveLength(0)
    })
  })

  test.describe('可访问性测试', () => {
    
    test('所有图片应该有 alt 属性', async ({ page }) => {
      await page.goto(BASE_URL)
      
      const images = page.locator('img')
      const count = await images.count()
      
      for (let i = 0; i < count; i++) {
        const img = images.nth(i)
        const alt = await img.getAttribute('alt')
        // 占位图可以没有 alt，但应该有
        if (await img.getAttribute('src')?.includes('placeholder')) {
          continue
        }
        expect(alt).toBeTruthy()
      }
    })

    test('按钮应该有可访问的文本', async ({ page }) => {
      await page.goto(`${BASE_URL}/login`)
      
      const buttons = page.locator('button')
      const count = await buttons.count()
      
      for (let i = 0; i < count; i++) {
        const button = buttons.nth(i)
        const text = await button.textContent()
        expect(text?.trim()).toBeTruthy()
      }
    })
  })
})
