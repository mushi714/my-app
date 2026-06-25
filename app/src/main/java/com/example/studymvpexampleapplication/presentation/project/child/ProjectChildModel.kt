package com.example.studymvpexampleapplication.presentation.project.child

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.ProjectChild
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.launchCoroutine

class ProjectChildModel : ProjectChildContract.Model {
    // 实现 Model 接口：用于获取指定分类 page 页的项目子项列表
    override fun getProjectChild(
        page: Int,
        cid: Int,
        listener: RetrofitResponseListener<ProjectChild>
    ) {
        // 在自定义的 launchCoroutine 中执行网络请求，自动处理协程上下文与异常
        return launchCoroutine({
            // 发起 Retrofit 网络请求，挂起函数 getProjectChild(page, cid)
            val projectChildBaseBean =
                RetrofitService.getApiService().getProjectChild(page, cid)
            // 根据统一 API 响应中的 errorCode 判断请求结果
            if (projectChildBaseBean.errorCode != 0) {
                // 业务错误时回调 onError，传递错误码与信息给 Presenter
                listener.onError(projectChildBaseBean.errorCode, projectChildBaseBean.errorMsg)
            } else {
                // 请求成功时回调 onSuccess，传递解析后的数据
                listener.onSuccess(projectChildBaseBean.data)
            }
        }, onError = { e: Throwable ->
            // 捕获网络或其他异常，打印堆栈以便调试，同时回调给 UI
            e.printStackTrace()
            listener.onError(-1, "网络请求失败: ${e.message}")
        })
    }

    // 实现加载更多接口，逻辑与 getProjectChild 相同，仅调用时机不同（分页加载）
    override fun getProjectMoreChild(
        page: Int,
        cid: Int,
        listener: RetrofitResponseListener<ProjectChild>
    ) {
        return launchCoroutine({
            val projectChildBaseBean =
                RetrofitService.getApiService().getProjectChild(page, cid)
            if (projectChildBaseBean.errorCode != 0) {
                listener.onError(projectChildBaseBean.errorCode, projectChildBaseBean.errorMsg)
            } else {
                listener.onSuccess(projectChildBaseBean.data)
            }
        }, onError = { e: Throwable ->
            e.printStackTrace()
            listener.onError(-1, "网络请求失败: ${e.message}")
        })
    }
}
