/**
 * 编辑器配置信息
 */

const state = {
  options: {
    lineNumbers: true,
    lineWrapping: true,
    styleActiveLine: true,
    extraKeys: 'Ctrl',
    theme: 'default'
  }
}

const mutations = {
  SAVE_OPTIONS(state, params) {
    state.options = { ...params }
  }
}

const actions = {
  saveOptions({ commit }, params) {
    commit('SAVE_OPTIONS', params)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}