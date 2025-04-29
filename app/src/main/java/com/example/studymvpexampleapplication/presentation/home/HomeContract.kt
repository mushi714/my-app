package com.example.studymvpexampleapplication.presentation.home

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Article
import com.example.studymvpexampleapplication.data.bean.Banner

/**
 * HomeContract 定义了首页模块在 MVP 架构中的三大角色接口：
 * - Model：负责数据获取与业务处理
 * - View：负责 UI 更新与用户交互回调
 * - Presenter：负责协调 View 与 Model、执行业务逻辑
 */
interface HomeContract : BaseContract {

    /**
     * Model 层接口，继承自 IBaseModel，可挂载公共生命周期管理方法
     */
    interface Model : BaseContract.IBaseModel {
        /**
         * 获取轮播图数据
         *
         * @param listener 网络请求回调，返回 Banner 列表或错误信息
         */
        suspend fun getBanner(listener: RetrofitResponseListener<MutableList<Banner>>)

        /**
         * 获取首页文章列表（第一页或指定页）
         *
         * @param page     页码，从 0 或 1 开始
         * @param listener 网络请求回调，返回 Article 对象或错误信息
         */
        suspend fun getArticleList(page: Int, listener: RetrofitResponseListener<Article>)

        /**
         * 拉取更多文章（分页加载下一页）
         *
         * @param page     下一页页码
         * @param listener 网络请求回调，返回 Article 对象或错误信息
         */
        suspend fun getMoreArticleList(page: Int, listener: RetrofitResponseListener<Article>)

        /**
         * 收藏文章
         *
         * @param id       待收藏的文章 ID
         * @param listener 网络请求回调，返回成功提示或错误信息
         */
        suspend fun collect(id: Int, listener: RetrofitResponseListener<String>)

        /**
         * 取消收藏文章
         *
         * @param id       待取消收藏的文章 ID
         * @param listener 网络请求回调，返回成功提示或错误信息
         */
        suspend fun unCollect(id: Int, listener: RetrofitResponseListener<String>)
    }

    /**
     * View 层接口，继承自 IBaseView，可获取 Activity 对象等通用方法
     */
    interface View : BaseContract.IBaseView {

        // 轮播图回调
        fun getBannerSuccess(bannerList: MutableList<Banner>)  // 成功
        fun getBannerError(errorMessage: String)               // 失败

        // 文章列表回调
        fun getArticleListSuccess(article: Article)            // 成功
        fun getArticleListError(errorMessage: String)          // 失败

        // 加载更多文章回调
        fun getMoreArticleListSuccess(article: Article)        // 成功
        fun getMoreArticleListError(errorMessage: String)      // 失败

        // 收藏回调
        fun collectSuccess(successMessage: String)             // 收藏成功
        fun collectError(errorMessage: String)                 // 收藏失败

        // 取消收藏回调
        fun unCollectSuccess(successMessage: String)           // 取消收藏成功
        fun unCollectError(errorMessage: String)               // 取消收藏失败

        // 用户未登录时的提示（可跳转到登录页）
        fun login(msg: String)
    }

    /**
     * Presenter 层接口，继承自 IBasePresenter，负责接收 View 的调用并调度 Model
     */
    interface Presenter : BaseContract.IBasePresenter {
        /**
         * 请求获取轮播图
         */
        fun getBanner()

        /**
         * 请求获取文章列表
         *
         * @param page 页码
         */
        fun getArticleList(page: Int)

        /**
         * 请求加载更多文章
         *
         * @param page 页码
         */
        fun getMoreArticleList(page: Int)

        /**
         * 请求收藏文章
         *
         * @param id 文章 ID
         */
        fun collect(id: Int)

        /**
         * 请求取消收藏
         *
         * @param id 文章 ID
         */
        fun unCollect(id: Int)
    }
}
