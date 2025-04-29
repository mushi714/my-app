package com.example.studymvpexampleapplication.presentation.home

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Article
import com.example.studymvpexampleapplication.data.bean.Banner
import kotlinx.coroutines.launch

/**
 * HomePresenter：MVP 模式中 Home 模块的 Presenter 实现
 * 负责从 Model 获取数据，并将结果回调给 View
 *
 * @param view 关联的 View 层接口，用于回调 UI 更新
 */
class HomePresenter(view: HomeContract.View) :
    BasePresenter<HomeContract.View, HomeContract.Model>(view), HomeContract.Presenter {

    /**
     * 创建并返回对应的 Model 实例
     */
    override fun createModel(): HomeContract.Model = HomeModel()

    /**
     * 获取轮播图数据
     * - 在协程作用域中启动异步请求
     * - 请求成功时调用 View.getBannerSuccess()
     * - 请求失败时调用 View.getBannerError()
     */
    override fun getBanner() {
        coroutineScope.launch {
            mModel?.getBanner(object : RetrofitResponseListener<MutableList<Banner>> {
                override fun onSuccess(response: MutableList<Banner>) {
                    mView?.get()?.getBannerSuccess(response)
                }
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getBannerError(errorMessage)
                }
            })
        }
    }

    /**
     * 获取首页文章列表
     * @param page 页码，从 0 或 1 开始
     */
    override fun getArticleList(page: Int) {
        coroutineScope.launch {
            mModel?.getArticleList(page, object : RetrofitResponseListener<Article> {
                override fun onSuccess(response: Article) {
                    mView?.get()?.getArticleListSuccess(response)
                }
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getArticleListError(errorMessage)
                }
            })
        }
    }

    /**
     * 加载更多文章（分页加载下一页）
     * @param page 下一页页码
     */
    override fun getMoreArticleList(page: Int) {
        coroutineScope.launch {
            mModel?.getArticleList(page, object : RetrofitResponseListener<Article> {
                override fun onSuccess(response: Article) {
                    mView?.get()?.getMoreArticleListSuccess(response)
                }
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.getMoreArticleListError(errorMessage)
                }
            })
        }
    }

    /**
     * 收藏文章
     * @param id 文章 ID
     * - 如果返回错误码 -1001（未登录），调用 View.login() 提示登录
     * - 其他错误调用 View.collectError()
     */
    override fun collect(id: Int) {
        coroutineScope.launch {
            mModel?.collect(id, object : RetrofitResponseListener<String> {
                override fun onSuccess(response: String) {
                    mView?.get()?.collectSuccess(response)
                }
                override fun onError(errorCode: Int, errorMessage: String) {
                    if (errorCode == -1001) {
                        // 用户未登录，触发登录流程
                        mView?.get()?.login(errorMessage)
                    } else {
                        mView?.get()?.collectError(errorMessage)
                    }
                }
            })
        }
    }

    /**
     * 取消收藏文章
     * @param id 文章 ID
     */
    override fun unCollect(id: Int) {
        coroutineScope.launch {
            mModel?.unCollect(id, object : RetrofitResponseListener<String> {
                override fun onSuccess(response: String) {
                    mView?.get()?.unCollectSuccess(response)
                }
                override fun onError(errorCode: Int, errorMessage: String) {
                    mView?.get()?.unCollectError(errorMessage)
                }
            })
        }
    }
}
