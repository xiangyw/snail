# 🐌 Snail API 接口文档

> OpenAPI 3.0 规范 | 版本：v1.0.0 | 更新日期：2026-03-30

---

## 📋 目录

1. [API 概览](#api-概览)
2. [认证机制](#认证机制)
3. [通用规范](#通用规范)
4. [接口详情](#接口详情)
5. [错误码](#错误码)

---

## API 概览

### 基础信息

| 项目 | 值 |
|------|-----|
| **基础 URL** | `https://api.snail.com/v1` |
| **开发环境** | `https://dev-api.snail.com/v1` |
| **测试环境** | `https://test-api.snail.com/v1` |
| **生产环境** | `https://api.snail.com/v1` |
| **协议** | HTTPS |
| **数据格式** | JSON (UTF-8) |

### 接口分类

| 模块 | 前缀 | 说明 |
|------|------|------|
| 用户认证 | `/auth` | 登录、注册、Token 管理 |
| 用户中心 | `/users` | 用户信息、资料管理 |
| 内容管理 | `/content` | 视频、直播、动态 |
| 商城 | `/shop` | 商品、订单、购物车 |
| 消息 | `/messages` | 私信、通知、系统消息 |
| 支付 | `/payment` | 支付、退款、对账 |
| 管理后台 | `/admin` | 平台管理接口 |

---

## 认证机制

### JWT Token 认证

所有需要认证的接口需在 Header 中携带 Token：

```http
Authorization: Bearer <access_token>
```

### Token 获取流程

```
1. 用户登录 → POST /auth/login
2. 返回 access_token + refresh_token
3. access_token 有效期：2 小时
4. refresh_token 有效期：7 天
5. Token 过期 → POST /auth/refresh 刷新
```

### 登录接口

```http
POST /auth/login
Content-Type: application/json

{
  "phone": "13800138000",
  "verifyCode": "123456",
  "inviteCode": "ABC123"  // 可选
}
```

**响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 7200,
    "user": {
      "id": 10001,
      "nickname": "蜗牛用户",
      "avatar": "https://cdn.snail.com/avatars/10001.jpg",
      "phone": "138****8000"
    }
  }
}
```

### 短信验证码

```http
POST /auth/sms/send
Content-Type: application/json

{
  "phone": "13800138000",
  "scene": "login"  // login | register | bind | reset
}
```

---

## 通用规范

### 请求规范

- **Content-Type**: `application/json`
- **字符编码**: UTF-8
- **时间格式**: ISO 8601 (`YYYY-MM-DD HH:mm:ss`)
- **分页参数**: `page` (页码，从 1 开始), `size` (每页数量，默认 20)

### 响应规范

```json
{
  "code": 200,
  "message": "success",
  "data": { },
  "timestamp": 1711776000000
}
```

### 分页响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [ ],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 100,
      "totalPages": 5
    }
  }
}
```

---

## 接口详情

### 🔐 认证模块 (/auth)

#### 1. 手机号登录

```http
POST /auth/login
```

**请求体：**
```json
{
  "phone": "13800138000",
  "verifyCode": "123456",
  "inviteCode": "ABC123"
}
```

**响应：** 见上方 Token 获取流程

---

#### 2. 退出登录

```http
POST /auth/logout
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "message": "退出成功"
}
```

---

#### 3. 刷新 Token

```http
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 7200
  }
}
```

---

### 👤 用户模块 (/users)

#### 1. 获取用户信息

```http
GET /users/{userId}
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "id": 10001,
    "nickname": "蜗牛用户",
    "avatar": "https://cdn.snail.com/avatars/10001.jpg",
    "gender": 1,
    "birthday": "1990-01-01",
    "signature": "这个人很懒，什么都没写",
    "followingCount": 50,
    "followerCount": 120,
    "likeCount": 300,
    "isFollowed": false,
    "isVerified": true,
    "verifiedType": "anchor"
  }
}
```

---

#### 2. 更新用户资料

```http
PUT /users/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "nickname": "新昵称",
  "avatar": "https://cdn.snail.com/avatars/new.jpg",
  "gender": 1,
  "birthday": "1990-01-01",
  "signature": "新的个性签名"
}
```

---

#### 3. 获取我的信息

```http
GET /users/me
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "id": 10001,
    "nickname": "蜗牛用户",
    "avatar": "https://cdn.snail.com/avatars/10001.jpg",
    "phone": "138****8000",
    "snailCoin": 1000,
    "snailBean": 500,
    "isRealNamed": true,
    "isAnchor": false,
    "isMerchant": false
  }
}
```

---

### 📺 直播模块 (/live)

#### 1. 获取直播列表

```http
GET /live/list
Authorization: Bearer <token>

Query Parameters:
  - page: 1
  - size: 20
  - category: all  // 分类
  - sort: hot  // hot | new | recommend
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "liveId": 5001,
        "title": "今晚 8 点福利直播",
        "cover": "https://cdn.snail.com/live/5001.jpg",
        "anchor": {
          "id": 10001,
          "nickname": "主播名称",
          "avatar": "https://cdn.snail.com/avatars/10001.jpg"
        },
        "viewerCount": 1520,
        "likeCount": 8500,
        "status": "live",
        "startTime": "2026-03-30 20:00:00"
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 100
    }
  }
}
```

---

#### 2. 进入直播间

```http
POST /live/{liveId}/enter
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "liveId": 5001,
    "rtmpUrl": "rtmp://live.snail.com/live/5001",
    "hlsUrl": "https://hls.snail.com/live/5001.m3u8",
    "chatToken": "chat_xxx_xxx",
    "giftConfig": [ ],
    "redPacketConfig": { }
  }
}
```

---

#### 3. 发送弹幕

```http
POST /live/{liveId}/chat
Authorization: Bearer <token>
Content-Type: application/json

{
  "content": "主播好！",
  "color": "#FFFFFF"
}
```

---

#### 4. 送礼物

```http
POST /live/{liveId}/gift
Authorization: Bearer <token>
Content-Type: application/json

{
  "giftId": 101,
  "quantity": 1,
  "receiverId": 10001
}
```

---

### 🛒 商城模块 (/shop)

#### 1. 商品列表

```http
GET /shop/products
Authorization: Bearer <token>

Query Parameters:
  - page: 1
  - size: 20
  - categoryId: 1
  - keyword: 搜索关键词
  - sort: default  // default | price_asc | price_desc | sales
  - priceMin: 0
  - priceMax: 1000
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "productId": 3001,
        "name": "新鲜大闸蟹 10 只装",
        "cover": "https://cdn.snail.com/products/3001.jpg",
        "images": ["url1", "url2", "url3"],
        "price": 199.00,
        "originalPrice": 299.00,
        "memberPrice": 179.00,
        "sales": 1520,
        "stock": 500,
        "shop": {
          "shopId": 2001,
          "name": "海鲜旗舰店",
          "logo": "https://cdn.snail.com/shops/2001.jpg"
        },
        "tags": ["热卖", "包邮"],
        "isLive": true,
        "liveId": 5001
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 500
    }
  }
}
```

---

#### 2. 商品详情

```http
GET /shop/products/{productId}
Authorization: Bearer <token>
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "productId": 3001,
    "name": "新鲜大闸蟹 10 只装",
    "description": "阳澄湖大闸蟹，鲜活发货",
    "detailHtml": "<div>...</div>",
    "price": 199.00,
    "originalPrice": 299.00,
    "memberPrice": 179.00,
    "sales": 1520,
    "stock": 500,
    "specs": [
      {
        "specId": 1,
        "name": "规格",
        "values": ["10 只装", "20 只装"]
      }
    ],
    "skus": [
      {
        "skuId": 30011,
        "specs": ["10 只装"],
        "price": 199.00,
        "stock": 300,
        "image": "https://cdn.snail.com/products/3001_1.jpg"
      }
    ],
    "shop": {
      "shopId": 2001,
      "name": "海鲜旗舰店",
      "rating": 4.8,
      "followerCount": 5000
    },
    "reviews": {
      "totalCount": 800,
      "goodCount": 750,
      "rating": 4.7
    }
  }
}
```

---

#### 3. 创建订单

```http
POST /shop/orders
Authorization: Bearer <token>
Content-Type: application/json

{
  "items": [
    {
      "skuId": 30011,
      "quantity": 2
    }
  ],
  "addressId": 1001,
  "couponId": null,
  "remark": "请尽快发货",
  "paymentType": "wechat"  // wechat | points | balance
}
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "orderId": 6001,
    "orderNo": "SN20260330123456",
    "totalAmount": 398.00,
    "payAmount": 398.00,
    "pointsUsed": 0,
    "paymentInfo": {
      "paymentType": "wechat",
      "wechatPayUrl": "weixin://wxpay/bizpayurl?pr=xxx"
    }
  }
}
```

---

#### 4. 订单列表

```http
GET /shop/orders
Authorization: Bearer <token>

Query Parameters:
  - page: 1
  - size: 20
  - status: all  // all | pending | paid | shipped | completed | cancelled
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "orderId": 6001,
        "orderNo": "SN20260330123456",
        "status": "paid",
        "statusText": "待发货",
        "totalAmount": 398.00,
        "payAmount": 398.00,
        "createTime": "2026-03-30 12:34:56",
        "items": [
          {
            "productId": 3001,
            "name": "新鲜大闸蟹 10 只装",
            "cover": "https://cdn.snail.com/products/3001.jpg",
            "price": 199.00,
            "quantity": 2
          }
        ],
        "shop": {
          "shopId": 2001,
          "name": "海鲜旗舰店"
        }
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 50
    }
  }
}
```

---

### 💬 消息模块 (/messages)

#### 1. 消息列表

```http
GET /messages/list
Authorization: Bearer <token>

Query Parameters:
  - page: 1
  - size: 20
  - type: all  // all | private | system | interaction
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "messageId": 8001,
        "type": "private",
        "from": {
          "userId": 10002,
          "nickname": "好友名称",
          "avatar": "https://cdn.snail.com/avatars/10002.jpg"
        },
        "content": "你好啊！",
        "isRead": false,
        "createTime": "2026-03-30 14:30:00"
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 100
    }
  }
}
```

---

#### 2. 发送私信

```http
POST /messages/private
Authorization: Bearer <token>
Content-Type: application/json

{
  "toUserId": 10002,
  "content": "你好",
  "type": "text"  // text | image | voice
}
```

---

### 🔔 通知模块 (/notifications)

#### 1. 获取通知列表

```http
GET /notifications
Authorization: Bearer <token>

Query Parameters:
  - page: 1
  - size: 20
  - type: all  // all | system | live | order
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "notificationId": 9001,
        "type": "live",
        "title": "关注的主播开播了",
        "content": "主播 蜗牛开始了直播，快来围观！",
        "extra": {
          "liveId": 5001,
          "anchorId": 10001
        },
        "isRead": false,
        "createTime": "2026-03-30 20:00:00"
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 50
    },
    "unreadCount": 5
  }
}
```

---

## 错误码

### 通用错误码

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 200 | 成功 | - |
| 400 | 请求参数错误 | 检查请求参数 |
| 401 | 未授权/Token 过期 | 重新登录或刷新 Token |
| 403 | 无权限 | 检查用户权限 |
| 404 | 资源不存在 | 检查资源 ID |
| 429 | 请求过于频繁 | 稍后重试 |
| 500 | 服务器内部错误 | 联系技术支持 |

### 业务错误码

| 错误码 | 说明 |
|--------|------|
| 1001 | 手机号格式错误 |
| 1002 | 验证码错误或已过期 |
| 1003 | 账号已被封禁 |
| 1004 | 邀请码无效 |
| 2001 | 商品已下架 |
| 2002 | 库存不足 |
| 2003 | 订单状态异常 |
| 3001 | 直播间不存在 |
| 3002 | 直播已结束 |
| 3003 | 余额不足 |
| 4001 | 消息发送失败 |

---

## 附录

### 完整 OpenAPI 规范文件

完整的 OpenAPI 3.0 YAML 文件位于：

```
docs/api/openapi.yaml
```

可通过 Swagger UI 在线查看：

```
开发环境：https://dev-api.snail.com/swagger-ui.html
测试环境：https://test-api.snail.com/swagger-ui.html
生产环境：https://api.snail.com/swagger-ui.html
```

---

*本文档由 Snail 技术团队维护 | 最后更新：2026-03-30*
