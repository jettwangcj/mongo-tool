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

      <el-tabs v-model="editableTabsValue" type="card" :closable = "isClosable" @tab-remove="removeTab">
        <el-tab-pane v-for="(item, index) in editableTabs"
                :key="item.name"
                :label="item.title"
                :name="item.name">
          <query-panel></query-panel>
        </el-tab-pane>
      </el-tabs>

    </div>

  </div>
</template>

<script>
import CreateConnection from "@/components/CreateConnection";
import QueryPanel from '@/components/QueryPanel'
import {  mapGetters, mapActions } from 'vuex'
import { delConnection } from '@/api'

export default {
  name: 'Home',
  components: {
    CreateConnection, QueryPanel
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
      treeLoading: false,

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
      },

      isClosable : false,
      editableTabsValue: '1',
      editableTabs: [{
        title: '无标题-查询',
        name: '1',
        type: 'query'
      }],
      tabIndex: 1
    }
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
      const newTabName = ++ this.tabIndex + '';
      this.editableTabs.push({
        title: '无标题-查询',
        name: newTabName,
        type: 'query'
      });
      this.editableTabsValue = newTabName;
      this.enableClosable();
    },
    newDatabase() {
      console.log(this.currRightClickObj)
    },
    enableClosable(){
      if(this.editableTabs.length > 1){
        this.isClosable = true;
      } else {
        this.isClosable = false;
      }
    },
    removeTab(targetName) {
      let tabs = this.editableTabs;
      let activeName = this.editableTabsValue;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }
      this.editableTabsValue = activeName;
      this.editableTabs = tabs.filter(tab => tab.name !== targetName);
      this.enableClosable();
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

    /*.database-select {
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
    }*/
  }

}
</style>
