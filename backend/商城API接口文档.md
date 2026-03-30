# Snail 商城API接口文档

## 1. 商品管理接口

### 1.1 获取商品列表
- **接口路径**: `GET /api/products`
- **功能描述**: 分页获取商品列表
- **请求参数**:
  - `page`: 页码，默认为0
  - `size`: 每页大小，默认为10
- **返回示例**:
```json
{
  "content": [
    {
      "id": 1,
      "name": "商品名称",
      "description": "商品描述",
      "price": 99.99,
      "stock": 100,
      "imageUrl": "http://example.com/image.jpg",
      "categoryId": 1,
      "isActive": true,
      "createdAt": "2023-01-01T10:00:00",
      "updatedAt": "2023-01-01T10:00:00"
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0
}
```

### 1.2 获取商品详情
- **接口路径**: `GET /api/products/{id}`
- **功能描述**: 根据ID获取商品详细信息
- **路径参数**:
  - `id`: 商品ID
- **返回示例**:
```json
{
  "id": 1,
  "name": "商品名称",
  "description": "商品描述",
  "price": 99.99,
  "stock": 100,
  "imageUrl": "http://example.com/image.jpg",
  "categoryId": 1,
  "isActive": true,
  "createdAt": "2023-01-01T10:00:00",
  "updatedAt": "2023-01-01T10:00:00"
}
```

### 1.3 搜索商品
- **接口路径**: `GET /api/products/search`
- **功能描述**: 根据关键词搜索商品
- **请求参数**:
  - `keyword`: 搜索关键词
  - `page`: 页码，默认为0
  - `size`: 每页大小，默认为10
- **返回示例**: 同商品列表

### 1.4 按分类获取商品
- **接口路径**: `GET /api/products/category/{categoryId}`
- **功能描述**: 根据分类ID获取商品列表
- **路径参数**:
  - `categoryId`: 分类ID
- **请求参数**:
  - `page`: 页码，默认为0
  - `size`: 每页大小，默认为10
- **返回示例**: 同商品列表

### 1.5 按价格范围获取商品
- **接口路径**: `GET /api/products/price-range`
- **功能描述**: 根据价格区间获取商品列表
- **请求参数**:
  - `minPrice`: 最低价格（可选）
  - `maxPrice`: 最高价格（可选）
  - `page`: 页码，默认为0
  - `size`: 每页大小，默认为10
- **返回示例**: 同商品列表

### 1.6 创建商品
- **接口路径**: `POST /api/products`
- **功能描述**: 创建新商品
- **请求体**:
```json
{
  "name": "商品名称",
  "description": "商品描述",
  "price": 99.99,
  "stock": 100,
  "imageUrl": "http://example.com/image.jpg",
  "categoryId": 1,
  "isActive": true
}
```
- **返回示例**: 成功的商品信息

### 1.7 更新商品
- **接口路径**: `PUT /api/products/{id}`
- **功能描述**: 更新商品信息
- **路径参数**:
  - `id`: 商品ID
- **请求体**: 同创建商品
- **返回示例**: 更新后的商品信息

### 1.8 删除商品
- **接口路径**: `DELETE /api/products/{id}`
- **功能描述**: 删除商品
- **路径参数**:
  - `id`: 商品ID
- **返回状态**: 204 No Content

## 2. 购物车管理接口

### 2.1 获取购物车
- **接口路径**: `GET /api/cart`
- **功能描述**: 获取用户的购物车信息
- **请求参数**:
  - `userId`: 用户ID
- **返回示例**:
```json
{
  "id": 1,
  "userId": 1,
  "items": [
    {
      "id": 1,
      "product": {
        "id": 1,
        "name": "商品名称",
        "price": 99.99,
        "stock": 100
      },
      "quantity": 2,
      "price": 99.99,
      "subtotal": 199.98
    }
  ],
  "totalCount": 2,
  "totalAmount": 199.98,
  "createdAt": "2023-01-01T10:00:00",
  "updatedAt": "2023-01-01T10:00:00"
}
```

### 2.2 添加到购物车
- **接口路径**: `POST /api/cart/add`
- **功能描述**: 将商品添加到购物车
- **请求参数**:
  - `userId`: 用户ID
- **请求体**:
```json
{
  "productId": 1,
  "quantity": 1
}
```
- **返回示例**: 更新后的购物车信息

### 2.3 更新购物车项
- **接口路径**: `PUT /api/cart/item/{cartItemId}`
- **功能描述**: 更新购物车中某项商品的数量
- **请求参数**:
  - `userId`: 用户ID
  - `cartItemId`: 购物车项ID
- **请求体**:
```json
{
  "quantity": 3
}
```
- **返回示例**: 更新后的购物车信息

### 2.4 从购物车移除商品
- **接口路径**: `DELETE /api/cart/item/{cartItemId}`
- **功能描述**: 从购物车中移除指定商品
- **请求参数**:
  - `userId`: 用户ID
  - `cartItemId`: 购物车项ID
- **返回示例**: 更新后的购物车信息

### 2.5 清空购物车
- **接口路径**: `DELETE /api/cart/clear`
- **功能描述**: 清空用户的购物车
- **请求参数**:
  - `userId`: 用户ID
- **返回状态**: 204 No Content

### 2.6 获取购物车商品数量
- **接口路径**: `GET /api/cart/count`
- **功能描述**: 获取购物车中商品的数量
- **请求参数**:
  - `userId`: 用户ID
- **返回示例**:
```json
{
  "count": 5
}
```

## 3. 订单管理接口

### 3.1 创建订单
- **接口路径**: `POST /api/orders`
- **功能描述**: 创建新订单
- **请求参数**:
  - `userId`: 用户ID
- **请求体**:
```json
{
  "shippingAddress": "收货地址",
  "phoneNumber": "联系电话",
  "receiverName": "收货人姓名",
  "cartItemIds": [1, 2, 3],  // 从购物车创建订单时使用
  "productItems": [           // 直接购买时使用
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```
- **返回示例**:
```json
{
  "id": 1,
  "userId": 1,
  "orderNumber": "SNAIL1234567890",
  "status": "PENDING",
  "totalAmount": 199.98,
  "shippingAddress": "收货地址",
  "phoneNumber": "联系电话",
  "receiverName": "收货人姓名",
  "items": [
    {
      "id": 1,
      "product": {
        "id": 1,
        "name": "商品名称",
        "price": 99.99
      },
      "quantity": 2,
      "price": 99.99,
      "subtotal": 199.98
    }
  ],
  "createdAt": "2023-01-01T10:00:00",
  "updatedAt": "2023-01-01T10:00:00"
}
```

### 3.2 获取用户订单列表
- **接口路径**: `GET /api/orders`
- **功能描述**: 获取用户的订单列表
- **请求参数**:
  - `userId`: 用户ID
  - `page`: 页码，默认为0
  - `size`: 每页大小，默认为10
- **返回示例**:
```json
{
  "content": [
    {
      "id": 1,
      "orderNumber": "SNAIL1234567890",
      "status": "PENDING",
      "totalAmount": 199.98,
      "createdAt": "2023-01-01T10:00:00"
    }
  ],
  "totalElements": 10,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

### 3.3 获取订单详情
- **接口路径**: `GET /api/orders/{orderId}`
- **功能描述**: 获取订单详细信息
- **请求参数**:
  - `userId`: 用户ID
  - `orderId`: 订单ID
- **返回示例**: 同创建订单返回格式

### 3.4 根据订单号获取订单
- **接口路径**: `GET /api/orders/number/{orderNumber}`
- **功能描述**: 根据订单号获取订单详细信息
- **请求参数**:
  - `userId`: 用户ID
  - `orderNumber`: 订单号
- **返回示例**: 同创建订单返回格式

### 3.5 取消订单
- **接口路径**: `PUT /api/orders/{orderId}/cancel`
- **功能描述**: 取消订单
- **请求参数**:
  - `userId`: 用户ID
  - `orderId`: 订单ID
- **返回示例**: 取消后的订单信息

### 3.6 更新订单状态（管理员）
- **接口路径**: `PUT /api/orders/{orderId}/status`
- **功能描述**: 管理员更新订单状态
- **路径参数**:
  - `orderId`: 订单ID
- **请求参数**:
  - `status`: 新的订单状态（PENDING, PAID, SHIPPED, DELIVERED, CANCELLED, REFUNDED）
- **返回示例**: 更新后的订单信息

## 错误码说明

| 错误码 | 描述 |
|--------|------|
| 400 | 请求参数错误 |
| 401 | 未授权访问 |
| 403 | 无权限操作 |
| 404 | 资源不存在 |
| 409 | 冲突（如库存不足） |
| 500 | 服务器内部错误 |

## 订单状态说明

- `PENDING`: 待支付
- `PAID`: 已支付
- `SHIPPED`: 已发货
- `DELIVERED`: 已收货
- `CANCELLED`: 已取消
- `REFUNDED`: 已退款