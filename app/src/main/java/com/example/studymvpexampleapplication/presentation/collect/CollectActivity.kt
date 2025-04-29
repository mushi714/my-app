package com.example.studymvpexampleapplication.presentation.collect

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.example.studymvpexampleapplication.adapter.CollectAdapter
import com.example.studymvpexampleapplication.base.mvp.BaseMVPActivity
import com.example.studymvpexampleapplication.data.bean.Collect
import com.example.studymvpexampleapplication.data.bean.CollectDetail
import com.example.studymvpexampleapplication.databinding.ActivityCollectBinding
import com.example.studymvpexampleapplication.presentation.detail.DetailActivity
import com.example.studymvpexampleapplication.presentation.login.LoginActivity
import com.yechaoa.yutilskt.show

/**
 * 收藏列表 Activity
 * 继承 BaseMVPActivity，使用 MVP 架构管理业务逻辑与 UI
 * 实现了列表点击、分页加载、下拉刷新和收藏取消等功能
 */
class CollectActivity : BaseMVPActivity<ActivityCollectBinding, CollectContract.Presenter>(
    { ActivityCollectBinding.inflate(it) }
), CollectContract.View,
    OnItemClickListener,          // 列表项点击监听
    OnLoadMoreListener,           // 列表加载更多监听
    SwipeRefreshLayout.OnRefreshListener,  // 下拉刷新监听
    OnItemChildClickListener {    // 列表子项点击监听（用于取消收藏）

    // Adapter 与数据源
    private lateinit var mCollectAdapter: CollectAdapter
    private lateinit var mDataList: MutableList<CollectDetail>
    private var mPosition: Int = 0   // 记录当前操作的条目位置

    companion object {
        private const val TOTAL_COUNTER = 20 // 每页加载数量
        private var CURRENT_SIZE = 0         // 当前已加载数量
        private var CURRENT_PAGE = 0         // 当前页码
    }

    /**
     * 视图初始化：设置标题、返回按钮、下拉刷新并请求首页数据
     */
    override fun initView() {
        setBarTitle("收藏列表")        // 设置标题
        setBackEnabled()               // 启用返回按钮
        initSwipeRefreshLayout()       // 初始化 SwipeRefreshLayout 样式与监听
        mPresenter.getCollectList(CURRENT_PAGE) // 请求第一页收藏列表
    }

    // 数据初始化：无额外逻辑
    override fun initData() {}

    // 事件注册：无额外逻辑
    override fun allClick() {}

    // 绑定 Presenter
    override fun createPresenter(): CollectContract.Presenter {
        return CollectPresenter(this)
    }

    // 返回当前 Activity 用于 Presenter 内部使用
    override fun getActivity(): Activity {
        return this
    }

    /**
     * 列表项点击：跳转到详情页面
     */
    override fun onItemClick(
        adapter: BaseQuickAdapter<*, *>, view: View, position: Int
    ) {
        val detail = mDataList[position]
        Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.WEB_TITLE, detail.title)
            putExtra(DetailActivity.WEB_URL, detail.link)
        }.also { startActivity(it) }
    }

    /**
     * 上拉加载更多：根据已加载数量判断是否还有更多或请求下一页
     */
    override fun onLoadMore() {
        if (CURRENT_SIZE < TOTAL_COUNTER) {
            // 数据未满一页，结束加载，显示无更多提示
            mCollectAdapter.loadMoreModule.loadMoreEnd(true)
        } else {
            // 请求下一页数据
            CURRENT_PAGE++
            mPresenter.getCollectMoreList(CURRENT_PAGE)
        }
    }

    /**
     * 下拉刷新：重置页码并重新加载首 页
     */
    override fun onRefresh() {
        binding.swipeRefresh.postDelayed({
            CURRENT_PAGE = 0
            mPresenter.getCollectList(CURRENT_PAGE)
            binding.swipeRefresh.isRefreshing = false
        }, 1500)
    }

    /**
     * 子项按钮点击（取消收藏）：记录位置并调用 Presenter 取消收藏
     */
    override fun onItemChildClick(
        adapter: BaseQuickAdapter<*, *>, view: View, position: Int
    ) {
        mPosition = position
        // originId 用于区分是否有原始文章 ID
        val originId = mDataList[position].originId.takeIf { it > 0 } ?: -1
        mPresenter.unCollect(mDataList[position].id, originId)
    }

    /**
     * 取消收藏成功：移除列表项并提示
     */
    override fun unCollectSuccess(successMessage: String) {
        show(successMessage)
        mCollectAdapter.removeAt(mPosition)
    }

    /**
     * 取消收藏失败：提示错误信息
     */
    override fun unCollectError(errorMessage: String) {
        show(errorMessage)
    }

    /**
     * 获取收藏列表成功：初始化 Adapter 并展示数据
     */
    override fun getCollectListSuccess(collect: Collect) {
        CURRENT_SIZE = collect.datas.size
        mDataList = collect.datas
        // 初始化 Adapter，配置动画、点击与加载更多
        mCollectAdapter = CollectAdapter().apply {
            animationEnable = true
            setOnItemClickListener(this@CollectActivity)
            setOnItemChildClickListener(this@CollectActivity)
            loadMoreModule.setOnLoadMoreListener(this@CollectActivity)
        }
        // RecyclerView 布局与 Adapter 绑定
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            )
            adapter = mCollectAdapter
        }
        mCollectAdapter.setList(mDataList)
    }

    /**
     * 获取收藏列表失败：提示错误
     */
    override fun getCollectListError(errorMessage: String) {
        show(errorMessage)
    }

    /**
     * 分页加载更多成功：追加数据并结束加载状态
     */
    override fun getCollectMoreListSuccess(collect: Collect) {
        CURRENT_SIZE = collect.datas.size
        mDataList.addAll(collect.datas)
        mCollectAdapter.addData(collect.datas)
        mCollectAdapter.loadMoreModule.loadMoreComplete()
    }

    /**
     * 分页加载更多失败：提示错误
     */
    override fun getCollectMoreListError(errorMessage: String) {
        show(errorMessage)
    }

    /**
     * 未登录回调：弹窗提示并跳转或关闭
     */
    override fun login(msg: String) {
        show(msg)
        AlertDialog.Builder(this@CollectActivity).apply {
            setTitle("提示")
            setMessage(msg)
            setPositiveButton("确定") { _, _ ->
                startActivity(Intent(this@CollectActivity, LoginActivity::class.java))
            }
            setNegativeButton("取消") { _, _ -> finish() }
        }.create().show()
    }

    /**
     * 初始化下拉刷新控件样式和监听
     */
    private fun initSwipeRefreshLayout() {
        binding.swipeRefresh.apply {
            setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
            )
            setOnRefreshListener(this@CollectActivity)
        }
    }
}