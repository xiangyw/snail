# 🐌 Snail 项目测试文档

> 版本：1.0 | 更新日期：2026-03-30 | 测试工程师：AI Agent

---

## 📁 目录结构

```
tests/
├── README.md                 # 本文件，测试文档索引
├── TEST_CASES.md            # 测试用例清单
├── API_TEST_GUIDE.md        # API 测试指南
├── POSTMAN_COLLECTION.json  # Postman 测试集合
└── backend/                 # 后端测试代码
    └── src/test/
        ├── java/com/snail/
        │   ├── service/     # 服务层测试
        │   ├── controller/  # 控制器测试
        │   ├── repository/  # Repository 测试
        │   ├── entity/      # 实体测试
        │   ├── dto/         # DTO 测试
        │   ├── factory/     # 测试数据工厂
        │   └── integration/ # 集成测试
        └── resources/
            └── application-test.yml  # 测试配置
```

---

## 🚀 快速开始

### 1. 环境准备

确保已安装以下工具：
- Java 17+
- Maven 3.8+
- Git

### 2. 运行所有测试

```bash
cd backend
mvn clean test
```

### 3. 运行特定测试

```bash
# 运行服务层测试
mvn test -Dtest="*ServiceTest"

# 运行控制器测试
mvn test -Dtest="*ControllerTest"

# 运行集成测试
mvn test -Dtest="*IntegrationTest"

# 运行单个测试类
mvn test -Dtest=AuthServiceTest
```

### 4. 生成测试覆盖率报告

```bash
mvn clean test jacoco:report
```

报告位置：`backend/target/site/jacoco/index.html`

---

## 📋 测试文档

### 测试用例清单

详细测试用例请查看：[TEST_CASES.md](./TEST_CASES.md)

包含：
- ✅ 服务层测试（AuthService, TaskService, PointsService）
- ✅ 控制器测试（AuthController, TaskController, PointsController）
- ✅ 实体测试（User, Task, PointsAccount 等）
- ✅ DTO 测试（AuthRequest, RegisterRequest）
- ✅ Repository 测试
- ✅ 集成测试

### API 测试指南

API 测试详细说明请查看：[API_TEST_GUIDE.md](./API_TEST_GUIDE.md)

包含：
- 环境配置
- 认证流程
- API 测试用例
- 测试工具使用
- 常见问题解答

### Postman 集合

导入 Postman 集合：[POSTMAN_COLLECTION.json](./POSTMAN_COLLECTION.json)

使用方法：
1. 打开 Postman
2. 点击 Import
3. 选择 `POSTMAN_COLLECTION.json`
4. 设置环境变量 `base_url` 为 `http://localhost:8080`
5. 运行集合

---

## 🧪 测试分类

### 单元测试 (Unit Tests)

测试单个类或方法的功能。

**位置：** `src/test/java/com/snail/{service,controller,entity,dto}/`

**特点：**
- 使用 Mockito 模拟依赖
- 快速执行
- 高覆盖率目标（≥80%）

**示例：**
```java
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private AuthService authService;
    
    @Test
    void register_Success() {
        // 测试代码
    }
}
```

### 集成测试 (Integration Tests)

测试多个组件的集成。

**位置：** `src/test/java/com/snail/integration/`

**特点：**
- 使用真实数据库（H2）
- 测试完整流程
- 验证组件间交互

**示例：**
```java
@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void register_Success() throws Exception {
        // 完整注册流程测试
    }
}
```

### Repository 测试

测试数据访问层。

**位置：** `src/test/java/com/snail/repository/`

**特点：**
- 使用 `@DataJpaTest`
- 测试数据库操作
- 验证查询方法

**示例：**
```java
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void findByUsername_Found() {
        // 测试代码
    }
}
```

---

## 📊 测试覆盖率

### 覆盖率目标

| 指标 | 目标 | 状态 |
|------|------|------|
| 行覆盖率 | ≥ 80% | 🔄 |
| 分支覆盖率 | ≥ 75% | 🔄 |
| 类覆盖率 | ≥ 90% | 🔄 |
| 方法覆盖率 | ≥ 85% | 🔄 |

### 查看覆盖率

```bash
# 生成报告
mvn clean test jacoco:report

# 在浏览器中打开
open backend/target/site/jacoco/index.html
```

---

## 🔧 测试工具

### 内置工具

- **JUnit 5**: 测试框架
- **Mockito**: 模拟框架
- **Spring Boot Test**: Spring 测试支持
- **H2 Database**: 内存数据库
- **AssertJ**: 流式断言

### 外部工具

- **Postman**: API 测试
- **cURL**: 命令行测试
- **JMeter**: 性能测试（可选）

---

## 📝 测试最佳实践

### 1. 测试命名

使用描述性命名：
```java
// ✅ 好的命名
@Test
void register_Success() { }

@Test
void register_UsernameExists_ThrowsException() { }

// ❌ 避免的命名
@Test
void test1() { }

@Test
void testRegister() { }
```

### 2. 测试结构

遵循 AAA 模式（Arrange-Act-Assert）：
```java
@Test
void login_Success() {
    // Arrange
    when(authService.login(any())).thenReturn(authResponse);
    
    // Act
    AuthResponse response = authService.login(authRequest);
    
    // Assert
    assertNotNull(response);
    assertEquals("token", response.getToken());
}
```

### 3. 测试数据

使用工厂类创建测试数据：
```java
User user = UserFactory.createUser();
User admin = UserFactory.createAdminUser();
RegisterRequest request = UserFactory.createRegisterRequest();
```

### 4. 测试隔离

每个测试应该独立：
```java
@BeforeEach
void setUp() {
    // 每个测试前重置状态
    userRepository.deleteAll();
}
```

---

## 🐛 常见问题

### Q: 测试失败怎么办？

1. 查看错误信息
2. 检查测试数据
3. 验证依赖模拟
4. 运行单个测试调试

### Q: 如何调试测试？

```bash
# 使用 Maven 调试
mvn test -Dtest=AuthServiceTest -Dmaven.surefire.debug

# 在 IDE 中右键点击测试类 -> Debug
```

### Q: 测试运行太慢？

1. 使用 `@MockBean` 替代真实组件
2. 使用内存数据库 H2
3. 并行运行测试
4. 只运行相关测试

### Q: 如何测试异常？

```java
@Test
void register_UsernameExists_ThrowsException() {
    when(userRepository.existsByUsername(any())).thenReturn(true);
    
    AuthenticationException exception = assertThrows(
        AuthenticationException.class,
        () -> authService.register(request)
    );
    assertEquals("Username already exists", exception.getMessage());
}
```

---

## 📈 持续改进

### 待添加测试

- [ ] Security 配置测试
- [ ] JWT 工具类测试
- [ ] 过滤器测试
- [ ] 异常处理器测试
- [ ] 性能测试
- [ ] 安全测试
- [ ] 端到端测试

### 测试优化计划

1. 提高测试覆盖率至 85%+
2. 添加更多边界条件测试
3. 优化测试执行速度
4. 完善集成测试覆盖
5. 添加性能基准测试

---

## 📞 联系

如有测试相关问题，请联系：
- 测试负责人：待指定
- 邮箱：test@snail.com
- 文档更新：提交 PR 到 `tests/` 目录

---

*文档版本：1.0*  
*最后更新：2026-03-30*  
*维护者：Snail 测试团队*
