package com.example.studymvpexampleapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
/**
 * 抽象 BaseActivity，使用泛型 ViewBinding 简化布局绑定
 *
 * @param VB 具体的 ViewBinding 类型
 * @param block 一个用于创建 VB 的 lambda，通常传入 { inflater -> XxxActivityBinding.inflate(inflater) }
 */
abstract class BaseActivity<VB : ViewBinding>(val block: (LayoutInflater) -> VB) :
    AppCompatActivity() {
    // 内部持有一个可空的绑定对象，防止在 onDestroy 后继续引用
    private var _binding: VB? = null
    /**
     * 对外公开的 binding 对象，只有在 onCreate 后到 onDestroy 之前可用
     * 访问时若 _binding 为 null，会抛出 IllegalStateException 提示已被销毁
     */
    val binding: VB
        get() = requireNotNull(_binding) { "biding 已被销毁" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 在 Activity 创建时，通过传入的 block 初始化 ViewBinding
        _binding = block(layoutInflater)
        // 将 binding.root 设置为当前 Activity 的内容视图
        setContentView(binding.root)
        // 调用各子类需实现的生命周期方法
        initView()
        initData()
        allClick()
    }

    /**
     * 初始化视图 例如 findView、RecyclerView 布局管理器等
     * */
    abstract fun initView()

    /**
     * 初始化或加载数据，例如网络请求、本地数据库查询等
     * */
    abstract fun initData()

    /**
     * 设置所有的点击事件回调监听
     * */
    abstract fun allClick()

    /**
     * 设置标题栏标题
     *
     * @param title 标题文本
     */
    protected fun setBarTitle(title: String) {
        supportActionBar?.title = title
    }

    /**
     * 启用默认返回按钮 (左上角箭头)，并响应点击
     */
    protected fun setBackEnabled() {
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     * 菜单项点击回调，处理左上角 Home/箭头返回
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 在 Activity 销毁时，清空 binding 引用，防止内存泄漏
        _binding = null
    }
}