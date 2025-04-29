package com.example.studymvpexampleapplication.presentation.project

import com.example.studymvpexampleapplication.base.mvp.BaseMVPFragment
import com.example.studymvpexampleapplication.data.bean.Project
import com.example.studymvpexampleapplication.databinding.FragmentProjectBinding
import com.example.studymvpexampleapplication.presentation.main.CommonViewPageAdapter
import com.example.studymvpexampleapplication.presentation.project.child.ProjectChildFragment
import com.yechaoa.yutilskt.show


class ProjectFragment : BaseMVPFragment<FragmentProjectBinding, ProjectContract.Presenter>({
    FragmentProjectBinding.inflate(it)
}), ProjectContract.View {
    /**
     * 初始化视图：将 TabLayout 与 ViewPager 绑定，实现滑动时 Tab 自动联动、
     * 点击 Tab 时 ViewPager 自动切换页面。 :contentReference[oaicite:0]{index=0}
     */
    override fun initView() {
        binding.projectTabLayout.setupWithViewPager(binding.projectViewPager)
    }

    /**
     * 初始化数据：通过 Presenter 发起获取项目分类列表的请求 :contentReference[oaicite:1]{index=1}
     */
    override fun initData() {
        mPresenter.getProject()
    }

    /**
     * 注册所有点击事件，这里暂不需要额外处理
     */
    override fun allClick() {
    }

    /**
     * 创建并返回 Presenter 实例，将当前 Fragment 注入给 Presenter
     */
    override fun createPresenter(): ProjectContract.Presenter {
        return ProjectPresenter(this)
    }

    /**
     * Presenter 回调：项目分类数据请求成功
     */
    override fun getProjectSuccess(projectList: MutableList<Project>) {
        // 提取所有分类标题，用于 ViewPager 的页面标签
        val titles: MutableList<String> = ArrayList()
        for (project in projectList) {
            titles.add(project.name)
        }

        // 创建通用的 ViewPager 适配器，并传入子 FragmentManager
        // childFragmentManager 用于管理 Fragment 中的子 Fragment :contentReference[oaicite:2]{index=2}
        val commonViewPagerAdapter = CommonViewPageAdapter(childFragmentManager, titles)

        // 根据每个分类 ID 构造对应的 ProjectChildFragment 实例，添加到适配器
        for (project in projectList) {
            commonViewPagerAdapter.addFragment(
                ProjectChildFragment.newInstance(project.id)
            )
        }

        // 将适配器绑定到 ViewPager 上，并默认选中第一个页面
        binding.projectViewPager.adapter = commonViewPagerAdapter
        binding.projectViewPager.currentItem = 0
    }

    /**
     * Presenter 回调：项目分类数据请求失败，显示错误提示
     */
    override fun getProjectError(errorMessage: String) {
        show(errorMessage)
    }

}
