package com.example.studymvpexampleapplication.presentation.home

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.example.studymvpexampleapplication.adapter.ArticleAdapter
import com.example.studymvpexampleapplication.base.mvp.BaseMVPFragment
import com.example.studymvpexampleapplication.data.bean.Article
import com.example.studymvpexampleapplication.data.bean.ArticleDetail
import com.example.studymvpexampleapplication.data.bean.Banner

import com.example.studymvpexampleapplication.databinding.FragmentHomeBinding
import com.example.studymvpexampleapplication.presentation.detail.DetailActivity
import com.example.studymvpexampleapplication.presentation.login.LoginActivity
import com.example.studymvpexampleapplication.util.GlideImageLoader
import com.yechaoa.yutilskt.DisplayUtil
import com.yechaoa.yutilskt.show
import kotlin.math.roundToInt
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerListener

/**
 * HomeFragment：基于 MVP 模式的首页 Fragment
 * - 继承自 BaseMVPFragment，实现 HomeContract.View 及各类回调接口
 * - 负责展示轮播图、文章列表、加载更多、收藏逻辑等
 */
class HomeFragment :
    BaseMVPFragment<FragmentHomeBinding, HomeContract.Presenter>({ FragmentHomeBinding.inflate(it) }),
    HomeContract.View,                        // 与 Presenter 交互的 View 接口
    OnBannerListener,                         // Banner 点击回调
    OnLoadMoreListener,                       // 上拉加载更多回调
    OnItemClickListener,                      // 列表项点击回调
    OnItemChildClickListener {                // 列表子项点击回调

    companion object {
        private const val TOTAL_COUNTER = 20 // 每次请求的最大条目数
        private var CURRENT_SIZE = 0         // 当前已加载条目数
        private var CURRENT_PAGE = 0         // 当前加载的页码
    }

    // 轮播图数据列表
    private lateinit var bannerList: List<Banner>
    // 文章数据列表
    private lateinit var mDataList: MutableList<ArticleDetail>
    // 列表适配器
    private lateinit var mArticleAdapter: ArticleAdapter
    // 当前操作的文章位置（用于收藏/取消收藏回调）
    private var mPosition: Int = 0

    override fun initView() {
        // 此处可初始化视图组件，已在布局中通过 ViewBinding 关联
    }

    override fun initData() {
        // 请求轮播图和第一页文章列表
        mPresenter.getBanner()
        mPresenter.getArticleList(CURRENT_PAGE)
    }

    override fun allClick() {
        // 此处可统一注册额外点击事件
    }

    override fun createPresenter(): HomeContract.Presenter {
        // 创建并返回 Presenter 实例
        return HomePresenter(this)
    }

    // ==================== Banner 回调 ====================

    override fun getBannerSuccess(bannerList: MutableList<Banner>) {
        this.bannerList = bannerList

        // 提取图片地址和标题
        val images: MutableList<String> = ArrayList()
        val titles: MutableList<String> = ArrayList()
        for (index in bannerList.indices) {
            images.add(bannerList[index].imagePath)
            titles.add(bannerList[index].title)
        }

        // 根据屏幕高度动态设置 Banner 高度
        val layoutParams = binding.banner.layoutParams
        layoutParams.height = (DisplayUtil.getScreenHeight() / 1.8).roundToInt()

        // 配置并启动 Banner
        binding.banner.setImages(images)
            .setBannerTitles(titles)
            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
            .setImageLoader(GlideImageLoader())
            .start()

        // 监听 Banner 点击事件
        binding.banner.setOnBannerListener(this)
    }

    override fun getBannerError(errorMessage: String) {
        // 轮播图加载失败，弹出提示
        show(errorMessage)
    }

    // ==================== 文章列表回调 ====================

    override fun getArticleListSuccess(article: Article) {
        // 保存当前加载条数和数据
        CURRENT_SIZE = article.size
        mDataList = article.datas

        // 初始化并配置适配器
        mArticleAdapter = ArticleAdapter().apply {
            animationEnable = true                     // 启用加载动画
            setOnItemClickListener(this@HomeFragment)  // 列表项点击
            setOnItemChildClickListener(this@HomeFragment) // 子项点击（收藏图标）
            loadMoreModule.setOnLoadMoreListener(this@HomeFragment) // 加载更多
        }

        // 绑定适配器并填充数据
        binding.recyclerView.adapter = mArticleAdapter
        mArticleAdapter.setList(mDataList)
    }

    override fun getArticleListError(errorMessage: String) {
        // 文章列表加载失败，弹出提示
        show(errorMessage)
    }

    override fun getMoreArticleListSuccess(article: Article) {
        // 分页加载成功，追加数据并通知适配器
        mDataList.addAll(article.datas)
        CURRENT_SIZE = article.datas.size
        mArticleAdapter.addData(article.datas)
        mArticleAdapter.loadMoreModule.loadMoreComplete()
    }

    override fun getMoreArticleListError(errorMessage: String) {
        // 分页加载失败，弹出提示
        show(errorMessage)
    }

    // ==================== 收藏/取消收藏回调 ====================

    override fun collectSuccess(successMessage: String) {
        // 收藏成功，更新数据并刷新列表
        show(successMessage)
        mDataList[mPosition].collect = true
        mArticleAdapter.notifyDataSetChanged()
    }

    override fun collectError(errorMessage: String) {
        // 收藏失败，弹出提示
        show(errorMessage)
    }

    override fun unCollectSuccess(successMessage: String) {
        // 取消收藏成功，更新数据并刷新列表
        show(successMessage)
        mDataList[mPosition].collect = false
        mArticleAdapter.notifyDataSetChanged()
    }

    override fun unCollectError(errorMessage: String) {
        // 取消收藏失败，弹出提示
        show(errorMessage)
    }

    // ==================== 登录提示回调 ====================

    override fun login(msg: String) {
        // 弹出对话框提示需登录，并跳转到 LoginActivity
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

    // ==================== 交互事件 ====================

    override fun onItemChildClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        position: Int
    ) {
        // 子项点击（收藏图标）
        mPosition = position
        if (mDataList[position].collect) {
            mPresenter.unCollect(mDataList[position].id)
        } else {
            mPresenter.collect(mDataList[position].id)
        }
    }

    override fun onLoadMore() {
        // 加载更多时延迟 1s 再执行，模拟加载
        binding.recyclerView.postDelayed({
            if (CURRENT_SIZE < TOTAL_COUNTER) {
                // 数据已加载完毕
                mArticleAdapter.loadMoreModule.loadMoreEnd(true)
            } else {
                // 加载下一页
                CURRENT_PAGE++
                mPresenter.getMoreArticleList(CURRENT_PAGE)
            }
        }, 1000)
    }

    override fun onItemClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        position: Int
    ) {
        // 列表项点击，跳转到详情页
        val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
            putExtra(DetailActivity.WEB_URL, mDataList[position].link)
            putExtra(DetailActivity.WEB_TITLE, mDataList[position].title)
        }
        startActivity(intent)
    }

    override fun OnBannerClick(position: Int) {
        // Banner 点击，跳转到对应链接详情页
        val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
            putExtra(DetailActivity.WEB_URL, bannerList[position].url)
            putExtra(DetailActivity.WEB_TITLE, bannerList[position].title)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment 销毁视图时，停止 Banner 自动轮播，避免资源泄漏
        binding.banner.stopAutoPlay()
    }

}
