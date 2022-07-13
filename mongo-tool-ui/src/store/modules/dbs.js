/**
 * WebSQL数据库对象
 */

const state = {
  db: null
}

const mutations = {
  CREATE_DATABASE(state, params) {
    state.db = params
  }
}

const actions = {
  createDatabase({ commit }, params) {
    // 创建数据库对象
    const db = openDatabase('mydb', '1.0', 'Test DB', 2 * 1024 * 1024)
    commit('CREATE_DATABASE', db)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}