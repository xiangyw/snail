import { defineStore } from 'pinia';

interface AuthState {
  userId: number | null;
  token: string | null;
  userInfo: any | null;
  isAuthenticated: boolean;
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    userId: null,
    token: null,
    userInfo: null,
    isAuthenticated: false
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    getUserInfo: (state) => state.userInfo
  },

  actions: {
    setAuthInfo(userId: number, token: string, userInfo: any = null) {
      this.userId = userId;
      this.token = token;
      this.userInfo = userInfo;
      this.isAuthenticated = true;
      
      // 存储到localStorage
      localStorage.setItem('userId', userId.toString());
      localStorage.setItem('token', token);
      if (userInfo) {
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
      }
    },

    clearAuthInfo() {
      this.userId = null;
      this.token = null;
      this.userInfo = null;
      this.isAuthenticated = false;
      
      // 清除localStorage
      localStorage.removeItem('userId');
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
    },

    // 从localStorage恢复状态
    hydrate() {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');
      const userInfo = localStorage.getItem('userInfo');

      if (userId) this.userId = parseInt(userId);
      if (token) this.token = token;
      if (userInfo) this.userInfo = JSON.parse(userInfo);
      this.isAuthenticated = !!(this.userId && this.token);
    }
  }
});