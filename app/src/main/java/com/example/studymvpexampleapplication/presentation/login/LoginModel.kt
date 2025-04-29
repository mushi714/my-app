package com.example.studymvpexampleapplication.presentation.login

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.User
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.launchCoroutine

/**
 * LoginModel 实现了 LoginContract.Model 接口，
 * 负责调用网络接口执行登录逻辑，并通过回调通知调用方结果。
 */
class LoginModel : LoginContract.Model {

    /**
     * 发起登录请求
     *
     * @param username  用户名
     * @param password  密码
     * @param listener  登录结果回调，成功时返回 User 对象，失败时返回错误码和消息
     */
    override suspend fun login(
        username: String,
        password: String,
        listener: RetrofitResponseListener<User>
    ) = launchCoroutine(
        // 正常执行块：调用 RetrofitService.getApiService().login 发起网络请求
        {
            // 发起登录请求，获取封装在 BaseBean<User> 中的响应
            val userBaseBean = RetrofitService.getApiService().login(username, password)
            // 根据返回的 errorCode 判断请求是否成功
            if (userBaseBean.errorCode != 0) {
                // 请求失败：通过 listener 回调 onError，传入错误码和错误消息
                listener.onError(userBaseBean.errorCode, userBaseBean.errorMsg)
            } else {
                // 请求成功：通过 listener 回调 onSuccess，传入解析出的 User 对象
                listener.onSuccess(userBaseBean.data)
            }
        },
        // 异常处理块：捕获网络或解析过程中的任何异常并打印堆栈
        onError = { e: Throwable ->
            e.printStackTrace()
        }
    )
}
