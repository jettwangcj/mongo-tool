import axios from 'axios'

// 创建axios实例对象
const instance = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 1000 * 60 * 4
})

// 请求拦截
instance.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截
instance.interceptors.response.use(
  response => {
    return response
  },
  error => {
    return Promise.reject(error)
  }
)

// 封装的请求方法
async function http({ url, method = 'get', params, data, simple = true }) {
  let result
  if (method === 'get' || method === 'delete') {
    await instance[method](url, { params, data }).then(res => {
      result = simple ? res.data : res
    }).catch(error => {
      result = error
    })
  } else if (method === 'post' || method === 'put' || method === 'patch') {
    await instance[method](url, params).then(res => {
      result = simple ? res.data : res
    }).catch(error => {
      result = error
    })
  }
  return result
}

export default http
