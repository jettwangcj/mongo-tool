import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate"
import getters from './getters'
import dbs from './modules/dbs'
import commits from './modules/commits'
import settings from './modules/settings'
import orders from './modules/orders'
import http from './modules/http'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    dbs,
    commits,
    settings,
    orders,
    http
  },
  getters,
  plugins: [
    createPersistedState()
  ]
})
