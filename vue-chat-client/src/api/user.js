import fetch from './fetch';

export default {
    getUser(params) { // 获取所有用户
        return fetch.get('api/auth/userInfo', params);
    },

    login(params) { // 登录
        return fetch.post('api/auth/login', params);
    },
    registry(params) { // 登录
        return fetch.post('api/auth/registry', params);
    },
    search(params) {
        return fetch.post('api/user/search', params);
    },
    applyUser(params) {
        return fetch.post('api/friend/apply', params);
    },

    answer(params) {
        return fetch.post('api/friend/answer', params);
    },



}

