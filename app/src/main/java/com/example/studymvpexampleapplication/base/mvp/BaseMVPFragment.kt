package com.example.studymvpexampleapplication.base.mvp

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.studymvpexampleapplication.base.BaseFragment

/**
 * 基于 MVP 模式的 Fragment 抽象基类，继承自 BaseFragment，
 * 用于统一管理 Presenter 的创建与生命周期。
 *
 * @param VB ViewBinding 类型，用于布局绑定
 * @param P Presenter 类型，实现 BaseContract.IBasePresenter
 * @param block 用于生成 ViewBinding 的 lambda 表达式，通常为 XxxFragmentBinding.inflate
 */
abstract class BaseMVPFragment<VB : ViewBinding, P : BaseContract.IBasePresenter>(
    block: (LayoutInflater) -> VB
) : BaseFragment<VB>(block) {

    /**
     * 延迟初始化 Presenter 实例，首次使用时通过 createPresenter() 方法创建。
     */
    protected val mPresenter: P by lazy {
        createPresenter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 视图销毁时，断开 Presenter 与 View 的关联，避免内存泄漏
        mPresenter.detachView()
    }

    /**
     * 子类必须实现该方法，用于提供具体的 Presenter 对象。
     *
     * @return 创建并返回一个具体的 Presenter 实例
     */
    protected abstract fun createPresenter(): P

    /**
     * 获取当前 Presenter 实例，便于子类调用
     *
     * @return Presenter 实例
     */
    protected fun getPresenter(): P = mPresenter
}