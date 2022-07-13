const getters = {
  db: state => state.dbs.db, // 数据库对象
  commitsList: state  => state.commits.list, // 命令提交记录
  options: state => state.settings.options // 编辑器配置
}

export default getters