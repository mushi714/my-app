package com.example.studymvpexampleapplication.presentation.navi

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Navi
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.launchCoroutine

class NaviModel : NaviContract.Model {
    // 实现 NaviContract.Model 接口，承接导航数据的获取逻辑，不改动原有接口定义。
    override suspend fun getNavi(listener: RetrofitResponseListener<MutableList<Navi>>) {
        // 使用自定义的 launchCoroutine 扩展函数在协程中执行异步任务，避免阻塞主线程。
        return launchCoroutine({
            // 调用 RetrofitService 获取 ApiService 实例，并挂起函数 getNavi 发起网络请求。
            val baseNaviBean = RetrofitService.getApiService().getNavi()
            // 根据返回的 errorCode 判断请求是否成功，非 0 表示失败。
            if (baseNaviBean.errorCode != 0) {
                // 失败时通过 listener 回调 onError，传递错误码和错误信息给上层处理。
                listener.onError(baseNaviBean.errorCode, baseNaviBean.errorMsg)
            } else {
                // 成功时通过 listener 回调 onSuccess，将获取到的数据列表传递给 View 显示。
                listener.onSuccess(baseNaviBean.data)
            }
        }, onError = { e: Throwable ->
            // 对网络或其他异常进行统一捕获，并打印堆栈信息，方便调试和日志记录，同时回调给 UI。
            e.printStackTrace()
            listener.onError(-1, "网络请求失败: ${e.message}")
        })
    }
}
