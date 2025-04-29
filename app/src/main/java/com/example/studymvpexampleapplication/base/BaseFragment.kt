package com.example.studymvpexampleapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
/**
 * 抽象 BaseFragment，使用泛型 ViewBinding 简化布局绑定
 *
 * @param VB 具体的 ViewBinding 类型
 * @param block 一个用于创建 VB 的 lambda，通常传入 { inflater -> XxxFragmentBinding.inflate(inflater, container, false) }
 */
abstract class BaseFragment<VB : ViewBinding>(val block: (LayoutInflater) -> VB) :
    Fragment() {

    // 内部持有一个可空的 binding 对象，防止在视图销毁后继续引用
    private var _binding: VB? = null

    /**
     * 对外公开的 binding 对象，只有在 onCreateView 到 onDestroyView 之间可用
     * 访问时若 _binding 为 null，会抛出 IllegalStateException 提示已被销毁
     */
    val binding: VB
        get() = requireNotNull(_binding) { "biding 已被销毁" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 在创建视图时，通过传入的 block 初始化 ViewBinding
        _binding = block(layoutInflater)
        // 返回根视图给 Fragment 宿主显示
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 视图创建后，调用子类需实现的初始化方法
        initView()
        initData()
        allClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 在视图销毁时，清空 binding 引用，防止内存泄漏
        _binding = null
    }

    /**
     * 初始化视图组件，例如设置 RecyclerView 的 adapter 或初始化 UI 控件状态
     */
    protected abstract fun initView()

    /**
     * 初始化或加载数据，例如发起网络请求或读取本地数据库
     */
    protected abstract fun initData()

    /**
     * 设置所有的点击事件回调监听
     */
    protected abstract fun allClick()
}