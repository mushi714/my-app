package com.example.studymvpexampleapplication.presentation.navi

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.studymvpexampleapplication.adapter.NaviLeftAdapter
import com.example.studymvpexampleapplication.adapter.NaviRightAdapter
import com.example.studymvpexampleapplication.base.mvp.BaseMVPFragment
import com.example.studymvpexampleapplication.data.bean.ArticleX
import com.example.studymvpexampleapplication.data.bean.Navi
import com.example.studymvpexampleapplication.databinding.FragmentNaviBinding
import com.example.studymvpexampleapplication.presentation.detail.DetailActivity
import com.yechaoa.yutilskt.ToastUtil
import com.yechaoa.yutilskt.show
/**
 * 继承自带 MVP 支持的 BaseMVPFragment，绑定 FragmentNaviBinding 并指定 Presenter 类型
 * ，负责注入 View 与 Presenter 的生命周期管理，符合 MVP 架构最佳实践
 * */
class NaviFragment :
    BaseMVPFragment<FragmentNaviBinding, NaviContract.Presenter>({ FragmentNaviBinding.inflate(it) }),
    NaviContract.View {

    // 导航分类列表数据源，懒加载后赋值
    private lateinit var mNaviList: MutableList<Navi>
    // 当前右侧文章列表数据源，根据选中分类动态赋值
    private lateinit var mArticles: MutableList<ArticleX>

    override fun initView() {
        // 可在此初始化视图组件，如 RecyclerView 分割线、下拉刷新等
    }

    override fun initData() {
        // 在数据初始化阶段请求导航数据，由 Presenter 回调通知 View
        mPresenter.getNavi()
    }

    override fun allClick() {
        // 可在此注册视图点击事件监听，如头部按钮、下拉刷新等
    }

    override fun createPresenter(): NaviContract.Presenter {
        // 将当前 Fragment 作为 View 注入到 Presenter 中，完成 V-P 绑定
        return NaviPresenter(this)
    }

    override fun getNaviSuccess(naviList: MutableList<Navi>) {
        // Presenter 回调获取到导航数据后的处理
        mNaviList = naviList

        // 左侧导航列表 Adapter，点击时更新右侧文章列表
        val naviLeftAdapter = NaviLeftAdapter { pos ->
            mArticles = mNaviList[pos].articles
            setRightLayout(mArticles)
        }

        // 使用 LinearLayoutManager 实现垂直列表布局
        binding.leftNavigation.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.leftNavigation.adapter = naviLeftAdapter

        // 将数据提交给 Adapter，触发列表刷新
        naviLeftAdapter.setList(mNaviList)

        // 默认选中第一个分类，展示其相关文章
        mArticles = mNaviList[0].articles
        setRightLayout(mArticles)
    }

    /**
     * 填充右侧数据
     */
    private fun setRightLayout(articles: MutableList<ArticleX>) {
        // 右侧文章列表 Adapter，点击时通过 Intent 跳转到详情页面
        val naviRightAdapter = NaviRightAdapter { pos ->
            val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.WEB_URL, mArticles[pos].link)
                putExtra(DetailActivity.WEB_TITLE, mArticles[pos].title)
            }
            startActivity(intent)
        }

        // 使用 StaggeredGridLayoutManager 实现两列瀑布流布局
        binding.rightNavi.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rightNavi.adapter = naviRightAdapter

        // 将文章列表数据提交给 Adapter，触发布局显示
        naviRightAdapter.setList(articles)
    }

    override fun getNaviError(errorMessage: String) {
        // 网络或业务异常时提示错误信息
        show(errorMessage)
    }

}
