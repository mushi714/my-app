package com.example.studymvpexampleapplication.presentation.tree.child

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Article
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.launchCoroutine

// TreeChildModel：实现 TreeChildContract.Model，负责处理子列表数据及收藏/取消收藏网络请求
class TreeChildModel : TreeChildContract.Model {
    /**
     * 获取分类文章列表
     * @param page 页码，用于分页请求
     * @param cid 分类 ID
     * @param listener 请求回调，返回 Article 对象
     */
    override suspend fun getTreeChild(
        page: Int,
        cid: Int,
        listener: RetrofitResponseListener<Article>
    ) {
        // 使用自定义协程封装，自动处理异常和线程切换
        return launchCoroutine({
            // 发起网络请求，获取当前页的子分类文章数据
            val baseTreeChildBean =
                RetrofitService.getApiService().getTreeChild(page, cid)
            // 根据返回的 errorCode 判定请求是否成功
            if (baseTreeChildBean.errorCode != 0) {
                // 请求失败时调用 onError，传递错误码和消息
                listener.onError(baseTreeChildBean.errorCode, baseTreeChildBean.errorMsg)
            } else {
                // 请求成功时调用 onSuccess，将数据传递给 View 层
                listener.onSuccess(baseTreeChildBean.data)
            }
        }, onError = { e: Throwable ->
            // 协程内抛出异常时打印堆栈，便于调试
            e.printStackTrace()
        })
    }

    /**
     * 分页加载更多分类文章
     * @param page 下一页页码
     * @param cid 分类 ID
     * @param listener 请求回调，返回 Article 对象
     */
    override suspend fun getTreeMoreChild(
        page: Int,
        cid: Int,
        listener: RetrofitResponseListener<Article>
    ) {
        // 与 getTreeChild 逻辑相同，调用相同接口进行分页请求
        return launchCoroutine({
            val baseTreeChildBean =
                RetrofitService.getApiService().getTreeChild(page, cid)
            if (baseTreeChildBean.errorCode != 0) {
                listener.onError(baseTreeChildBean.errorCode, baseTreeChildBean.errorMsg)
            } else {
                listener.onSuccess(baseTreeChildBean.data)
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
        })
    }

    /**
     * 收藏文章
     * @param id 文章 ID
     * @param listener 请求回调，返回操作结果提示
     */
    override suspend fun collect(id: Int, listener: RetrofitResponseListener<String>) {
        // 封装协程调用，发起收藏请求
        return launchCoroutine({
            val baseCollectBean = RetrofitService.getApiService().collect(id)
            // 根据返回码调用不同回调
            if (baseCollectBean.errorCode != 0) {
                listener.onError(baseCollectBean.errorCode, baseCollectBean.errorMsg)
            } else {
                listener.onSuccess("收藏成功")  // 硬编码成功提示
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
        })
    }

    /**
     * 取消收藏文章
     * @param id 文章 ID
     * @param listener 请求回调，返回操作结果提示
     */
    override suspend fun unCollect(id: Int, listener: RetrofitResponseListener<String>) {
        // 封装协程调用，发起取消收藏请求
        return launchCoroutine({
            val baseCollectBean = RetrofitService.getApiService().unCollect(id)
            if (baseCollectBean.errorCode != 0) {
                listener.onError(baseCollectBean.errorCode, baseCollectBean.errorMsg)
            } else {
                listener.onSuccess("取消收藏成功")  // 硬编码取消收藏提示
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
        })
    }
}
