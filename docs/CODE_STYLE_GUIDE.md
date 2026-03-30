# 🐌 Snail 代码规范文档

> 版本：v1.0.0 | 更新日期：2026-03-30 | 适用：前端/后端开发

---

## 📋 目录

1. [通用规范](#通用规范)
2. [前端代码规范](#前端代码规范)
3. [后端代码规范](#后端代码规范)
4. [数据库规范](#数据库规范)
5. [代码审查清单](#代码审查清单)

---

## 通用规范

### 命名规范

#### 文件命名

| 类型 | 规范 | 示例 |
|------|------|------|
| 前端组件 | PascalCase | `UserList.vue` |
| 前端工具 | camelCase | `stringUtils.ts` |
| 后端类 | PascalCase | `UserService.java` |
| 后端接口 | PascalCase | `UserController.java` |
| 配置文件 | kebab-case | `docker-compose.yml` |
| 脚本文件 | kebab-case | `install-all.sh` |

#### 变量命名

```typescript
// ✅ 正确
const userName = '蜗牛';
const userList = [];
const MAX_RETRY_COUNT = 3;
const isLogin = true;
const getUserInfo = () => {};

// ❌ 错误
const username = '蜗牛';        // 应使用 camelCase
const UserName = '蜗牛';        // 非常量不要用 PascalCase
const maxRetryCount = 3;        // 常量应全大写
const flag = true;              // 语义不明确
```

#### 类与接口命名

```typescript
// ✅ 正确 - 类使用 PascalCase
class UserInfo { }
interface UserResponse { }
type UserList = UserInfo[];

// ✅ 正确 - 接口加 I 前缀 (可选)
interface IUserService { }
```

### 注释规范

#### 单行注释

```java
// 单个空格后跟注释内容
// TODO: 优化查询性能
// FIXME: 修复空指针问题
// NOTE: 重要说明
```

#### 文档注释 (Java)

```java
/**
 * 用户服务类
 * 
 * <p>提供用户相关的业务逻辑处理，包括：
 * <ul>
 *   <li>用户注册</li>
 *   <li>用户登录</li>
 *   <li>资料修改</li>
 * </ul>
 * 
 * @author Snail Team
 * @since 1.0.0
 */
public class UserService {
    
    /**
     * 根据 ID 查询用户信息
     *
     * @param userId 用户 ID，不能为空
     * @return 用户信息
     * @throws UserNotFoundException 用户不存在时抛出
     */
    public UserInfo getUserById(Long userId) {
        // ...
    }
}
```

#### JSDoc (TypeScript)

```typescript
/**
 * 格式化日期
 * @param date - 日期对象
 * @param format - 格式化模板，默认 'YYYY-MM-DD'
 * @returns 格式化后的字符串
 * @example
 * ```ts
 * formatDate(new Date(), 'YYYY-MM-DD HH:mm:ss')
 * // '2026-03-30 10:30:00'
 * ```
 */
export function formatDate(date: Date, format: string = 'YYYY-MM-DD'): string {
  // ...
}
```

### 代码格式

#### 行宽限制

- **最大行宽**: 120 字符
- **推荐行宽**: 80-100 字符

#### 空行规范

```typescript
// ✅ 正确 - 逻辑块之间空一行
const user = await getUserById(id);

if (!user) {
  throw new Error('用户不存在');
}

return user;

// ❌ 错误 - 过多空行
const user = await getUserById(id);


if (!user) {

  throw new Error('用户不存在');

}
```

---

## 前端代码规范

### Vue 组件规范

#### 组件结构顺序

```vue
<script setup lang="ts">
// 1. imports
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

// 2. 组件 options (如需要)
defineOptions({
  name: 'UserList'
})

// 3. props 定义
interface Props {
  userId?: number
  showAvatar?: boolean
}
const props = withDefaults(defineProps<Props>(), {
  userId: 0,
  showAvatar: true
})

// 4. emits 定义
interface Emits {
  (e: 'update', value: number): void
  (e: 'delete', id: number): void
}
const emit = defineEmits<Emits>()

// 5. 组合式函数调用
const router = useRouter()
const userStore = useUserStore()

// 6. 响应式数据
const loading = ref(false)
const userList = ref([])

// 7. 计算属性
const totalCount = computed(() => userList.value.length)

// 8. 监听器
watch(
  () => props.userId,
  (newVal) => {
    fetchUserList()
  }
)

// 9. 生命周期
onMounted(() => {
  fetchUserList()
})

// 10. 方法
const fetchUserList = async () => {
  loading.value = true
  try {
    userList.value = await api.getUserList()
  } finally {
    loading.value = false
  }
}

const handleDelete = (id: number) => {
  emit('delete', id)
}
</script>

<template>
  <!-- 模板内容 -->
</template>

<style scoped>
/* 样式 */
</style>
```

#### Props 命名

```typescript
// ✅ 正确 - 使用 camelCase
interface Props {
  userName: string
  isActive: boolean
  itemList: Array<Item>
}

// ❌ 错误 - 不要使用 kebab-case 或 PascalCase
interface Props {
  'user-name': string  // 错误
  UserName: string     // 错误
}
```

#### 事件命名

```typescript
// ✅ 正确 - 使用 camelCase，动词开头
interface Emits {
  (e: 'update', value: number): void
  (e: 'delete', id: number): void
  (e: 'change:status', status: string): void
}

// ❌ 错误
interface Emits {
  (e: 'onUpdate', value: number): void  // 不要加 on 前缀
  (e: 'user-delete', id: number): void  // 不要用 kebab-case
}
```

### TypeScript 规范

#### 类型定义

```typescript
// ✅ 正确 - 使用 interface 定义对象类型
interface User {
  id: number
  name: string
  email?: string  // 可选属性
  roles: string[]
}

// ✅ 正确 - 使用 type 定义联合类型
type Status = 'pending' | 'active' | 'disabled'

// ✅ 正确 - 使用 enum 定义枚举
enum UserRole {
  ADMIN = 'admin',
  USER = 'user',
  GUEST = 'guest'
}

// ❌ 错误 - 避免使用 any
const data: any = {}  // 错误

// ✅ 正确 - 使用 unknown 或具体类型
const data: unknown = {}
```

#### 泛型命名

```typescript
// ✅ 正确
interface Response<T> {
  code: number
  data: T
  message: string
}

interface PageResult<T> {
  list: T[]
  total: number
}

// ❌ 错误 - 泛型名称要有意义
interface Response<X> { }  // 过于简单
```

### CSS 规范

#### 类名命名

```css
/* ✅ 正确 - 使用 kebab-case */
.user-list { }
.user-list-item { }
.user-list-item__avatar { }
.user-list-item--active { }

/* ❌ 错误 */
.userList { }           /* 不要用 camelCase */
.user_list { }          /* 不要用下划线分隔单词 */
```

#### BEM 命名规范

```css
/* Block */
.card { }

/* Element - 双下划线 */
.card__header { }
.card__body { }
.card__footer { }

/* Modifier - 双横线 */
.card--primary { }
.card--small { }
.card__button--disabled { }
```

#### 样式顺序

```css
.button {
  /* 1. 定位属性 */
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;
  
  /* 2. 盒模型 */
  display: flex;
  width: 100px;
  height: 50px;
  margin: 10px;
  padding: 5px;
  
  /* 3. 字体样式 */
  font-size: 14px;
  font-weight: bold;
  line-height: 1.5;
  
  /* 4. 文本样式 */
  text-align: center;
  color: #333;
  
  /* 5. 背景与边框 */
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  
  /* 6. 其他 */
  cursor: pointer;
  transition: all 0.3s;
}
```

### 最佳实践

#### 组件设计

```typescript
// ✅ 正确 - 单一职责
// UserList.vue - 只负责列表展示
// UserItem.vue - 只负责单项展示
// UserForm.vue - 只负责表单编辑

// ❌ 错误 - 一个组件做所有事情
// User.vue - 包含列表、详情、编辑所有功能
```

#### 状态管理

```typescript
// ✅ 正确 - 使用 Pinia
// stores/user.ts
export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null,
    token: ''
  }),
  getters: {
    isLoggedIn: (state) => !!state.token
  },
  actions: {
    async login(credentials) {
      // ...
    }
  }
})

// ❌ 错误 - 避免使用全局变量
window.userInfo = { }  // 不要这样做
```

#### API 调用

```typescript
// ✅ 正确 - 统一封装
// utils/api.ts
export const api = {
  user: {
    getList: (params) => request.get('/users', { params }),
    getById: (id) => request.get(`/users/${id}`),
    create: (data) => request.post('/users', data)
  }
}

// views/UserList.vue
const userList = await api.user.getList({ page: 1, size: 20 })

// ❌ 错误 - 直接在组件中调用
const res = await fetch('http://api.snail.com/users')
```

---

## 后端代码规范

### Java 编码规范

#### 类设计原则

```java
// ✅ 正确 - 单一职责
@Service
public class UserService {
    // 只处理用户相关业务
}

@Service
public class OrderService {
    // 只处理订单相关业务
}

// ❌ 错误 - 职责混乱
@Service
public class CommonService {
    // 包含用户、订单、支付所有逻辑
}
```

#### 异常处理

```java
// ✅ 正确 - 使用自定义异常
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND, "用户不存在：" + userId);
    }
}

// 业务层抛出
public UserInfo getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
}

// 全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public Result<Void> handleUserNotFound(UserNotFoundException e) {
        return Result.error(e.getCode(), e.getMessage());
    }
}

// ❌ 错误 - 吞掉异常
try {
    // ...
} catch (Exception e) {
    // 空 catch 块
}

// ❌ 错误 - 打印堆栈
try {
    // ...
} catch (Exception e) {
    e.printStackTrace();  // 不要这样做
}
```

#### 日志规范

```java
// ✅ 正确
@Slf4j
public class UserService {
    
    public void createUser(CreateUserRequest request) {
        log.info("创建用户，用户名：{}", request.getUsername());
        
        try {
            // 业务逻辑
            log.debug("用户创建成功，userId: {}", user.getId());
        } catch (Exception e) {
            log.error("创建用户失败，用户名：{}", request.getUsername(), e);
            throw new BusinessException("创建用户失败");
        }
    }
}

// ❌ 错误
System.out.println("用户创建成功");  // 不要使用 System.out
log.debug("用户创建成功，用户 ID:" + user.getId());  // 使用占位符
```

#### 代码分层

```java
// Controller 层 - 只处理 HTTP 相关
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public Result<UserVO> create(@Valid @RequestBody CreateUserRequest request) {
        UserVO user = userService.createUser(request);
        return Result.success(user);
    }
}

// Service 层 - 业务逻辑
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public UserVO createUser(CreateUserRequest request) {
        // 业务逻辑
        User user = convertToEntity(request);
        userRepository.save(user);
        return convertToVO(user);
    }
}

// Repository 层 - 数据访问
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByEmail(String email);
}
```

### 命名规范

#### 类命名

```java
// ✅ 正确
public class UserController { }      // Controller 后缀
public class UserService { }         // Service 后缀
public class UserRepository { }      // Repository 后缀
public class User entity { }         // 实体类 - 名词
public class CreateUserRequest { }   // 请求类
public class UserVO { }              // 视图对象
public class UserDTO { }             // 数据传输对象

// ❌ 错误
public class UserCtrl { }            // 不要用缩写
public class UserServiceImpl { }     // 实现类不需要 Impl 后缀
```

#### 方法命名

```java
// ✅ 正确 - 动词开头
public User getUserById(Long id);
public List<User> listUsers(Pageable page);
public User createUser(CreateUserRequest request);
public void updateUser(Long id, UpdateUserRequest request);
public void deleteUser(Long id);
public boolean existsByUsername(String username);

// ❌ 错误
public User user(Long id);           // 缺少动词
public List<User> getUsers(Pageable page);  // list 更简洁
```

### Spring Boot 规范

#### 配置类

```java
// ✅ 正确 - 使用@ConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "snail.jwt")
@Data
public class JwtProperties {
    
    private String secretKey;
    private Long expiration;
    private String tokenPrefix;
}

// 使用
@Service
public class JwtTokenProvider {
    
    @Autowired
    private JwtProperties properties;
    
    public String generateToken(User user) {
        // 使用 properties.getSecretKey()
    }
}

// ❌ 错误 - 避免使用@Value 分散配置
@Value("${snail.jwt.secret-key}")
private String secretKey;
```

#### 事务管理

```java
// ✅ 正确 - 在 Service 层添加事务
@Service
public class OrderService {
    
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(CreateOrderRequest request) {
        // 1. 创建订单
        Order order = new Order();
        orderRepository.save(order);
        
        // 2. 扣减库存
        productRepository.decreaseStock(request.getItems());
        
        // 3. 扣减余额
        userRepository.decreaseBalance(request.getUserId(), request.getAmount());
        
        return order;
    }
}

// ❌ 错误 - 在 Controller 层添加事务
@RestController
public class OrderController {
    
    @Transactional  // 错误位置
    public Result<Order> create(@RequestBody CreateOrderRequest request) {
        // ...
    }
}
```

### 安全规范

#### 密码加密

```java
// ✅ 正确 - 使用 BCrypt
@Service
public class AuthService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
    }
    
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

// ❌ 错误 - 明文存储
user.setPassword(request.getPassword());  // 绝对禁止
```

#### SQL 注入防护

```java
// ✅ 正确 - 使用参数化查询
@Query("SELECT u FROM User u WHERE u.username = :username")
Optional<User> findByUsername(@Param("username") String username);

// ❌ 错误 - 字符串拼接 (JPA 中)
// 避免动态拼接 SQL
```

---

## 数据库规范

### 表命名

```sql
-- ✅ 正确 - 使用复数形式，下划线分隔
CREATE TABLE users ( );
CREATE TABLE user_profiles ( );
CREATE TABLE order_items ( );

-- ❌ 错误
CREATE TABLE user ( );          -- 不要用单数
CREATE TABLE tbl_user ( );      -- 不要加 tbl 前缀
CREATE TABLE T_USER ( );        -- 不要全大写
```

### 字段命名

```sql
-- ✅ 正确
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户 ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    email VARCHAR(100) COMMENT '邮箱',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是'
);

-- ❌ 错误
CREATE TABLE users (
    userId BIGINT,              -- 不要用 camelCase
    UserName VARCHAR(50),       -- 不要大小写混用
    createTime DATETIME,        -- 统一使用 created_at
    deleteFlag TINYINT          -- 统一使用 is_deleted
);
```

### 索引规范

```sql
-- ✅ 正确 - 有意义的索引名
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE UNIQUE INDEX uk_users_email ON users(email);

-- 组合索引 - 最左前缀原则
CREATE INDEX idx_orders_status_time ON orders(status, created_at);
```

---

## 代码审查清单

### 前端审查项

- [ ] 组件是否单一职责
- [ ] Props 和 Emits 是否有类型定义
- [ ] 是否使用 TypeScript 而非 any
- [ ] API 调用是否统一封装
- [ ] 错误处理是否完善
- [ ] 是否有内存泄漏风险 (定时器、监听器清理)
- [ ] 样式是否使用 scoped
- [ ] 是否有硬编码的魔法数字
- [ ] 注释是否清晰准确
- [ ] 控制台是否有警告

### 后端审查项

- [ ] Controller 是否只处理 HTTP 逻辑
- [ ] Service 层是否有事务管理
- [ ] 异常处理是否统一
- [ ] 日志是否规范 (级别、内容)
- [ ] 是否有 SQL 注入风险
- [ ] 密码是否加密存储
- [ ] 敏感信息是否脱敏
- [ ] 接口是否有权限校验
- [ ] 参数是否有校验 (@Valid)
- [ ] 是否有 N+1 查询问题

### 通用审查项

- [ ] 代码是否符合命名规范
- [ ] 是否有重复代码
- [ ] 方法是否过长 (>50 行)
- [ ] 类是否过大 (>500 行)
- [ ] 注释是否必要且准确
- [ ] 是否有死代码
- [ ] 是否通过单元测试
- [ ] 是否有性能问题
- [ ] 是否通过 CI 检查

---

## 工具配置

### ESLint 配置 (.eslintrc.js)

```javascript
module.exports = {
  root: true,
  env: {
    browser: true,
    es2021: true,
    node: true
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    'plugin:@typescript-eslint/recommended'
  ],
  parser: 'vue-eslint-parser',
  parserOptions: {
    ecmaVersion: 'latest',
    parser: '@typescript-eslint/parser',
    sourceType: 'module'
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    '@typescript-eslint/no-explicit-any': 'warn',
    'vue/multi-word-component-names': 'off'
  }
}
```

### Checkstyle 配置

```xml
<?xml version="1.0"?>
<!DOCTYPE module PUBLIC
    "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
    "https://checkstyle.org/dtds/configuration_1_3.dtd">

<module name="Checker">
  <module name="TreeWalker">
    <module name="PackageName">
      <property name="format" value="^[a-z]+(\.[a-z][a-z0-9]*)*$"/>
    </module>
    <module name="TypeName"/>
    <module name="MethodName"/>
    <module name="ParameterName"/>
    <module name="LocalVariableName"/>
    <module name="MemberName"/>
    <module name="ConstantName"/>
  </module>
</module>
```

---

*代码规范是团队协作的基础，请严格遵守！*

*最后更新：2026-03-30*
