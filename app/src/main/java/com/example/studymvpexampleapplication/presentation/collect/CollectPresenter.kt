package com.example.studymvpexampleapplication.presentation.collect

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Collect
import kotlinx.coroutines.launch

class CollectPresenter(view: CollectContract.View) :
// 继承自 BasePresenter，绑定 View 与 Model 的通用逻辑，并实现 CollectContract.Presenter 接口
    BasePresenter<CollectContract.View, CollectContract.Model>(view),
    CollectContract.Presenter {

    /**
     * 创建并返回当前 Presenter 对应的 Model 实例
     */
    override fun createModel(): CollectContract.Model {
        return CollectModel()
    }

    /**
     * 取消收藏
     * @param id 收藏记录的唯一 ID
     * @param originId 原始文章 ID；当在“我的收藏”页面取消时，可为 -1
     */
    override fun unCollect(id: Int, originId: Int) {
        // 在 BasePresenter 提供的 coroutineScope 中启动协程，保证生命周期安全
        coroutineScope.launch {
            // 调用 Model 层的 unCollect 方法，并通过 RetrofitResponseListener 回调结果
            mModel?.unCollect(id, originId, object : RetrofitResponseListener<String> {
                // 请求成功时调用，将成功消息传递给 View 层
                override fun onSuccess(response: String) {
                    mView?.get()?.unCollectSuccess(response)
                }

                // 请求失败时调用，将错误信息传递给 View 层
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.unCollectError(errorMessage)
                }
            })
        }
    }

    /**
     * 获取收藏列表（首页或指定页）
     * @param page 分页页码，从 0 开始
     */
    override fun getCollectList(page: Int) {
        coroutineScope.launch {
            // 调用 Model 层获取收藏列表并监听回调
            mModel?.getCollectList(page, object : RetrofitResponseListener<Collect> {
                // 请求成功时，传递数据给 View 更新 UI
                override fun onSuccess(response: Collect) {
                    mView?.get()?.getCollectListSuccess(response)
                }
                // 请求失败时，根据错误码判断是否需要跳转登录或显示错误
                override fun onError(errorCode: Int, errorMessage: String) {
                    if (errorCode == -1001) {
                        // 特殊错误码表示未登录，通知 View 执行登录流程
                        mView?.get()?.login(errorMessage)
                    } else {
                        // 其他错误，通知 View 显示错误信息
                        mView?.get()?.getCollectListError(errorMessage)
                    }
                }
            })
        }
    }

    /**
     * 分页加载更多收藏列表
     * @param page 下一页页码
     */
    override fun getCollectMoreList(page: Int) {
        coroutineScope.launch {
            // 调用 Model 层获取下一页数据
            mModel?.getCollectMoreList(page, object : RetrofitResponseListener<Collect> {
                // 成功时通知 View 追加数据
                override fun onSuccess(response: Collect) {
                    mView?.get()?.getCollectMoreListSuccess(response)
                }
                // 失败时通知 View 显示错误
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getCollectMoreListError(errorMessage)
                }
            })
        }
    }
}
