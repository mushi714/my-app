package com.example.studymvpexampleapplication.presentation.collect

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Collect


interface CollectContract : BaseContract {
    // Model 层：负责收藏数据的获取与操作
    interface Model : BaseContract.IBaseModel {
        /**
         * 获取收藏列表
         * @param page 分页页码，从 0 开始
         * @param listener 回调接口，成功返回 Collect 实体，失败返回错误信息
         */
        suspend fun getCollectList(page: Int, listener: RetrofitResponseListener<Collect>)

        /**
         * 获取更多收藏列表（分页加载下一页）
         * @param page 下一页页码
         * @param listener 回调接口，成功返回 Collect 实体，失败返回错误信息
         */
        suspend fun getCollectMoreList(page: Int, listener: RetrofitResponseListener<Collect>)

        /**
         * 取消收藏
         * @param id 收藏记录的唯一 ID
         * @param originId 原始文章 ID（无原始文章时传 -1）
         * @param listener 回调接口，成功返回操作结果消息，失败返回错误信息
         */
        suspend fun unCollect(id: Int, originId: Int, listener: RetrofitResponseListener<String>)
    }

    // View 层：定义 UI 层显示与错误提示的接口
    interface View : BaseContract.IBaseView {
        /**
         * 取消收藏成功回调
         * @param successMessage 成功提示信息
         */
        fun unCollectSuccess(successMessage: String)

        /**
         * 取消收藏失败回调
         * @param errorMessage 错误提示信息
         */
        fun unCollectError(errorMessage: String)

        /**
         * 获取收藏列表成功回调
         * @param collect 收藏列表数据实体
         */
        fun getCollectListSuccess(collect: Collect)

        /**
         * 获取收藏列表失败回调
         * @param errorMessage 错误提示信息
         */
        fun getCollectListError(errorMessage: String)

        /**
         * 加载更多收藏列表成功回调
         * @param collect 收藏列表数据实体
         */
        fun getCollectMoreListSuccess(collect: Collect)

        /**
         * 加载更多收藏列表失败回调
         * @param errorMessage 错误提示信息
         */
        fun getCollectMoreListError(errorMessage: String)

        /**
         * 未登录回调
         * @param msg 提示登录的消息
         */
        fun login(msg: String)
    }

    // Presenter 层：负责协调 Model 与 View，触发数据请求并分发结果
    interface Presenter : BaseContract.IBasePresenter {
        /**
         * 触发取消收藏操作
         * @param id 收藏记录的唯一 ID
         * @param originId 原始文章 ID
         */
        fun unCollect(id: Int, originId: Int)

        /**
         * 触发获取收藏列表
         * @param page 分页页码
         */
        fun getCollectList(page: Int)

        /**
         * 触发分页加载更多收藏列表
         * @param page 下一页页码
         */
        fun getCollectMoreList(page: Int)
    }
}
