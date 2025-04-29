package com.example.studymvpexampleapplication.presentation.tree.child

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.example.studymvpexampleapplication.base.mvp.BaseMVPFragment
import com.example.studymvpexampleapplication.data.bean.Article
import com.example.studymvpexampleapplication.data.bean.ArticleDetail
import com.example.studymvpexampleapplication.databinding.FragmentTreeChildBinding
import com.example.studymvpexampleapplication.presentation.detail.DetailActivity
import com.example.studymvpexampleapplication.adapter.ArticleAdapter
import com.example.studymvpexampleapplication.presentation.login.LoginActivity
import com.yechaoa.yutilskt.ToastUtil
import com.yechaoa.yutilskt.show

// TreeChildFragment：展示子章节文章列表的 Fragment，支持下拉加载、分页、文章点击与收藏操作
class TreeChildFragment : BaseMVPFragment<FragmentTreeChildBinding, TreeChildContract.Presenter>({
    FragmentTreeChildBinding.inflate(it)
}), TreeChildContract.View, OnLoadMoreListener, OnItemClickListener,
    OnItemChildClickListener {

    companion object {
        // 每次请求的文章数量常量
        private const val TOTAL_COUNTER = 20
        // 已加载文章数量
        private var CURRENT_SIZE = 0
        // 当前请求页码
        private var CURRENT_PAGE = 0

        // Intent 传递的分类 ID Key
        const val CID: String = "cid"

        /**
         * 静态工厂方法，根据分类 ID 创建新的 Fragment 实例
         */
        fun newInstance(cid: Int): TreeChildFragment {
            val fragment = TreeChildFragment()
            val bundle = Bundle()
            bundle.putInt(CID, cid)
            fragment.arguments = bundle
            return fragment
        }
    }

    // 当前分类 ID，从 arguments 中获取
    private var mCid: Int = 0
    // 存储已加载的文章列表
    private lateinit var mDataList: MutableList<ArticleDetail>
    // BRVAH 文章列表适配器
    private lateinit var mArticleAdapter: ArticleAdapter
    // 当前点击或收藏操作的文章位置
    private var mPosition: Int = 0

    /**
     * 初始化视图，可在此设置 RecyclerView、刷新控件等（此处留空）
     */
    override fun initView() {
    }

    /**
     * 初始化数据，获取传递的分类 ID 并发起首屏数据请求
     */
    override fun initData() {
        // 从 arguments 中取出分类 ID
        mCid = arguments?.getInt(CID)!!
        // 请求第一页文章列表
        mPresenter.getTreeChild(CURRENT_PAGE, mCid)
    }

    // 统一点击事件入口（若有其他全局点击，可在此处理）
    override fun allClick() {
    }

    /**
     * 创建 Presenter 实例，用于后续业务调用
     */
    override fun createPresenter(): TreeChildContract.Presenter {
        return TreeChildPresenter(this)
    }

    /**
     * 分页加载更多回调，当列表滑动到底部时触发
     */
    override fun onLoadMore() {
        if (CURRENT_SIZE < TOTAL_COUNTER) {
            // 如果当前数据不足一页，直接结束加载并显示尾部
            mArticleAdapter.loadMoreModule.loadMoreEnd(true)
        } else {
            // 否则增加页码并请求下一页数据
            CURRENT_PAGE++
            mPresenter.getTreeMoreChild(CURRENT_PAGE, mCid)
        }
    }

    /**
     * 列表项点击回调，跳转到文章详情页面
     */
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
            putExtra(DetailActivity.WEB_URL, mDataList[position].link)
            putExtra(DetailActivity.WEB_TITLE, mDataList[position].title)
        }
        startActivity(intent)
    }

    /**
     * 子控件（收藏按钮）点击回调，执行收藏或取消收藏操作
     */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mPosition = position
        // 根据当前收藏状态调用不同的 Presenter 方法
        if (mDataList[position].collect) {
            mPresenter.unCollect(mDataList[position].id)
        } else {
            mPresenter.collect(mDataList[position].id)
        }
    }

    /**
     * 首次请求或刷新文章列表成功回调
     */
    override fun getTreeChildSuccess(article: Article) {
        // 更新已加载数量
        CURRENT_SIZE = article.datas.size
        // 保存数据源
        mDataList = article.datas
        // 初始化并配置 Adapter
        mArticleAdapter = ArticleAdapter().apply {
            setOnItemClickListener(this@TreeChildFragment)       // 注册列表项点击
            setOnItemChildClickListener(this@TreeChildFragment)  // 注册子控件点击
            loadMoreModule.setOnLoadMoreListener(this@TreeChildFragment) // 注册加载更多
        }
        // 绑定 Adapter 到 RecyclerView
        binding.recyclerView.adapter = mArticleAdapter
        // 提交数据列表
        mArticleAdapter.setList(article.datas)
    }

    /**
     * 首次请求或刷新文章列表失败回调，展示错误信息
     */
    override fun getTreeChildError(errorMessage: String) {
        show(errorMessage)
    }

    /**
     * 加载更多数据成功回调
     */
    override fun getTreeMoreChildSuccess(article: Article) {
        // 更新当前页数据量
        CURRENT_SIZE = article.datas.size
        // 将新数据追加到列表中
        mDataList.addAll(article.datas)
        mArticleAdapter.addData(article.datas)
        // 通知加载更多模块加载完成
        mArticleAdapter.loadMoreModule.loadMoreComplete()
    }

    /**
     * 加载更多数据失败回调，展示错误信息
     */
    override fun getTreeMoreChildError(errorMessage: String) {
        show(errorMessage)
    }

    /**
     * 收藏成功回调，更新数据状态并刷新列表
     */
    override fun collectSuccess(successMessage: String) {
        ToastUtil.showCenter(successMessage)
        mDataList[mPosition].collect = true
        mArticleAdapter.notifyDataSetChanged()
    }

    /**
     * 收藏失败回调，展示错误信息
     */
    override fun collectError(errorMessage: String) {
        show(errorMessage)
    }

    /**
     * 取消收藏成功回调，更新数据状态并刷新列表
     */
    override fun unCollectSuccess(successMessage: String) {
        ToastUtil.showCenter(successMessage)
        mDataList[mPosition].collect = false
        mArticleAdapter.notifyDataSetChanged()
    }

    /**
     * 取消收藏失败回调，展示错误信息
     */
    override fun unCollectError(errorMessage: String) {
        show(errorMessage)
    }

    /**
     * 未登录或登录失效时回调，弹出登录对话框引导用户登录
     */
    override fun login(msg: String) {
        val builder = AlertDialog.Builder(binding.root.context).apply {
            setTitle("提示")
            setMessage(msg)
            setPositiveButton("确定") { _, _ ->
                startActivity(Intent(binding.root.context, LoginActivity::class.java))
            }
            setNegativeButton("取消", null)
        }
        builder.create().show()
    }
}
