import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'

export const store = createStore({
  state() {
    return {
      authCode: null,
      loginUser: null,
    }
  },
  mutations: {
    authCode(state, authCode) {
      state.authCode = authCode;
    },
    loginUser(state, loginUser) {
      state.loginUser = loginUser;
    },
    logout(state) {
      state.authCode = null;
      state.loginUser = null;
    },
  },
  actions: {},
  modules: {},
  getters: {
    isLogin(state) {
      return state.authCode != null && state.loginUser != null;
    },
    userName(state) {
      if (state.loginUser == null) {
        return null;
      }
      return state.loginUser.familyName + " " + state.loginUser.firstName;
    },
    authCode(state) {
      return state.authCode;
    },
  },

  // localStorageに保存
  // plugins: [createPersistedState()],
  // sessionStorageに保存
  plugins: [createPersistedState({ storage: window.sessionStorage })],
});
