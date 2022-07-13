import {
  getDataBases,
  getAllConnection,
  getTreeData,
  getFieldNames,
  sendSQL
} from '@/api'

/**
 * 发送http请求
 */

const state = {}

const mutations = {}

const actions = {

  async getDataBases({ commit }, params) {
    const data = await getDataBases(params)
    return data
  },

  async getAllConnection({ commit }, params) {
    const data = await getAllConnection(params)
    return data
  },
  async getTreeData({ commit }, params) {
    const data = await getTreeData(params)
    return data
  },
  async getFieldNames({ commit }, params) {
    const data = await getFieldNames(params)
    return data
  },
  async sendSQL({ commit }, params) {
    const data = await sendSQL(params)
    return data
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
