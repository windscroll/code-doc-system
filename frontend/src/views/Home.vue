<template>
  <div class="app" :class="theme">
    <div class="side">
      <div class="side-header">
        <h3>📁 代码题库</h3>
      </div>
      <el-tree 
        :data="tree" 
        @node-click="clickNode" 
        :default-expand-all="true"
        :props="{ label: 'name', children: 'children' }"
      />
    </div>
    <div class="main">
      <div class="top-bar">
        <div class="breadcrumb">
          <span v-for="(item, idx) in breadcrumb" :key="idx">
            <span v-if="idx > 0"> / </span>
            <span class="bread-item" @click="jumpTo(item)">{{ item.name }}</span>
          </span>
        </div>
        <div class="actions">
          <el-button :icon="theme === 'dark' ? '🌞' : '🌙'" circle @click="toggleTheme" />
          <el-button v-if="!isAdmin" type="primary" plain size="small" @click="loginShow = true">登录</el-button>
          <span v-else class="admin-badge">管理员: {{ adminName }}</span>
        </div>
      </div>
      
      <div v-if="doc" class="doc-container">
        <div class="doc-header">
          <h2>{{ doc.title }}</h2>
          <el-button v-if="isAdmin" type="warning" size="small" @click="editDoc">编辑题目</el-button>
        </div>
        <div v-if="doc.content" class="doc-content" v-html="doc.content"></div>
        <div class="solutions">
          <h3>解题方法</h3>
          <CodeBlock 
            v-for="s in doc.solutions" 
            :key="s.id"
            :code="s.codeContent"
            :solution-id="s.id"
            :copy-count="s.copyCount"
            :language="s.language"
          />
        </div>
      </div>
      <div v-else class="empty-state">
        <p>👈 从左侧选择一个题目查看</p>
      </div>
    </div>

    <el-dialog v-model="loginShow" title="管理员登录" width="380px">
      <el-form :model="loginForm" label-width="60px">
        <el-form-item label="账号">
          <el-input v-model="loginForm.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="loginShow = false">取消</el-button>
        <el-button type="primary" @click="login">登录</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import CodeBlock from '../components/CodeBlock.vue'

// ✅ 这一行我已经帮你改好了
axios.defaults.baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const tree = ref([])
const doc = ref(null)
const theme = ref('light')
const isAdmin = ref(false)
const adminName = ref('')
const loginShow = ref(false)
const breadcrumb = ref([])

const loginForm = ref({ username: '', password: '' })

const loadTree = async () => {
  try {
    const res = await axios.get('/folder/tree')
    tree.value = res.data.data || []
  } catch (e) {
    console.error('加载树失败', e)
  }
}

const toggleTheme = () => {
  theme.value = theme.value === 'light' ? 'dark' : 'light'
  localStorage.setItem('theme', theme.value)
  document.documentElement.setAttribute('data-theme', theme.value)
}

const clickNode = (node) => {
  if (node.solutions) {
    doc.value = node
    breadcrumb.value = [{ name: node.title, id: node.id }]
  }
}

const jumpTo = (item) => {
  if (item.solutions) {
    doc.value = item
  }
}

const login = async () => {
  try {
    await axios.post('/admin/login', loginForm.value)
    isAdmin.value = true
    adminName.value = loginForm.value.username
    loginShow.value = false
    ElMessage.success('登录成功')
  } catch (e) {
    ElMessage.error('登录失败，请检查账号密码')
  }
}

const editDoc = () => {
  ElMessage.info('编辑功能开发中')
}

onMounted(() => {
  loadTree()
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    theme.value = savedTheme
    document.documentElement.setAttribute('data-theme', savedTheme)
  }
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.app {
  display: flex;
  height: 100vh;
  width: 100%;
}

.side {
  width: 300px;
  border-right: 1px solid var(--border);
  background: var(--bg-side);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.side-header {
  padding: 16px;
  border-bottom: 1px solid var(--border);
}

.side-header h3 {
  font-size: 16px;
  font-weight: 500;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  background: var(--bg-main);
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: var(--bg-top);
  border-bottom: 1px solid var(--border);
  position: sticky;
  top: 0;
  z-index: 100;
}

.breadcrumb {
  font-size: 14px;
  color: var(--text-light);
}

.bread-item {
  cursor: pointer;
}

.bread-item:hover {
  color: #409eff;
  text-decoration: underline;
}

.actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.admin-badge {
  font-size: 12px;
  color: #67c23a;
  background: rgba(103, 194, 58, 0.1);
  padding: 4px 12px;
  border-radius: 20px;
}

.doc-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px;
  width: 100%;
}

.doc-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--border);
}

.doc-header h2 {
  font-size: 24px;
  font-weight: 600;
}

.doc-content {
  padding: 16px;
  background: var(--bg-card);
  border-radius: 12px;
  margin-bottom: 24px;
  line-height: 1.6;
  color: var(--text);
}

.solutions h3 {
  margin-bottom: 16px;
  font-size: 18px;
  font-weight: 500;
}

.empty-state {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--text-light);
  font-size: 16px;
}

/* 主题变量 */
[data-theme="light"] {
  --bg-side: #f8f9fa;
  --bg-main: #ffffff;
  --bg-top: #ffffff;
  --bg-card: #f8f9fa;
  --border: #e9ecef;
  --text: #212529;
  --text-light: #6c757d;
}

[data-theme="dark"] {
  --bg-side: #1a1a2e;
  --bg-main: #16213e;
  --bg-top: #1a1a2e;
  --bg-card: #0f3460;
  --border: #2c3e50;
  --text: #e8e8e8;
  --text-light: #a0a0a0;
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: var(--border);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: #c0c0c0;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a0a0a0;
}
</style>