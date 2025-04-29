package com.example.studymvpexampleapplication.presentation.project

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Project

interface ProjectContract : BaseContract {
    // 定义“项目”模块的契约接口，遵循 MVP 架构，将 Model、View、Presenter 三层职责分离
    interface Model : BaseContract.IBaseModel {
        // Model 层：发起网络请求获取项目列表
        // listener 为自定义的 RetrofitResponseListener，用于回调成功或失败结果
        fun getProject(listener: RetrofitResponseListener<MutableList<Project>>)
    }

    interface View : BaseContract.IBaseView {
        // View 层：请求成功时回调，传递项目列表用于更新 UI
        fun getProjectSuccess(projectList: MutableList<Project>)
        // View 层：请求失败时回调，传递错误信息以便提示用户
        fun getProjectError(errorMessage: String)
    }

    interface Presenter : BaseContract.IBasePresenter {
        // Presenter 层：触发项目列表获取流程，协调 Model 与 View 之间的交互
        fun getProject()
    }
}
