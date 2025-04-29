package com.example.studymvpexampleapplication.presentation.navi

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Navi
import kotlinx.coroutines.launch

class NaviPresenter(view: NaviContract.View) :
    BasePresenter<NaviContract.View, NaviContract.Model>(view), NaviContract.Presenter {
    // 将 View 注入到 BasePresenter 中，确保 Presenter 拥有对 View 的引用和生命周期控制。
    override fun createModel(): NaviContract.Model = NaviModel()
    // 创建并返回具体的 Model 实例，用于执行数据请求逻辑。

    override fun getNavi() {
        // 在 Presenter 的 CoroutineScope 中启动协程，保障结构化并发，避免内存泄漏。
        coroutineScope.launch {
            // 调用 Model 的 getNavi 方法发起异步网络请求，并通过回调接口处理结果。
            mModel?.getNavi(object : RetrofitResponseListener<MutableList<Navi>> {
                override fun onSuccess(response: MutableList<Navi>) {
                    // 请求成功时，通过 View 接口回调并传递数据，更新界面展示。
                    mView?.get()?.getNaviSuccess(response)
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    // 请求失败时，通过 View 接口回调并传递错误信息，用于界面提示或错误处理。
                    mView?.get()?.getNaviError(errorMessage)
                }

            })
        }
    }

}
