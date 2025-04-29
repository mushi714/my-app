package com.example.studymvpexampleapplication.base.mvp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import java.lang.ref.WeakReference

/**
 * BasePresenter 抽象类，封装了 MVP 架构中 Presenter 层的通用逻辑：
 *  - 使用弱引用持有 View，防止内存泄漏
 *  - 在初始化时创建 Model 实例
 *  - 提供协程作用域用于异步操作，并在解绑时取消
 */
abstract class BasePresenter<V : BaseContract.IBaseView, M>(view: V) :
    BaseContract.IBasePresenter {

    /** 对 View 的弱引用，避免强引用导致 Activity/Fragment 无法回收 */
    var mView: WeakReference<V?>? = null

    /** 对 Model 的引用，用于执行业务逻辑 */
    var mModel: M? = null

    /** 协程作用域，指定在主线程，适合更新 UI 操作 */
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        // 在构造时绑定 View 并创建对应的 Model
        attachView(view)
        mModel = createModel()
    }

    /**
     * 创建并返回当前 Presenter 对应的 Model 实例
     * 子类必须实现此方法，提供具体的业务 Model
     */
    abstract fun createModel(): M

    /**
     * 将 View 与 Presenter 关联，使用 WeakReference 包装
     *
     * @param view 具体的 View 实现
     */
    open fun attachView(view: V) {
        mView = WeakReference(view)
    }

    /**
     * 获取当前关联的 View 实例
     *
     * @return 视图实例或 null（若已被回收）
     */
    open fun getView(): V? = mView?.get()

    /**
     * 检查 Presenter 是否仍与 View 建立了有效关联
     *
     * @return true：关联未断开且 View 未被回收；false：已断开或 View 为 null
     */
    override fun isViewAttach(): Boolean {
        return mView != null && mView?.get() != null
    }

    /**
     * 解除 Presenter 与 View 的关联，清理资源，并取消所有协程
     * 调用时机：通常在 Activity/Fragment 的 onDestroy 或 onDestroyView 中
     */
    override fun detachView() {
        // 清理对 View 的弱引用
        if (mView != null) {
            mView?.clear()
            mView = null
        }
        // 清理对 Model 的引用
        if (mModel != null) {
            mModel = null
        }
        // 取消所有在 coroutineScope 中启动的协程任务
        coroutineScope.cancel()
    }
}
