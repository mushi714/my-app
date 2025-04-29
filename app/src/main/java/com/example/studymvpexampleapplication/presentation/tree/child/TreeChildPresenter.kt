package com.example.studymvpexampleapplication.presentation.tree.child

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Article
import kotlinx.coroutines.launch

// TreeChildPresenter：处理子树页面的 Presenter，实现 TreeChildContract.Presenter 接口
class TreeChildPresenter(view: TreeChildContract.View) :
// 继承自通用 BasePresenter，关联 View 和 Model
    BasePresenter<TreeChildContract.View, TreeChildContract.Model>(view),
    TreeChildContract.Presenter {  // 实现契约中的 Presenter 方法

    /**
     * 创建并返回 Model 层实例，用于发起网络请求
     */
    override fun createModel(): TreeChildContract.Model = TreeChildModel()

    /**
     * 请求获取当前分类的文章列表
     * @param page 页码，用于分页加载
     * @param cid 分类 ID
     */
    override fun getTreeChild(page: Int, cid: Int) {
        // 在 CoroutineScope 中启动协程，防止阻塞主线程
        coroutineScope.launch {
            mModel?.getTreeChild(page, cid, object : RetrofitResponseListener<Article> {
                /**
                 * 网络请求成功时回调，将结果传递给 View 层
                 */
                override fun onSuccess(response: Article) {
                    mView?.get()?.getTreeChildSuccess(response)
                }

                /**
                 * 网络请求失败时回调，将错误信息传递给 View 层
                 */
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getTreeChildError(errorMessage)
                }

            })
        }
    }

    /**
     * 请求加载更多文章（分页）
     * @param page 下一页页码
     * @param cid 分类 ID
     */
    override fun getTreeMoreChild(page: Int, cid: Int) {
        coroutineScope.launch {
            mModel?.getTreeMoreChild(page, cid, object : RetrofitResponseListener<Article> {
                override fun onSuccess(response: Article) {
                    mView?.get()?.getTreeMoreChildSuccess(response)
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getTreeMoreChildError(errorMessage)
                }

            })
        }
    }

    /**
     * 请求收藏文章
     * @param id 文章 ID
     */
    override fun collect(id: Int) {
        coroutineScope.launch {
            mModel?.collect(id, object : RetrofitResponseListener<String> {
                /**
                 * 收藏成功时回调，通知 View 更新收藏状态
                 */
                override fun onSuccess(response: String) {
                    mView?.get()?.collectSuccess("收藏成功")
                }

                /**
                 * 收藏失败时回调，特殊处理登出状态
                 * @param errorCode 错误码，-1001 通常表示未登录或登录过期
                 */
                override fun onError(errorCode: Int, errorMessage: String) {
                    if (errorCode == -1001) {
                        // 未登录或登录失效，提示 View 跳转登录
                        mView?.get()?.login(errorMessage)
                    } else {
                        // 其他错误直接通知收藏失败
                        mView?.get()?.collectError("收藏失败")
                    }
                }

            })
        }
    }

    /**
     * 请求取消收藏文章
     * @param id 文章 ID
     */
    override fun unCollect(id: Int) {
        coroutineScope.launch {
            mModel?.collect(id, object : RetrofitResponseListener<String> {
                /**
                 * 取消收藏成功时回调，通知 View 更新状态
                 */
                override fun onSuccess(response: String) {
                    mView?.get()?.unCollectSuccess("取消收藏成功")
                }

                /**
                 * 取消收藏失败时回调，通知 View 展示失败提示
                 */
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.unCollectError("取消收藏失败")
                }

            })
        }
    }
}
