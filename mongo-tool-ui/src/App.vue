<template>
  <div id="app">
    <div id="nav">
      <el-button type="text" icon="iconfont icon-createConnection"
                 @click="createConnectionHandler"
                 style="margin-right: 10px;color: #fff;">
        创建连接
      </el-button>
      <router-link to="/">
        <div style="display: inline-block">
          <i class="iconfont icon-SQLEditor mr5"></i>SQL编辑器
        </div>
      </router-link>
      <router-link to="/commits">
        <div style="display: inline-block">
          <i class="iconfont icon-record mr5"></i>提交记录
        </div>
      </router-link>
      <router-link to="/setting">
        <div style="display: inline-block">
          <i class="iconfont icon-setting mr5"></i>设置
        </div>
      </router-link>
    </div>
    <router-view/>

    <create-connection :connection = "createConnectionInfo"
                       @cancel-connection="cancelConnection"
                       @create-connection-sucess="createConnectionSuccess"></create-connection>
    <!--<el-dialog title="创建连接" :visible.sync="connectionDialogVisible" width = "40%">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="连接名称" prop="name">
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="url" prop="url">
          <el-input type="textarea" v-model="ruleForm.url" placeholder="mongodb://localhost:27017/"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="testConnection('ruleForm')" plain>测试连接</el-button>
          <el-button type="primary" @click="createConnection('ruleForm')" plain>创建连接</el-button>
          <el-button @click="cancelConnection" plain>取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>-->
  </div>
</template>

<script>
  import CreateConnection from "@/components/CreateConnection";
  export default {
    components: {CreateConnection},
    data() {
      return {
        createConnectionInfo : {
          currentConnectionId: undefined,
          connectionDialogVisible: false,
        }
      }
    },
    created() {
    },
    methods: {
      createConnectionHandler(){
        this.connectionDialogVisible = true;
      },
      cancelConnection(){
        this.createConnectionInfo = { connectionDialogVisible : false, currentConnectionId : undefined }
      },
      createConnectionSuccess(){
        this.createConnectionInfo = { connectionDialogVisible : false, currentConnectionId : undefined }
        this.$router.go(0)
      }
    }
  }
</script>

<style lang="scss">
html,body {
  padding: 0;
  margin: 0;
  height: 100%;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  // text-align: center;
  color: #2c3e50;
  height: 100%;
}

#nav {
  height: 60px;
  line-height: 60px;
  padding-left: 30px;
  background-color: #29323b;
  .mr5{
    margin-right: 5px;
  }
  a {
    color: #fff;
    text-decoration: none;
    margin: 0px 15px;
    font-size: 15px;

    &.router-link-exact-active {
      color: #66b1ff;
    }
  }
}
</style>
