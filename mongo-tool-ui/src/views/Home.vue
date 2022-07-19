<template>
  <div class="home" :style="{height: `${winHeight}px`}">
    <div class="home-container-left">
      <div class="tree-container" :style="{height: `${winHeight}px`}" v-loading="treeLoading">

        <el-tree :props="props" :load="loadNode" node-key="id"
                 @node-click="treeClick"
                 @node-contextmenu = "operationTree"
                 lazy highlight-current>
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>
              <i :class="data.icon" :style = "data.style"></i>
              {{ node.label }}
            </span>
           </span>
        </el-tree>

        <vue-context-menu :contextMenuData="contextMenuData"
                          @editConnection="editConnection"
                          @newConnection="newConnection"
                          @delConnection="delConnection"
                          @newQuery="newQuery"
                          @newDatabase="newDatabase"></vue-context-menu>

      </div>

      <create-connection :connection = "createConnectionInfo"
                         @cancel-connection="cancelConnection"
                         @create-connection-sucess="createConnectionSuccess"></create-connection>

    </div>
    <div class="home-contaioner-right">
      <div class="database-select">
        <el-select v-model="connectionValue" size = "small"  filterable @change="connectionChangeHandler" placeholder="--请选择连接--">
            <template #prefix>
                <span style="display: table-cell;vertical-align: middle;">
                  <i class="iconfont icon-mongodb" style="color: green;top: 50%;position: absolute;transform: translateY(-50%);"></i>
                </span>
            </template>
          <el-option
                  v-for="item in connectionOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
          </el-option>
        </el-select>

        <el-select v-model="databaseValue" size = "small" style = "margin-left: 1%"  filterable placeholder="--请选择数据库--">
            <template #prefix>
                <span style="display: table-cell;vertical-align: middle;">
                  <i class="iconfont icon-database" style="color:#66b1ff;top: 50%;position: absolute;transform: translateY(-50%);"></i>
                </span>
            </template>
            <el-option
                  v-for="item in databaseOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
            </el-option>
        </el-select>
      </div>
      <!-- SQL编辑器 -->
      <codemirror-editor
        ref="codemirrorEditor"
        :run-loading="runLoading"
        @input="onEditorInput"
        @run="onSQLRun"
        @stop="onSQLStop"
        @commit="onSQLCommit"
      ></codemirror-editor>
      <div class="tables-result" :style="{height: `${tablesHeight}px`}">
        <el-tabs type="border-card" style="height: 99%">
          <el-tab-pane label="信息">
            <span v-if="runType === 0" class="el-icon-info" style="color: #909399;"></span>
            <span v-if="runType === 1" class="el-icon-success" style="color: #67C23A;"></span>
            <span v-if="runType === 2" class="el-icon-warning" style="color: #E6A23C;"></span>
            <span v-if="runType === 3" class="el-icon-error" style="color: #F56C6C;"></span>
            {{ runResult }}
<!--            <template v-if="runType === 3">
              <div class="error-message"><b>message：</b>{{ errMsg.sqlMessage }}</div>
              <div class="error-message"><b>errno：</b>{{ errMsg.errno }}</div>
              <div class="error-message"><b>sql：</b>"{{ errMsg.sql }}"</div>
              <div class="error-message"><b>code：</b>{{ errMsg.code }}</div>
              <div class="error-message"><b>sqlState：</b>{{ errMsg.sqlState }}</div>
            </template>-->
          </el-tab-pane>
          <el-tab-pane label="结果">
            <el-table
              :data="tableData"
              border
              :style="{width: '100%', height: `${tablesHeight - 80}px`}">
              <el-table-column
                v-for="item in columns"
                :key="item.value"
                :prop="item.value"
                :label="item.label"
              />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

  </div>
</template>

<script>
import CreateConnection from "@/components/CreateConnection";
import CodemirrorEditor from '@/components/CodemirrorEditor'
import {  mapGetters, mapActions } from 'vuex'

import { delConnection } from '@/api'

export default {
  name: 'Home',
  components: {
    CodemirrorEditor, CreateConnection
  },
  data() {
    return {
      props: {
        label: 'name',
        children: 'childNodes',
        isLeaf: 'isLeaf',
        id: 'id'
      },
      winHeight: 0,
      tablesHeight: 0,
      foldIndex: [], // 记录展开状态的表结构，存入index
      treeData: [], // 左侧展示的表结构
      runResult: '暂无数据', // 运行结果提示
      runType: 0, // 运行结果类型，0无结果、1成功、2警告、3异常
      tableData: [], // 运行结果的表数据展示
      columns: [], // 运行的表结构，对象元素，{label: '', value: ''}
      code: '', // 实时输入的代码
      runLoading: false, // 运行加载状态
      errMsg: {
        sqlMessage: '',
        errno: '',
        sql: '',
        code: '',
        sqlState: ''
      },

      treeLoading: false,

      connectionOptions: [],
      connectionValue: '',

      databaseOptions: [],
      databaseValue: '',

      contextMenuData: {
        // the contextmenu name(@1.4.1 updated)
        menuName: "demo",
        // The coordinates of the display(菜单显示的位置)
        axis: { x: null, y: null },
        // Menu options (菜单选项)
        menulists: [ ]
      },
      // 右击连接/数据库 的当前数据
      currRightClickObj : {
        level : undefined,
        connectionId : '',
        database: ''
      },

      createConnectionInfo : {
        currentConnectionId: undefined,
        connectionDialogVisible: false,
      }
    }
  },
  created() {
    this.treeLoading = true;
    this.getDataBaseTrees({ level: 0 }).then(res => {
      const { data } = res;
      this.connectionOptions = data.map(item => ({
        value: item.connectionId,
        label: item.name,
      }));
      this.treeLoading = false
    });
  },
  mounted() {
    this.winHeight = window.innerHeight - 52
    this.tablesHeight = window.innerHeight - 392
    window.onresize = () => {
      this.winHeight = window.innerHeight - 52
      this.tablesHeight = window.innerHeight - 392
    }
  },
  computed: {
    ...mapGetters([
      'db',
      'commitsList'
    ])
  },
  methods: {
    ...mapActions('dbs', [
      'createDatabase',
      'execute'
    ]),
    ...mapActions('commits', [
      'addLogs'
    ]),
    ...mapActions('http', [
      'getTableData',
      'getDataBaseTrees',
      'getFieldNames',
      'sendSQL'
    ]),
    loadNode(node, resolve) {
      const connectionId =  node.data ? node.data.connectionId : undefined
      const dataBaseName =  node.data ? node.data.name : undefined
      this.getDataBaseTrees({ level: node.level, connectionId , dataBaseName }).then(res => {
        const { data } = res;
        const style = node.level === 0 ? 'color: green' : node.level === 1 ? 'color: #66b1ff' : 'color: #409eff'
        const icon = node.level === 0 ? 'iconfont icon-mongodb' : node.level === 1 ? 'iconfont icon-database' : 'iconfont icon-table'
        resolve(data.map(item => {

          if(node.level === 0){
            // 获取的是连接信息
            window.sessionStorage.setItem("connection-" + item.connectionId, JSON.stringify({ id: item.connectionId, name : item.name, url : item.url }))
          }
          return {
            connectionId: item.connectionId,
            icon: icon,
            name: item.name,
            style: style,
            isLeaf: item.leafed
          }

        }));
      });
    },
    treeClick(data, node){
      if(node.level === 3) {
        console.log(node, data)

      }
    },
    operationTree(event, data, node, t){
      const x = event.clientX
      const y = event.clientY
      this.contextMenuData.axis = { x, y };

      if(node.level === 1){
        // 数据库连接 右击

        this.contextMenuData.menulists = [
          { fnHandler : 'editConnection', btnName : '编辑连接' },
          { fnHandler : 'newConnection', btnName : '新建连接' },
          { fnHandler : 'delConnection', btnName : '删除连接' },
          { fnHandler : 'newQuery', btnName : '新建查询' },
          { fnHandler : 'newDatabase', btnName : '新建数据库' },
        ]
        this.currRightClickObj = { level : node.level, connectionId: data.connectionId }
      } else if(node.level === 2){
        this.contextMenuData.menulists = []
      }
    },
    connectionChangeHandler(){
        this.getDataBaseTrees({ level: 1, connectionId : this.connectionValue }).then(res => {
            const { data } = res;
            this.databaseOptions = data.map(item => ({
                value: item.name,
                label: item.name,
            }))
        });
    },
    onEditorInput(code) {
      // 编辑器输入事件
      this.code = code
    },
    onSQLRun() {
      // SQL编辑器运行事件
      this.executeSql()
    },
    onSQLStop() {
      // SQL编辑器停止运行事件
    },
    onSQLCommit() {
      // SQL编辑器提交事件
      if (this.code.trim() === '') {
        this.$message.warning('请先编辑 SQL 命令！')
        return
      }
      this.$prompt('请输入命令标题：', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValidator: (val) => {
          if (val.trim() === '') {
            return false
          }
          return true
        },
        inputErrorMessage: '命令标题不能为空'
      }).then(({ value }) => {
        if (value.trim() !== '') {
          const logs = {
            title: value,
            sql: this.code,
            createTime: Date.now()
          }
          this.addLogs(logs)
          this.$message.success('提交成功！')
        } else {
          this.$message.error('提交失败！')
        }
      }).catch(() => {
        this.$message.warning('已取消提交')
      })
    },
    async executeSql() {
      // 执行SQL语句
      if (this.code.trim() === '') {
        this.$message.warning('请先编辑 SQL 命令！')
        return
      }

      if(this.connectionValue === '' || this.databaseValue === ''){
        this.$message.warning('请先选择连接和数据库！')
        return
      }

      this.runLoading = true
      this.tableData = []
      this.columns = []
      this.runResult = '执行中...'
      this.runType = 0
      this.errMsg = {}
      const token = this.connectionValue.concat('#').concat(this.databaseValue)
      const res = await this.sendSQL({ sql: this.code, token })

      const { code } = res
      if(code === '00000'){
        const { sqlType, success, failMessage, columns, data } = res.data
        if(sqlType === 1){
          // 查询语句
          if(success){
            // 执行成功
            this.runType = 1
            this.runResult = `成功查询 ${data.length} 条数据`
            if (data.length > 0) {
              this.tableData = [...data]
              columns.forEach(column => this.columns.push({ label: column,value: column }))
            }
          } else {
            // 执行失败

          }
        } else {
          // 非查询语句

        }

      } else {
        this.runType = 3
        this.runResult = '你小子把服务器整挂了。。。。。一个字：绝。。。'
      }





     /* if (this.code.startsWith('SELECT')) {
        if (type === '1') {
          this.runType = 1
          this.runResult = `成功查询 ${result.length} 条数据`
          if (result.length > 0) {
            this.tableData = [...result]
            const obj = { ...this.tableData[0] }
            for(let key in obj) {
              this.columns.push({
                label: key,
                value: key
              })
            }
          }
        } else if (type === '2') {
          this.runType = 3
          this.runResult = '执行失败！'
          this.errMsg = {...result}
        }
      } else {
        if (type === '1') {
          this.runType = 1
          this.runResult = `执行成功！${result.affectedRows}行数据受影响`
        //  this.queryTables()
        } else if (type === '2') {
          this.runType = 3
          this.runResult = '执行失败！'
          this.errMsg = {...result}
        }
      }*/
      this.runLoading = false
    },

    cancelConnection(){
      this.createConnectionInfo = {
        connectionDialogVisible : false,
        currentConnectionId : undefined
      }
    },
    createConnectionSuccess(){
      this.createConnectionInfo = {
        connectionDialogVisible : false,
        currentConnectionId : undefined
      }
      this.$router.go(0)
    },
    editConnection() {
      this.createConnectionInfo = {
        connectionDialogVisible : true,
        currentConnectionId : this.currRightClickObj.connectionId
      }
    },
    newConnection() {
      this.createConnectionInfo = {
        connectionDialogVisible : true,
        currentConnectionId : undefined
      }
    },
    delConnection() {
      this.$confirm('是否确认删除连接?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delConnection({ connectionId : this.currRightClickObj.connectionId}).then(res => {
          const { code } = res
          if(code === '00000'){
            this.$message.success('删除成功！')
            this.$router.go(0)
          }
          console.log(res);
        });
      });
    },
    newQuery() {
      console.log(this.currRightClickObj)
    },
    newDatabase() {
      console.log(this.currRightClickObj)
    }

  }
}
</script>

<style lang="scss" scoped>
.vue-contextmenu-listWrapper {
  padding-left: 5px !important;
}
.nav-name-right {
  margin: 0px 10px !important;
}
.home {
  background-color: #F4F4F4;
  display: flex;

  // 左侧区域
  .home-container-left {
    flex: 1.1;
    height: 100%;
    background-color: #fff;
    overflow: auto;

    .el-tabs {
      user-select: none;
      & ::v-deep .el-tabs__item {
        font-size: 12px;
        padding: 0px 10px;
      }

      .tree-container {
        overflow-y: scroll;
        overflow-x: hidden;

        .tree-table {
          font-size: 12px;
          box-sizing: border-box;
          padding: 0px 10px;

          .tree-table-name {
            line-height: 30px;
            display: flex;
            align-items: center;
            cursor: pointer;

            .tree-fold-btn {
              font-size: 14px;
              color: #666;
              display: inline-block;
              height: 10px;
              padding: 0px 4px;
              margin-right: 5px;
              cursor: pointer;
            }
          }

          .tree-table-field {
            line-height: 30px;
            box-sizing: border-box;
            padding-left: 30px;
            color: #777;
            display: flex;
            align-items: center;

            .el-icon-document-copy {
              margin-left: 5px;
              cursor: pointer;

              &:hover {
                color: #409EFF;
              }
            }
          }
        }
      }
      .tree-container::-webkit-scrollbar {
        width: 3px;
      }
      .tree-container::-webkit-scrollbar-thumb {
        border-radius: 10px;
        -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
        background: rgba(0, 0, 0, 0.1);
      }

      // 命令集
      /*.order-list {
        list-style: none;
        padding: 0;
        margin: 0;

        li {
          font-size: 12px;
          margin: 10px 0px;
          box-sizing: border-box;
          padding: 5px 15px;
          cursor: pointer;
          display: flex;
          align-items: center;

          &:hover {
            background: #E8F2FF;
          }

          img {
            width: 22px;
            margin-right: 5px;
          }
        }
      }*/
    }
  }

  // 右侧区域
  .home-contaioner-right {
    flex: 6;
    height: 100%;

    .database-select {
      margin-left: 2%;
      margin-top: 1%;
    }
    // 工具栏
    .header-tools {
      height: 40px;

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

    // 运行结果
    .tables-result {
      width: 100%;
      box-sizing: border-box;
      padding: 5px 0 0 1px;

      .el-tabs {
        .el-tab-pane {
          font-size: 12px;

          .error-message {
            margin: 8px 0px;
            box-sizing: border-box;
            padding-left: 14px;
          }
        }

        .el-table {
          // border: 1px solid red;
          height: 100px;
          overflow-y: scroll;
          font-size: 12px;
        }
        .el-table::-webkit-scrollbar {
          width: 5px;
        }
        .el-table::-webkit-scrollbar-thumb {
          border-radius: 10px;
          -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
          background: rgba(0, 0, 0, 0.2);
        }
      }
    }
  }

}
</style>
