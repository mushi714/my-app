package com.example.studymvpexampleapplication.presentation.tree.child

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Article

// TreeChildContract：树形列表子页面的 MVP 契约，定义了 Model、View 和 Presenter 层接口
interface TreeChildContract : BaseContract {

    // Model 层：负责数据获取与业务请求，继承自 IBaseModel
    interface Model : BaseContract.IBaseModel {
        /**
         * 获取指定分类的文章列表
         * @param page 页码，从 0 或 1 开始
         * @param cid 分类 ID
         * @param listener 网络请求回调，返回 Article 对象
         */
        suspend fun getTreeChild(page: Int, cid: Int, listener: RetrofitResponseListener<Article>)

        /**
         * 获取更多文章列表（分页加载）
         * @param page 下一页页码
         * @param cid 分类 ID
         * @param listener 网络请求回调，返回 Article 对象
         */
        suspend fun getTreeMoreChild(page: Int, cid: Int, listener: RetrofitResponseListener<Article>)

        /**
         * 收藏指定文章
         * @param id 文章 ID
         * @param listener 收藏结果回调，返回成功消息
         */
        suspend fun collect(id: Int, listener: RetrofitResponseListener<String>)

        /**
         * 取消收藏指定文章
         * @param id 文章 ID
         * @param listener 取消收藏结果回调，返回成功消息
         */
        suspend fun unCollect(id: Int, listener: RetrofitResponseListener<String>)
    }

    // View 层：定义 UI 更新方法，继承自 IBaseView
    interface View : BaseContract.IBaseView {
        /**
         * 获取文章列表成功回调
         * @param article 返回的 Article 数据
         */
        fun getTreeChildSuccess(article: Article)
        /**
         * 获取文章列表失败回调
         * @param errorMessage 错误描述
         */
        fun getTreeChildError(errorMessage: String)

        /**
         * 分页加载更多文章成功回调
         * @param article 返回的 Article 数据
         */
        fun getTreeMoreChildSuccess(article: Article)
        /**
         * 分页加载更多文章失败回调
         * @param errorMessage 错误描述
         */
        fun getTreeMoreChildError(errorMessage: String)

        /**
         * 收藏成功回调
         * @param successMessage 成功消息文本
         */
        fun collectSuccess(successMessage: String)
        /**
         * 收藏失败回调
         * @param errorMessage 错误描述
         */
        fun collectError(errorMessage: String)

        /**
         * 取消收藏成功回调
         * @param successMessage 成功消息文本
         */
        fun unCollectSuccess(successMessage: String)
        /**
         * 取消收藏失败回调
         * @param errorMessage 错误描述
         */
        fun unCollectError(errorMessage: String)

        /**
         * 未登录或需要登录时的提示回调
         * @param msg 登录提示消息
         */
        fun login(msg: String)
    }

    // Presenter 层：定义与 View 交互的方法，继承自 IBasePresenter
    interface Presenter : BaseContract.IBasePresenter {
        /**
         * 请求获取文章列表
         * @param page 页码
         * @param cid 分类 ID
         */
        fun getTreeChild(page: Int, cid: Int)

        /**
         * 请求加载更多文章（分页）
         * @param page 下一页页码
         * @param cid 分类 ID
         */
        fun getTreeMoreChild(page: Int, cid: Int)

        /**
         * 请求收藏文章
         * @param id 文章 ID
         */
        fun collect(id: Int)

        /**
         * 请求取消收藏文章
         * @param id 文章 ID
         */
        fun unCollect(id: Int)
    }
}
