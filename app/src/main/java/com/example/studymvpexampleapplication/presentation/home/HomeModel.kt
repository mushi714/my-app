package com.example.studymvpexampleapplication.presentation.home

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Article
import com.example.studymvpexampleapplication.data.bean.Banner
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.MLog
import com.example.studymvpexampleapplication.util.launchCoroutine
import com.example.studymvpexampleapplication.util.toJsonString

/**
 * HomeModel：MVP 模式中 Home 模块的 Model 实现
 * 接收 Presenter 请求获取数据，并返回给 Presenter
 */
class HomeModel : HomeContract.Model {

    /**
     * 获取轮播图数据
     * 通过 launchCoroutine 在协程中执行网络请求，简化异步处理
     */
    override suspend fun getBanner(listener: RetrofitResponseListener<MutableList<Banner>>) {
        return launchCoroutine({
            // 调用 RetrofitService 获取 Banner 列表
            val baseBannerBean = RetrofitService.getApiService().getBanner()
            MLog.d("baseBannerBean = ${baseBannerBean.data.toJsonString()}")

            // 根据返回的 errorCode 决定回调 success 还是 error
            if (baseBannerBean.errorCode != 0) {
                listener.onError(baseBannerBean.errorCode, baseBannerBean.errorMsg)
            } else {
                listener.onSuccess(baseBannerBean.data)
            }
        }, onError = { e: Throwable ->
            // 网络或解析异常时打印异常信息并回调给 UI
            e.printStackTrace()
            listener.onError(-1, "网络请求失败: ${e.message}")
        })
    }

    /**
     * 获取首页文章列表（分页第一页）
     * @param page  页码，从 0 或 1 开始
     */
    override suspend fun getArticleList(page: Int, listener: RetrofitResponseListener<Article>) {
        return launchCoroutine({
            // 请求第 page 页文章数据
            val baseArticleBean = RetrofitService.getApiService().getArticleList(page)

            if (baseArticleBean.errorCode != 0) {
                listener.onError(baseArticleBean.errorCode, baseArticleBean.errorMsg)
            } else {
                listener.onSuccess(baseArticleBean.data)
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
        })
    }

    /**
     * 加载更多文章（分页加载后续页）
     * @param page 下一页页码
     */
    override suspend fun getMoreArticleList(
        page: Int,
        listener: RetrofitResponseListener<Article>
    ) {
        return launchCoroutine({
            // 与 getArticleList 相同接口，利用不同 page 参数实现“加载更多”
            val baseArticleBean = RetrofitService.getApiService().getArticleList(page)

            if (baseArticleBean.errorCode != 0) {
                listener.onError(baseArticleBean.errorCode, baseArticleBean.errorMsg)
            } else {
                listener.onSuccess(baseArticleBean.data)
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
        })
    }

    /**
     * 收藏文章
     * @param id 文章 ID
     */
    override suspend fun collect(id: Int, listener: RetrofitResponseListener<String>) {
        return launchCoroutine({
            // 调用收藏接口
            val baseCollectBean = RetrofitService.getApiService().collect(id)
            if (baseCollectBean.errorCode != 0) {
                listener.onError(baseCollectBean.errorCode, baseCollectBean.errorMsg)
            } else {
                // 收藏成功，返回固定提示
                listener.onSuccess("收藏成功")
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
        })
    }

    /**
     * 取消收藏文章
     * @param id 文章 ID
     */
    override suspend fun unCollect(id: Int, listener: RetrofitResponseListener<String>) {
        return launchCoroutine({
            // 调用取消收藏接口
            val baseCollectBean = RetrofitService.getApiService().unCollect(id)
            if (baseCollectBean.errorCode != 0) {
                listener.onError(baseCollectBean.errorCode, baseCollectBean.errorMsg)
            } else {
                // 取消收藏成功，返回固定提示
                listener.onSuccess("取消收藏成功")
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
        })
    }

}
