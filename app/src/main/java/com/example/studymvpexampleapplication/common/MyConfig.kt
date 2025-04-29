package com.example.studymvpexampleapplication.common


// 用于存放全局配置项的类
class MyConfig {

    companion object {
        // 表示用户是否已登录的标志位，存储在 SharedPreferences 中的 key
        const val IS_LOGIN = "isLogin"

        // 存储用户登录后返回的 Cookie，用于后续接口请求时维持会话
        const val COOKIE = "cookie"

        // 存储用户名或用户标识，用于界面展示或请求参数
        const val USER_NAME = "username"
    }

}
