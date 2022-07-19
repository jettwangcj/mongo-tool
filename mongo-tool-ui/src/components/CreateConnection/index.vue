<template>
    <div id="connection">
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
        name: "CreateConnection",

        props: {
            connection: {
                type : Object,
                default() {
                    return {
                        connectionDialogVisible : undefined,
                        currentConnectionId : undefined
                    }
                }
            },
        },
        data() {
            return {
                connectionDialogVisible : false,
                ruleForm :{
                    id: '',
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
        watch: {
            connection(newConnection, oldConnection){
                this.connectionDialogVisible = newConnection.connectionDialogVisible;
                if(this.connectionDialogVisible){
                    const connection = window.sessionStorage.getItem("connection-" + newConnection.currentConnectionId);
                    if(connection) {
                        const { id, name, url} = JSON.parse(connection)
                        this.ruleForm = { id, name, url }
                    } else {
                        this.ruleForm = { id: '',name : '',url : ''}
                    }
                }
            }
        },
        methods: {
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
                        createConnection({ id: this.ruleForm.id, name: this.ruleForm.name , url : this.ruleForm.url }).then(res => {
                            const { code } = res
                            if(code == '00000'){
                                this.$message({
                                    message: '创建成功',
                                    type: 'success'
                                });
                                this.$emit("create-connection-sucess")
                            } else {
                                this.$message.error('创建失败！');
                            }
                        });
                    }
                });
            },
            cancelConnection(){
                this.ruleForm = { name : '', url : ''}
                this.$emit("cancel-connection")
            }
        }
    }
</script>

<style scoped>

</style>
