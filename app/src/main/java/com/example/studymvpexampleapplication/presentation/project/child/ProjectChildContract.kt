package com.example.studymvpexampleapplication.presentation.project.child

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.ProjectChild

interface ProjectChildContract : BaseContract {
    // 定义“项目子项”模块的契约接口，遵循 MVP 架构，将 Model、View、Presenter 三层职责分离
    interface Model : BaseContract.IBaseModel {
        /**
         * 获取指定分类（cid）下的项目列表（第 page 页）
         * @param page 页码，从 1 开始
         * @param cid  分类 ID，用于区分不同项目分类
         * @param listener 回调接口，成功时返回 ProjectChild 对象，失败时返回错误信息
         */
        fun getProjectChild(
            page: Int,
            cid: Int,
            listener: RetrofitResponseListener<ProjectChild>
        )

        /**
         * 加载更多指定分类（cid）的后续页面数据
         * 与 getProjectChild 区别仅在于调用时机：通常用于上拉分页时请求下一页
         */
        fun getProjectMoreChild(
            page: Int,
            cid: Int,
            listener: RetrofitResponseListener<ProjectChild>
        )
    }

    interface View : BaseContract.IBaseView {
        /**
         * 获取子项目列表成功回调
         * @param projectChild 包含列表数据及分页信息的实体类
         */
        fun getProjectChildSuccess(projectChild: ProjectChild)

        /**
         * 获取子项目列表失败回调
         * @param errorMessage 错误消息，供界面提示或日志记录
         */
        fun getProjectChildError(errorMessage: String)

        /**
         * 加载更多子项目成功回调
         * @param projectChild 包含加载到的新数据及分页信息
         */
        fun getProjectMoreChildSuccess(projectChild: ProjectChild)

        /**
         * 加载更多子项目失败回调
         * @param errorMessage 错误消息，供界面提示或日志记录
         */
        fun getProjectMoreChildError(errorMessage: String)
    }

    interface Presenter : BaseContract.IBasePresenter {
        /**
         * 请求指定分类的第 page 页子项目列表
         * Presenter 层负责调用 Model#getProjectChild，并将结果分发给 View
         */
        fun getProjectChild(page: Int, cid: Int)

        /**
         * 请求更多指定分类的子项目下一页数据
         * 用于分页加载场景，调用 Model#getProjectMoreChild 并分发结果
         */
        fun getProjectMoreChild(page: Int, cid: Int)
    }
}
