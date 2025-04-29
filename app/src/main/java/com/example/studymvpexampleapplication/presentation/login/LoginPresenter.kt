package com.example.studymvpexampleapplication.presentation.login

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.User
import kotlinx.coroutines.launch

/**
 * LoginPresenter 实现了 LoginContract.Presenter，并继承自 BasePresenter，
 * 负责接收 View 层的登录请求，调用 Model 层执行业务，再将结果反馈给 View 层。
 *
 * @param view 传入的 LoginContract.View 实例，用于后续回调 UI 更新
 */
class LoginPresenter(view: LoginContract.View) :
    BasePresenter<LoginContract.View, LoginContract.Model>(view),
    LoginContract.Presenter {

    /**
     * 创建并返回当前 Presenter 对应的 Model 实例
     *
     * @return LoginModel，用于执行实际的网络登录请求
     */
    override fun createModel(): LoginContract.Model = LoginModel()

    /**
     * 发起登录请求
     *
     * @param username 用户输入的用户名
     * @param password 用户输入的密码
     */
    override fun login(username: String, password: String) {
        // 使用 BasePresenter 中提供的 coroutineScope 在主线程启动协程
        coroutineScope.launch {
            // 调用 Model 层的 login 方法，传入用户名、密码和回调监听器
            mModel?.login(username, password, object : RetrofitResponseListener<User> {
                /**
                 * 当登录成功时被回调
                 *
                 * @param response 成功返回的 User 对象
                 */
                override fun onSuccess(response: User) {
                    // 从弱引用中获取 View 实例，并通知 View 层登录成功
                    mView?.get()?.loginSuccess(response)
                }

                /**
                 * 当登录失败时被回调
                 *
                 * @param errorCode    后端返回的错误码
                 * @param errorMessage 后端返回的错误描述
                 */
                override fun onError(errorCode: Int, errorMessage: String) {
                    // 从弱引用中获取 View 实例，并通知 View 层登录失败
                    mView?.get()?.loginError(errorMessage)
                }
            })
        }
    }
}
