package com.example.studymvpexampleapplication.presentation.navi

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Navi

interface NaviContract : BaseContract {
    // 定义导航功能的契约接口，继承自 BaseContract，保证所有模块的契约风格一致。
    interface Model : BaseContract.IBaseModel {

        // Model 负责数据层操作，继承通用的 IBaseModel 接口以获取基础功能。
        suspend fun getNavi(listener: RetrofitResponseListener<MutableList<Navi>>)

        // 使用 Retrofit 在协程中发起异步网络请求获取导航数据，suspend 关键字表示该函数可挂起，不会阻塞线程。
    }

    interface View : BaseContract.IBaseView {

        // View 接口定义了界面层的回调方法，继承自 IBaseView 以复用通用 UI 行为。
        fun getNaviSuccess(naviList: MutableList<Navi>)

        // 当数据请求成功时调用，传入导航列表用于更新界面。
        fun getNaviError(errorMessage: String)
        // 当数据请求失败时调用，传入错误信息以便在界面上提示用户。
    }

    interface Presenter : BaseContract.IBasePresenter {
        // Presenter 充当中间人，协调 Model 与 View 的交互，符合 MVP 架构模式。
        fun getNavi()
        // 启动导航数据获取流程，调用 Model#getNavi 并将结果分发给 View。
    }
}
