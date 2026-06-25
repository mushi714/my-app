package com.example.studymvpexampleapplication.presentation.collect

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Collect
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.launchCoroutine

class CollectModel : CollectContract.Model {
    /**
     * 获取收藏列表（第一页或指定页）
     * @param page 请求页码，从 0 开始
     * @param listener 回调接口，返回 Collect 实体或错误信息
     */
    override suspend fun getCollectList(page: Int, listener: RetrofitResponseListener<Collect>) {
        // 在协程中执行网络请求，保证不阻塞 UI 线程
        return launchCoroutine({
            // 调用 RetrofitService 获取收藏列表数据
            val collectBaseBean = RetrofitService.getApiService().getCollectList(page)
            // 判断返回码，非 0 视为业务错误
            if (collectBaseBean.errorCode != 0) {
                // 请求失败，回调错误码和错误信息
                listener.onError(collectBaseBean.errorCode, collectBaseBean.errorMsg)
            } else {
                // 请求成功，回调实际数据
                listener.onSuccess(collectBaseBean.data)
            }
        }, onError = { e: Throwable ->
            // 捕获网络或其他异常，打印堆栈以便调试，同时回调给 UI
            e.printStackTrace()
            listener.onError(-1, "网络请求失败: ${e.message}")
        })
    }

    /**
     * 获取更多收藏列表（下一页）
     * @param page 请求的下一页页码
     * @param listener 回调接口
     */
    override suspend fun getCollectMoreList(
        page: Int,
        listener: RetrofitResponseListener<Collect>
    ) {
        // 与 getCollectList 逻辑相同，仅请求页码不同
        return launchCoroutine({
            val collectBaseBean = RetrofitService.getApiService().getCollectList(page)
            if (collectBaseBean.errorCode != 0) {
                listener.onError(collectBaseBean.errorCode, collectBaseBean.errorMsg)
            } else {
                listener.onSuccess(collectBaseBean.data)
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
        })
    }

    /**
     * 取消收藏（根据收藏 ID 和原始文章 ID）
     * @param id 收藏记录的唯一标识
     * @param originId 原始文章 ID；如果为自己收藏页面的取消操作，则为 -1
     * @param listener 回调接口，成功返回固定提示，失败返回错误信息
     */
    override suspend fun unCollect(
        id: Int,
        originId: Int,
        listener: RetrofitResponseListener<String>
    ) {
        // 发起取消收藏请求
        return launchCoroutine({
            val baseCollectBean = RetrofitService.getApiService().unCollect1(id, originId)
            if (baseCollectBean.errorCode != 0) {
                // 业务错误时回调错误信息
                listener.onError(baseCollectBean.errorCode, baseCollectBean.errorMsg)
            } else {
                // 成功时回调固定成功提示
                listener.onSuccess("取消收藏成功")
            }
        }, onError = { e: Throwable ->
            // 异常时打印日志
            e.printStackTrace()
        })
    }
}
