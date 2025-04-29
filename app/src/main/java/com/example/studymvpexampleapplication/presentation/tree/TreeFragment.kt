package com.example.studymvpexampleapplication.presentation.tree

import android.content.Intent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studymvpexampleapplication.adapter.TreeAdapter
import com.example.studymvpexampleapplication.base.mvp.BaseMVPFragment
import com.example.studymvpexampleapplication.data.bean.Children
import com.example.studymvpexampleapplication.data.bean.Tree
import com.example.studymvpexampleapplication.databinding.FragmentTreeBinding
import com.example.studymvpexampleapplication.presentation.tree.child.TreeChildActivity
import com.yechaoa.yutilskt.show


// TreeFragment：MVP 模式下用于展示 Tree 列表的 Fragment，
// 继承自 BaseMVPFragment 并实现了 TreeContract.View 接口
class TreeFragment :
    BaseMVPFragment<FragmentTreeBinding, TreeContract.Presenter>({ FragmentTreeBinding.inflate(it) }),
    TreeContract.View {   // 实现 View 层回调接口

    // 当前选中的父项位置，用于在子标签点击时获取对应的父节点数据
    private var mPosition: Int = 0

    // 存储从 Presenter 获取到的 Tree 数据列表
    private lateinit var mTreeList: MutableList<Tree>

    // 初始化视图：在 Fragment 创建后调用，用于配置 UI 组件
    override fun initView() {
        initSwipeRefreshLayout()  // 初始化下拉刷新组件
    }

    // 初始化或恢复数据：在视图初始化后调用，用于发起数据请求
    override fun initData() {
        mPresenter.getTree()  // 调用 Presenter 执行网络请求获取 Tree 数据
    }

    // 统一点击事件入口（此处留空，如需可复用）
    override fun allClick() {
    }

    // 创建 Presenter 实例的方法，实现契约中指定的工厂方法
    override fun createPresenter(): TreeContract.Presenter {
        return TreePresenter(this)  // 将当前 Fragment 作为 View 传入 Presenter
    }

    // 配置下拉刷新控件的样式和刷新回调
    private fun initSwipeRefreshLayout() {
        binding.swipeRefresh.setColorSchemeResources(
            android.R.color.holo_blue_bright,   // 刷新进度条颜色
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light
        )
        // 下拉刷新触发时回调
        binding.swipeRefresh.setOnRefreshListener {
            // 延迟用于模拟网络请求时长
            binding.swipeRefresh.postDelayed({
                mPresenter.getTree()                   // 重新请求数据
                binding.swipeRefresh.isRefreshing = false // 关闭刷新动画
            }, 1500)
        }
    }

    // Presenter 获取 Tree 数据成功后的回调
    override fun getTreeSuccess(treeMutableList: MutableList<Tree>) {
        mTreeList = treeMutableList  // 保存数据到成员变量

        // 创建并配置 TreeAdapter
        val treeAdapter = TreeAdapter().apply {
            // 父项点击事件：控制展开/收起逻辑
            setOnItemClickListener { _, _, position ->
                mPosition = position  // 记录当前点击的父项位置

                // 如果当前项已展开，则全部折叠；否则先折叠所有，再展开当前项
                if (treeMutableList[position].isShow) {
                    treeMutableList.forEach { it.isShow = false }
                } else {
                    treeMutableList.forEach { it.isShow = false }
                    treeMutableList[position].isShow = true
                }
                notifyDataSetChanged()  // 刷新列表，应用展开/折叠状态
            }

            // 子标签点击事件：跳转到 TreeChildActivity 并传递对应数据
            setOnChildClickListener(object : TreeAdapter.OnChildClickListener {
                override fun onChildClick(
                    parentPos: Int,
                    childPos: Int,
                    child: Children
                ) {
                    // 构建跳转 Intent，传入标题（父节点 name）、子列表及子项位置
                    val intent = Intent(binding.root.context, TreeChildActivity::class.java).apply {
                        putExtra(TreeChildActivity.TITLE, mTreeList[parentPos].name)
                        putExtra(TreeChildActivity.CID, mTreeList[parentPos].children)
                        putExtra(TreeChildActivity.POSITION, childPos)
                    }
                    startActivity(intent)  // 启动子页面
                }
            })
        }

        // 为 RecyclerView 添加垂直分割线
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.root.context,
                LinearLayoutManager.VERTICAL
            )
        )

        // 绑定适配器并提交数据列表
        binding.recyclerView.adapter = treeAdapter
        treeAdapter.setList(treeMutableList)
    }

    // Presenter 获取数据失败时的回调，展示错误信息
    override fun getTreeError(errorMessage: String) {
        show(errorMessage) // 调用基类方法（如 Toast）提示用户
    }
}

