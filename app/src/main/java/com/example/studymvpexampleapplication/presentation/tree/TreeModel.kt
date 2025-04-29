package com.example.studymvpexampleapplication.presentation.tree

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Tree
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.launchCoroutine

// TreeContract.Model 的具体实现类，负责发起获取树形数据的网络请求
class TreeModel : TreeContract.Model {

    /**
     * 从远程接口异步获取树形结构数据
     * @param listener 用于回调请求结果的监听器
     */
    override suspend fun getTree(listener: RetrofitResponseListener<MutableList<Tree>>) {
        // 使用自定义的协程启动方法，封装网络请求与异常处理
        return launchCoroutine({

            // 调用 RetrofitService 获取 API 接口并执行 getTree 请求
            val baseTreeBean = RetrofitService.getApiService().getTree()

            // 根据返回结果的 errorCode 判断请求是否成功
            if (baseTreeBean.errorCode != 0) {
                // 当 errorCode 不为 0 时，调用 onError 回调并传入错误码与错误信息
                listener.onError(baseTreeBean.errorCode, baseTreeBean.errorMsg)
            } else {
                // 当请求成功时，将数据列表通过 onSuccess 回调传递给调用者
                listener.onSuccess(baseTreeBean.data)
            }

            // 协程出错时的统一处理
        }, onError = { e: Throwable ->
            // 打印异常堆栈，便于调试
            e.printStackTrace()
        })
    }
}
