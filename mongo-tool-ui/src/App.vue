<template>
  <div id="app">
    <div id="nav">
      <el-button type="text" icon="iconfont icon-connection" @click="createConnectionHandler">创建连接</el-button>
      <router-link to="/">SQL编辑器</router-link>
      <router-link to="/commits">提交记录</router-link>
      <router-link to="/setting">设置</router-link>
    </div>
    <router-view/>

    <el-dialog title="创建连接" :visible.sync="connectionDialogVisible" width = "40%">
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
    </el-dialog>
  </div>
</template>

<script>
  import { testConnection, createConnection } from '@/api/index.js'

  export default {
    data() {
      return {
        connectionDialogVisible : false,
        ruleForm :{
          name : '',
          url : ''
        },
        rules : {
          name: [
            { required: true, message: 'Mongo 连接名称不能为空', trigger: 'blur' }
          ],
          url: [
            { required: true, message: 'Mongo 连接URL不能为空 ', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
    },
    methods: {
      createConnectionHandler(){
        this.connectionDialogVisible = true;
      },
      testConnection(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
           testConnection({ url : this.ruleForm.url }).then(res => {
             const { failMessage, success } = res.data
             if(success){
               this.$message({
                 message: '连接成功',
                 type: 'success'
               });
             } else {
               this.$message({
                 offset: 50,
                 showClose: true,
                 message: '连接失败，' + failMessage,
                 type: 'error',
                 duration: 3000 * 10
               });
             }
           });

          }
        });
      },
      createConnection(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            createConnection({ name: this.ruleForm.name , url : this.ruleForm.url }).then(res => {
              const { code } = res
              if(code == '00000'){
                this.$message({
                  message: '创建成功',
                  type: 'success'
                });
                this.connectionDialogVisible = false;
                this.$router.go(0)
              } else {
                this.$message.error('创建失败！');
              }
            });
          }
        });
      },
      cancelConnection(){
        this.connectionDialogVisible = false;
        this.ruleForm = { name : '', url : ''}
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
  height: 50px;
  line-height: 50px;
  padding-left: 30px;
  background-color: #545c64;
  a {
    color: #fff;
    text-decoration: none;
    margin: 0px 15px;
    font-size: 14px;

    &.router-link-exact-active {
      color: #ffd04b;
    }
  }
}
</style>
