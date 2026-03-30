# GitHub 分支保护配置指南

**适用仓库**: https://github.com/xiangyw/snail

---

## ⚙️ 配置步骤

### 1. 推送新分支到远程

```bash
cd /home/openclaw/.openclaw/workspace/projects/snail

# 推送所有分支
git push -u origin main develop test
```

---

### 2. 配置分支保护规则

访问：`https://github.com/xiangyw/snail/settings/branches`

#### main 分支保护

1. 点击 "Add branch protection rule"
2. Branch name pattern: `main`
3. 勾选以下选项：

**Rule settings**
- ✅ Require a pull request before merging
  - ✅ Require approvals: `1`
  - ✅ Dismiss stale pull request approvals when new commits are pushed
  - ✅ Require review from Code Owners (可选)
- ✅ Require status checks to pass before merging
  - ✅ Require branches to be up to date before merging
  - Status checks that are required:
    - `frontend`
    - `backend`
- ✅ Include administrators
- ✅ Force pushes: ❌ (禁止)
- ✅ Deletions: ❌ (禁止)
- ✅ Allow linear history (推荐)
- ✅ Allow merge commits (可选)
- ✅ Allow squash merging (推荐)

4. 点击 "Create"

---

#### test 分支保护

1. 点击 "Add branch protection rule"
2. Branch name pattern: `test`
3. 勾选以下选项：

**Rule settings**
- ✅ Require a pull request before merging
  - ✅ Require approvals: `1`
- ✅ Require status checks to pass before merging
  - Status checks that are required:
    - `frontend`
    - `backend`
- ✅ Include administrators
- ✅ Force pushes: ❌
- ✅ Deletions: ❌

4. 点击 "Create"

---

#### develop 分支保护

1. 点击 "Add branch protection rule"
2. Branch name pattern: `develop`
3. 勾选以下选项：

**Rule settings**
- ✅ Require a pull request before merging
  - ✅ Require approvals: `1` (可选，宽松模式)
- ✅ Require status checks to pass before merging
- ✅ Force pushes: ❌
- ✅ Deletions: ❌

4. 点击 "Create"

---

### 3. 配置 CODEOWNERS (可选)

创建 `.github/CODEOWNERS` 文件：

```
# 默认审查者
* @xiangyw

# 前端代码
/frontend/ @frontend-team

# 后端代码
/backend/ @backend-team

# 部署配置
/deploy/ @devops-team
```

---

### 4. 验证配置

```bash
# 尝试直接 push 到 main (应该被拒绝)
git checkout main
echo "test" >> README.md
git commit -am "test: 测试直接 push"
git push

# 预期输出:
# remote: Resolving deltas: 100% (1/1)
# remote: error: GH006: Protected branch update failed for refs/heads/main.
# remote: error: You are not authorized to push to this branch.
```

---

## 📋 检查清单

- [ ] main 分支保护已启用
- [ ] test 分支保护已启用
- [ ] develop 分支保护已启用
- [ ] PR 审查要求已设置 (至少 1 人)
- [ ] CI 状态检查已配置
- [ ] 管理员也被限制 (Include administrators)
- [ ] 强制推送已禁止
- [ ] 分支删除已禁止

---

## 🔗 相关文档

- [Git 工作流规范](./GIT_WORKFLOW.md)
- [团队组织](../TEAM.md)

---

*配置完成后，所有开发人员必须遵循 PR 流程*
