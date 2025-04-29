package com.example.studymvpexampleapplication.presentation.project

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Project
import kotlinx.coroutines.launch

class ProjectPresenter(view: ProjectContract.View) :
// 继承 BasePresenter，绑定 View 与 Model 的通用逻辑，并实现 ProjectContract.Presenter
    BasePresenter<ProjectContract.View, ProjectContract.Model>(view),
    ProjectContract.Presenter {

    // 创建并返回当前 Presenter 需要使用的 Model 实例
    override fun createModel(): ProjectContract.Model = ProjectModel()

    // Presenter 层对外暴露的方法，触发项目列表的获取流程
    override fun getProject() {
        // 在 BasePresenter 提供的 coroutineScope 中启动协程，保证异步操作与生命周期绑定
        coroutineScope.launch {
            // 调用 Model#getProject 发起网络或其他异步请求，并通过回调接收结果
            mModel?.getProject(object : RetrofitResponseListener<MutableList<Project>> {
                // 请求成功时调用，将结果传递给 View 层进行 UI 更新
                override fun onSuccess(response: MutableList<Project>) {
                    mView?.get()?.getProjectSuccess(response)
                }

                // 请求失败时调用，将错误信息传递给 View 层进行提示
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getProjectError(errorMessage)
                }
            })
        }
    }
}
