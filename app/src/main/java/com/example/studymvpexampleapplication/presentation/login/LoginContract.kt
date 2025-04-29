package com.example.studymvpexampleapplication.presentation.login

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.User

/**
 * LoginContract 定义了登录模块的 MVP 三大角色接口：Model、View、Presenter，
 * 用于在登录功能中统一约定各层之间的交互方法，解耦并规范实现。
 */
interface LoginContract : BaseContract {

    /**
     * Model 层接口，负责具体的数据获取和业务逻辑处理。
     * 继承自 IBaseModel，可根据需要扩展更多通用方法。
     */
    interface Model : BaseContract.IBaseModel {
        /**
         * 执行登录操作
         *
         * @param username   用户输入的用户名
         * @param password   用户输入的密码
         * @param listener   回调监听器，用于异步获取结果或错误信息
         */
        suspend fun login(
            username: String,
            password: String,
            listener: RetrofitResponseListener<User>
        )
    }

    /**
     * View 层接口，负责通知 Presenter 更新 UI，以及展示登录结果。
     * 继承自 IBaseView，可获取 Activity 上下文等通用方法。
     */
    interface View : BaseContract.IBaseView {
        /**
         * 登录成功时调用
         *
         * @param baseUserBean 服务器返回的用户信息实体
         */
        fun loginSuccess(baseUserBean: User)

        /**
         * 登录失败时调用
         *
         * @param errorMessage 错误提示信息，可用于 Toast 或错误页展示
         */
        fun loginError(errorMessage: String)
    }

    /**
     * Presenter 层接口，负责接收 View 的用户交互请求，
     * 调用 Model 完成业务逻辑后，将结果反馈给 View。
     * 继承自 IBasePresenter，提供生命周期管理方法。
     */
    interface Presenter : BaseContract.IBasePresenter {
        /**
         * 发起登录请求
         *
         * @param username 用户名
         * @param password 密码
         */
        fun login(username: String, password: String)
    }
}
