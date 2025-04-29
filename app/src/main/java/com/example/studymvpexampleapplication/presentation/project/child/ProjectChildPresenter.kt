package com.example.studymvpexampleapplication.presentation.project.child

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.ProjectChild
import kotlinx.coroutines.launch

class ProjectChildPresenter(view: ProjectChildContract.View) :
// 继承自 BasePresenter，绑定 View 与 Model 的通用逻辑，并实现 ProjectChildContract.Presenter 接口
    BasePresenter<ProjectChildContract.View, ProjectChildContract.Model>(view),
    ProjectChildContract.Presenter {

    /**
     * 创建并返回当前 Presenter 对应的 Model 实例
     */
    override fun createModel(): ProjectChildContract.Model {
        return ProjectChildModel()
    }

    /**
     * 请求指定分类（cid）第 page 页的子项目列表
     * Presenter 调用 Model#getProjectChild，并在回调中将结果分发给 View
     */
    override fun getProjectChild(page: Int, cid: Int) {
        // 使用 BasePresenter 提供的 coroutineScope 启动协程，保证与生命周期绑定
        coroutineScope.launch {
            mModel?.getProjectChild(page, cid, object : RetrofitResponseListener<ProjectChild> {
                /**
                 * 网络请求成功时调用，将数据传递给 View 进行显示
                 */
                override fun onSuccess(response: ProjectChild) {
                    mView?.get()?.getProjectChildSuccess(response)
                }

                /**
                 * 网络请求或业务错误时调用，将错误信息传递给 View 进行提示
                 */
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getProjectChildError(errorMessage)
                }

            })
        }
    }

    /**
     * 请求指定分类（cid）第 page 页的更多子项目（用于分页加载下一页）
     * 与 getProjectChild 不同的是，回调分为 “加载更多” 逻辑
     */
    override fun getProjectMoreChild(page: Int, cid: Int) {
        // 同样在协程中异步请求
        coroutineScope.launch {
            mModel?.getProjectMoreChild(page, cid, object : RetrofitResponseListener<ProjectChild> {
                /**
                 * 加载更多成功时调用，将新数据传递给 View 追加显示
                 */
                override fun onSuccess(response: ProjectChild) {
                    mView?.get()?.getProjectMoreChildSuccess(response)
                }

                /**
                 * 加载更多失败时调用，将错误信息传递给 View 进行提示
                 */
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getProjectMoreChildError(errorMessage)
                }

            })
        }
    }

}
