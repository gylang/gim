import axios from 'axios';
import router from 'vue-router';

let instance = axios.create({
    baseURL: 'http://127.0.0.1:8888/'
});
// http request 请求拦截器，有token值则配置上token值
instance.interceptors.request.use(
    config => {
        let token = sessionStorage.getItem("token");
        console.log(token)
        if (token) {  // 每次发送请求之前判断是否存在token，如果存在，则统一在http请求的header都加上token，不用每次请求都手动添加了
            console.log("设置请求头")
            config.headers["token"] = token;
        }
        return config;
    },
    err => {
        return Promise.reject(err);
    });

// http response 服务器响应拦截器，这里拦截401错误，并重新跳入登页重新获取token
instance.interceptors.response.use(
    (response) => {
        console.log(response.data)
        if (response.data.code === '00000') {
            return response;
        }

        // 暂时不做权限处理
        else if (response.data.status === 0) {
            router.replace('/');
        } else if (response.data.code !== '00000') {
            Message.error({message: response.data.msg});
        }
    },
    error => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    // 这里写清除token的代码
                    router.replace('/');
            }
        }
        return Promise.reject(error.response.data)
    });

export default {
    get(url, params) {
        return new Promise((resolve, reject) => {
            instance.get(url, {params}).then(r => {
                if (r) {
                    resolve(r.data);
                }
            })
        })
    },
    post(url, params) {
        return new Promise((resolve, reject) => {
            instance.post(url, params).then(r => {
                if (r) {
                    resolve(r.data);
                }
            })
        })
    }
}
