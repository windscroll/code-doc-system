<template>
  <div class="code-card">
    <div class="code-header">
      <div class="code-info">
        <span class="language-badge">{{ language || 'java' }}</span>
        <span class="copy-stat">📋 复制次数: {{ copyCount || 0 }}</span>
      </div>
      <el-button 
        type="primary" 
        size="small" 
        plain 
        @click="doCopy"
        class="copy-btn"
      >
        一键复制
      </el-button>
    </div>
    <div class="code-wrapper">
      <pre><code :class="`language-${language || 'java'}`" v-html="highlightedCode"></code></pre>
    </div>
    <div v-if="copySuccess" class="copy-tip">复制成功！</div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'

const props = defineProps({
  code: {
    type: String,
    required: true,
    default: ''
  },
  solutionId: {
    type: Number,
    required: true
  },
  copyCount: {
    type: Number,
    default: 0
  },
  language: {
    type: String,
    default: 'java'
  }
})

const copySuccess = ref(false)

const highlightedCode = computed(() => {
  if (!props.code) return ''
  try {
    const lang = props.language || 'java'
    if (hljs.getLanguage(lang)) {
      return hljs.highlight(props.code, { language: lang }).value
    }
    return hljs.highlightAuto(props.code).value
  } catch (e) {
    return props.code
  }
})

const doCopy = async () => {
  try {
    // 复制代码到剪贴板
    await navigator.clipboard.writeText(props.code)
    
    // 调用后端统计接口（防刷）
    await axios.post(`http://localhost:8080/solution/copy/${props.solutionId}`)
    
    copySuccess.value = true
    setTimeout(() => {
      copySuccess.value = false
    }, 1500)
    
    ElMessage.success('复制成功')
  } catch (e) {
    console.error('复制失败', e)
    ElMessage.error('复制失败，请重试')
  }
}
</script>

<style scoped>
.code-card {
  margin: 20px 0;
  border-radius: 12px;
  overflow: hidden;
  background: #1e1e1e;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  position: relative;
}

.code-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.code-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #2d2d2d;
  border-bottom: 1px solid #3e3e3e;
}

.code-info {
  display: flex;
  gap: 16px;
  align-items: center;
}

.language-badge {
  font-size: 12px;
  padding: 4px 10px;
  background: #409eff;
  color: white;
  border-radius: 20px;
  font-weight: 500;
}

.copy-stat {
  font-size: 12px;
  color: #a0a0a0;
}

.copy-btn {
  font-size: 12px;
  padding: 6px 14px;
}

.copy-tip {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 8px 20px;
  border-radius: 24px;
  font-size: 14px;
  z-index: 10;
  animation: fadeOut 1.5s ease forwards;
}

@keyframes fadeOut {
  0% {
    opacity: 1;
  }
  70% {
    opacity: 1;
  }
  100% {
    opacity: 0;
    visibility: hidden;
  }
}

.code-wrapper {
  position: relative;
}

:deep(pre) {
  margin: 0;
  padding: 20px;
  overflow-x: auto;
  font-family: 'Fira Code', 'Consolas', 'Monaco', monospace;
  font-size: 14px;
  line-height: 1.5;
  background: #1e1e1e;
}

:deep(code) {
  font-family: inherit;
}
</style>