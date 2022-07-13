/**
 * 提交记录
 */

const state = {
  list: []
}

const mutations = {
  ADD_LOGS(state, params) {
    state.list.unshift(params)
  }
}

const actions = {
  addLogs({ commit }, params) {
    // 提交记录
    commit('ADD_LOGS', params)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}