<template>
  <div class="home" :style="{height: `${winHeight}px`}">
    <div class="home-container-left">
      <div class="tree-container" :style="{height: `${winHeight}px`}">

        <el-tree :props="props" :load="loadNode" node-key="id"
                 lazy highlight-current>
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>
              <i class="el-icon-document"></i>
              {{ node.label }}
            </span>
           </span>
        </el-tree>

      </div>

      <!--<el-tabs tab-position="left" :style="{height: `${winHeight}px`}">
        <el-tab-pane label="表结构">
          <div class="tree-container" :style="{height: `${winHeight}px`}">
            <div v-for="(table,index) in treeData" :key="table.tname" class="tree-table">
              <div class="tree-table-name" @click="onFoldTable(index)">
                <span class="tree-fold-btn"
                    :class="['tree-fold-btn', foldIndex.includes(index) ? 'el-icon-remove-outline' : 'el-icon-circle-plus-outline']"
                />
                <img src="@/assets/table.png" width="15px">
                {{ table.tname }}
              </div>
              <template v-if="table.fields.length > 0 && foldIndex.includes(index)">
                <div v-for="field in table.fields" :key="field.name" class="tree-table-field">
                  {{ field.COLUMN_NAME }}
                  &lt;!&ndash; <span class="el-icon-document-copy"></span> &ndash;&gt;
                </div>
              </template>
            </div>
          </div>
        </el-tab-pane>
        &lt;!&ndash;<el-tab-pane label="命令集">
          <ul class="order-list">
            <li v-for="item in commitsList" :key="item.createTime" @click="copySQLOrder(item)">
              <img src="@/assets/order.png">
              {{ item.title }}
            </li>
          </ul>
        </el-tab-pane>&ndash;&gt;
      </el-tabs>-->
    </div>
    <div class="home-contaioner-right">
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
            <template v-if="runType === 3">
              <div class="error-message"><b>message：</b>{{ errMsg.sqlMessage }}</div>
              <div class="error-message"><b>errno：</b>{{ errMsg.errno }}</div>
              <div class="error-message"><b>sql：</b>"{{ errMsg.sql }}"</div>
              <div class="error-message"><b>code：</b>{{ errMsg.code }}</div>
              <div class="error-message"><b>sqlState：</b>{{ errMsg.sqlState }}</div>
            </template>
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
import CodemirrorEditor from '@/components/CodemirrorEditor'
import {  mapGetters, mapActions } from 'vuex'
export default {
  name: 'Home',
  components: {
    CodemirrorEditor
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
      }
    }
  },
  created() {
    this.queryTables()
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
      'getAllConnection',
      'getTreeData',
      'getFieldNames',
      'sendSQL'
    ]),
    loadNode(node, resolve) {
      if (node.level === 0) {
        this.queryConnections().then(res => {
          const { data } = res;
         resolve(data.map(item => ({
           id: item.id,
           name: item.database,
           isLeaf: false
         })));
        });
      }
      if(node.level > 0){
        console.log(node.data)
        this.queryDataBases().then(res => {
          const { data } = res;
          resolve(data.map(item => ({
            id: item.id,
            name: item.database,
            isLeaf: false
          })));
        });

      }

      /*if (node.level > 0) {

        console.log(node.data)
        const data = [{
          name: 'leaf',
          isLeaf: true
        }, {
          name: 'zone',

        }];
        resolve(data);

      }*/

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
    async queryConnections(){
      const res = await this.getAllConnection();
      return res;
    },

    async queryDataBases(connectionId){
      const res = await this.getDataBases({ id : connectionId });
      return res;
    },

    async executeSql() {
      // 执行SQL语句
      if (this.code.trim() === '') {
        this.$message.warning('请先编辑 SQL 命令！')
        return
      }
      this.runLoading = true
      this.tableData = []
      this.columns = []
      this.runResult = '执行中...'
      this.runType = 0
      this.errMsg = {}
      const res = await this.sendSQL({ sql: this.code })
      const { type, result } = res
      if (this.code.startsWith('SELECT')) {
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
          this.queryTables()
        } else if (type === '2') {
          this.runType = 3
          this.runResult = '执行失败！'
          this.errMsg = {...result}
        }
      }
      this.runLoading = false
    },

    async queryTables() {
      // 查询所有表
      const res = await this.getTreeData()
      const { type, result } = res
      if (type === '1' && result.length > 0) {
        this.treeData = []
        const tips = {}
        result.forEach(async(tname) => {
          if (tname.table_name && tname.table_name.trim() !== '') {
            const treeObj = {
              tname: tname.table_name,
              fields: []
            }
            tips[tname.table_name] = []

            // 查询表中的字段
            const fieldRes = await this.getFieldNames({ tname: tname.table_name })
            if (fieldRes.type === '1') {
              treeObj.fields = [...fieldRes.result]
              fieldRes.result.forEach(f => {
                tips[tname.table_name].push(f.COLUMN_NAME)
              })
            }
            this.treeData.push(treeObj)
          }
        })
        this.$refs.codemirrorEditor.setHintOptions(tips)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.home {
  background-color: #F4F4F4;
  display: flex;

  // 左侧区域
  .home-container-left {
    flex: 1.1;
    height: 100%;
    background-color: #fff;

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
      .order-list {
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
      }
    }
  }

  // 右侧区域
  .home-contaioner-right {
    flex: 6;
    height: 100%;

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
