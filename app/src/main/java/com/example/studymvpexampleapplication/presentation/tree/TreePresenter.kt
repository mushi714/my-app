package com.example.studymvpexampleapplication.presentation.tree

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Tree
import kotlinx.coroutines.launch

// TreePresenter：MVP 模式下的 Presenter 实现类
class TreePresenter(view: TreeContract.View) :
// 继承自通用的 BasePresenter，管理 View 和 Model 的生命周期
    BasePresenter<TreeContract.View, TreeContract.Model>(view),
    // 实现 TreeContract 中定义的 Presenter 接口
    TreeContract.Presenter {

    // 创建并返回具体的 Model 实例，用于执行数据请求
    override fun createModel(): TreeContract.Model = TreeModel()

    // 发起获取树形数据的请求
    override fun getTree() {
        // 在协程作用域内启动新的协程，不阻塞当前线程
        coroutineScope.launch {
            // 调用 Model 层的 getTree 方法，并传入回调监听器
            mModel?.getTree(object : RetrofitResponseListener<MutableList<Tree>> {
                // 当数据请求成功时，通知 View 层展示数据
                override fun onSuccess(response: MutableList<Tree>) {
                    mView?.get()?.getTreeSuccess(response)
                }

                // 当数据请求失败时，通知 View 层展示错误信息
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getTreeError(errorMessage)
                }
            })
        }
    }
}
