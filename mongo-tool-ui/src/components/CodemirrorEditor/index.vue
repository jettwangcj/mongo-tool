<template>
  <div class="sql-editor">
    <div v-show="runLoading" class="run-loadings">
      <span class="el-icon-loading"></span>
      正在执行
    </div>
    <div class="header-tools">
        <ul class="tools-list">
          <li class="tools-item" @click="onRunCode">
            <i class="iconfont icon-video-play" style="color: #67C23A"></i>
            运行
          </li>
          <li class="tools-item" @click="onStopRun">
            <i class="iconfont icon-video-pause" style="color: red"></i>
            停止
          </li>
          <li class="tools-item" @click="onCommitOrder">
            <i class="iconfont icon-promotion "></i> <!--el-icon-s-promotion-->
            提交
          </li>
          <li class="tools-item" @click="onFormatSQL">
            <i class="iconfont icon-format" style="color: #66b1ff"></i>
            格式化
          </li>
        </ul>
    </div>
    <textarea ref="codeMirror"></textarea>
  </div>
</template>

<script>
// 核心样式
import 'codemirror/lib/codemirror.css'

// 引入主题后还需要在 options 中指定主题才会生效
import 'codemirror/addon/display/fullscreen.css' // 全屏显示编辑器
import 'codemirror/addon/display/fullscreen.js' // 全屏显示编辑器

import 'codemirror/theme/cobalt.css'
import 'codemirror/theme/eclipse.css'
import 'codemirror/theme/ayu-dark.css'
import 'codemirror/theme/idea.css'
import 'codemirror/theme/solarized.css'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/sql-hint.js'
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/edit/matchbrackets.js'
import 'codemirror/mode/sql/sql'
import CodeMirror from 'codemirror'
import { format } from 'sql-formatter'
import { mapGetters } from 'vuex'
export default {
  props: {
    value: {
      type: String,
      default: ''
    },
    runLoading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      sqlEditor: null,
      code: ''
    }
  },
  computed: {
    ...mapGetters([
      'options'
    ])
  },
  mounted() {
    this.init()
    this.setSize()
  },
  methods:{
    init() {
      this.sqlEditor = CodeMirror.fromTextArea(this.$refs.codeMirror, {
        mode: "text/x-sparksql", // spark sql模式
        lineNumbers: this.options.lineNumbers,  // 显示行号
        styleActiveLine: this.options.styleActiveLine, // 激活当前行
        theme: this.options.theme, // 主题
        lineWrapping: this.options.lineWrapping, // 自动换行
        matchBrackets: true, // 括号匹配
        autoCloseBrackets: true,
        line: true,
        extraKeys: { // 触发提示按键
          [this.options.extraKeys]: "autocomplete"
        },
        hintOptions: { // 自定义提示选项
          completeSingle: false, // 当匹配只有一项的时候是否自动补全
          tables: {} // 代码提示
        },
      })
      this.setValue()
      this.editorEvents()
    },
    setValue(sql) {
      this.sqlEditor.setValue(sql ? sql : this.value)
    },
    setSize(width = 'auto', height = '300px') {
      this.sqlEditor.setSize(width,height)
    },
    setHintOptions(tables) {
      this.sqlEditor.options.hintOptions.tables = tables
    },
    editorEvents() {
      // 设置代码提示
      this.sqlEditor.on('keyup', (cm, event) => {
        if (event.keyCode >= 65 && event.keyCode <= 90){
            cm.showHint();
        }
        //所有的字母和'$','{','.'在键按下之后都将触发自动完成  
    	  if (!cm.state.completionActive &&  
    	            ((event.keyCode >= 65 && event.keyCode <= 90 ) || event.keyCode == 52 || event.keyCode == 219 || event.keyCode == 190)) {  
    	    CodeMirror.commands.autocomplete(cm, null, {completeSingle: false});  
    	  }
      })

      // 代码输入的双向绑定
      this.sqlEditor.on('change', (editor) => {
        // 这里要用多一个载体去获取值,不然会重复赋值卡顿
        this.code = editor.getValue()
        if (this.$emit) {
          this.$emit('input', this.code)
        }
      })
    },
    onRunCode() {
      // 运行代码
      this.$emit('run', this.sqlEditor.getValue())
    },
    onStopRun() {
      // 停止执行代码
      this.$emit('stop')
    },
    onCommitOrder() {
      // 提交命令
      this.$emit('commit', this.sqlEditor.getValue())
    },
    onFormatSQL() {
      // 格式化代码
      if (this.sqlEditor.getValue().trim() === '') {
        this.$message.warning('请先编辑 SQL 命令！')
        return
      }
      const sqlCode = this.sqlEditor.getValue()
      this.sqlEditor.setValue(format(sqlCode))
    }
  }
}
</script>

<style lang="scss" scoped>
.sql-editor {
  position: relative;

  // 工具栏
  .header-tools {
      height: 40px;
      user-select: none;

      .tools-list {
        list-style: none;
        padding: 0  0 0 20px;
        margin: 0;
        display: flex;
        align-items: center;
        height: 100%;
        font-size: 12px;
        color: #666;
  
        .tools-item {
          margin: 0px 10px;
          cursor: pointer;
  
          &:hover {
            color: #999;
          }
  
          .el-icon-video-play {
            color: #4BC451;
          }
          .el-icon-video-pause {
            color: #F35353;
          }
          .el-icon-document-checked,.el-icon-s-promotion,.el-icon-document {
            color: #4381E6;
          }
        }
      }
  }

  // 加载状态
  .run-loadings {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: calc(100% - 40px);
    z-index: 999;
    background: rgba($color: #000000, $alpha: .3);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #462CF4;
    font-size: 12px;

    .el-icon-loading {
      font-size: 22px;
      margin-bottom: 10px;
      color: #462CF4;
    }
  }
}
</style>