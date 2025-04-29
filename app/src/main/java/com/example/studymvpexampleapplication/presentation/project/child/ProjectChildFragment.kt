package com.example.studymvpexampleapplication.presentation.project.child

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.adapter.ProjectChildAdapter
import com.example.studymvpexampleapplication.base.mvp.BaseMVPFragment
import com.example.studymvpexampleapplication.data.bean.DataX
import com.example.studymvpexampleapplication.data.bean.ProjectChild
import com.example.studymvpexampleapplication.databinding.FragmentProjectChildBinding
import com.example.studymvpexampleapplication.presentation.detail.DetailActivity
import com.yechaoa.yutilskt.show

class ProjectChildFragment :
    BaseMVPFragment<FragmentProjectChildBinding, ProjectChildContract.Presenter>({
        FragmentProjectChildBinding.inflate(it)
    }), ProjectChildContract.View, OnLoadMoreListener,
    OnItemClickListener {

    companion object {
        private const val TOTAL_COUNTER = 20   // 每次加载的数据量
        private var CURRENT_SIZE = 0          // 当前已加载的数据数量，用于判断是否还有更多
        private var CURRENT_PAGE = 1          // 当前请求的页码，分页加载时递增

        const val CID: String = "cid"         // Fragment 参数 key，用于传递分类 ID

        /**
         * 创建 Fragment 的静态工厂方法，推荐使用 newInstance 模式，
         * 将参数封装到 Bundle 并通过 setArguments 传递，保证重建时参数不丢失
         */
        fun newInstance(cid: Int): ProjectChildFragment {
            val projectChildFragment = ProjectChildFragment()
            val bundle = Bundle().apply {
                putInt(CID, cid)            // 将分类 ID 放入 Bundle
            }
            projectChildFragment.arguments = bundle
            return projectChildFragment
        }
    }

    private var mCid: Int = 0                      // 存储当前分类 ID
    private lateinit var mDataList: MutableList<DataX>   // 文章数据列表，用于 Adapter 数据源
    private lateinit var mProjectChildAdapter: ProjectChildAdapter  // RecyclerView 的 Adapter

    override fun createPresenter(): ProjectChildContract.Presenter {
        // 创建并绑定 Presenter 实例，将当前 Fragment 作为 View 注入
        return ProjectChildPresenter(this)
    }

    override fun initView() {
        // 给 RecyclerView 添加分割线装饰，实现垂直列表间隔
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.root.context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun initData() {
        // 从 arguments 获取传入的分类 ID，并调用 Presenter 发起首页数据请求
        mCid = arguments?.getInt(CID)!!
        mPresenter.getProjectChild(CURRENT_PAGE, mCid)
    }

    override fun allClick() {
        // 如需处理其他点击事件，可在此注册监听
    }

    override fun onLoadMore() {
        // 上拉加载更多回调：根据已加载数量判断是否需要结束加载或请求下一页
        if (CURRENT_SIZE < TOTAL_COUNTER) {
            // 数据不足一页，提示没有更多，并结束加载状态
            mProjectChildAdapter.loadMoreModule.loadMoreEnd(true)
        } else {
            // 增加页码并请求下一页数据
            CURRENT_PAGE++
            mPresenter.getProjectMoreChild(CURRENT_PAGE, mCid)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        // 列表项点击回调，通过 Intent 跳转到详情页面，并传递 URL 和标题参数
        val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
            putExtra(DetailActivity.WEB_URL, mDataList[position].link)
            putExtra(DetailActivity.WEB_TITLE, mDataList[position].title)
        }
        startActivity(intent)
    }

    override fun getProjectChildSuccess(projectChild: ProjectChild) {
        // 首次数据请求成功：更新当前加载数量与数据列表，并初始化 Adapter
        CURRENT_SIZE = projectChild.datas.size
        mDataList = projectChild.datas

        mProjectChildAdapter = ProjectChildAdapter().apply {
            setOnItemClickListener(this@ProjectChildFragment)  // 设置条目点击监听
            loadMoreModule.setOnLoadMoreListener(this@ProjectChildFragment)  // 设置加载更多监听
        }
        binding.recyclerView.adapter = mProjectChildAdapter
        mProjectChildAdapter.setList(mDataList)  // 提交数据刷新列表
    }

    override fun getProjectChildError(errorMessage: String) {
        // 数据请求失败时在 UI 上提示错误信息
        show(errorMessage)
    }

    override fun getProjectMoreChildSuccess(projectChild: ProjectChild) {
        // 分页加载更多成功：更新加载数量，追加数据，并结束加载状态
        CURRENT_SIZE = projectChild.datas.size
        mDataList.addAll(projectChild.datas)
        mProjectChildAdapter.addData(projectChild.datas)
        mProjectChildAdapter.loadMoreModule.loadMoreComplete()
    }

    override fun getProjectMoreChildError(errorMessage: String) {
        // 分页加载失败时在 UI 上提示错误信息
        show(errorMessage)
    }

}
