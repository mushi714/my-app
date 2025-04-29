package com.example.studymvpexampleapplication.presentation.project

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Project
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.launchCoroutine

class ProjectModel : ProjectContract.Model {
    // 实现 ProjectContract.Model 接口，负责项目数据获取逻辑
    override fun getProject(listener: RetrofitResponseListener<MutableList<Project>>) {
        // 使用自定义的 launchCoroutine 扩展函数，在协程中启动异步任务，fire-and-forget 不阻塞调用方
        return launchCoroutine({
            // 通过 RetrofitService 单例获取 ApiService 实例，调用 getProject() 发起网络请求获取项目列表
            val baseProjectBean = RetrofitService.getApiService().getProject()
            // 根据返回的 errorCode 判断请求是否成功，非 0 表示业务错误
            if (baseProjectBean.errorCode != 0) {
                // 请求失败时回调 onError，将错误码与错误信息传递给调用者
                listener.onError(baseProjectBean.errorCode, baseProjectBean.errorMsg)
            } else {
                // 请求成功时回调 onSuccess，将服务端返回的数据列表传递给调用者
                listener.onSuccess(baseProjectBean.data)
            }
        }, onError = { e: Throwable ->
            // 捕获网络或其他异常，并打印堆栈信息以便调试
            e.printStackTrace()
        })
    }

}
