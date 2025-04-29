package com.example.studymvpexampleapplication.presentation.tree.child

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.base.BaseActivity
import com.example.studymvpexampleapplication.data.bean.Children
import com.example.studymvpexampleapplication.databinding.ActivityTreeChildBinding
import com.example.studymvpexampleapplication.presentation.main.CommonViewPageAdapter

// TreeChildActivity：用于展示子分类详情的 Activity，继承自支持 ViewBinding 的 BaseActivity
class TreeChildActivity :
    BaseActivity<ActivityTreeChildBinding>({ ActivityTreeChildBinding.inflate(it) }) {

    companion object {
        // Intent 传递的标题 Key
        var TITLE: String = "title"
        // Intent 传递的子分类列表 Key
        var CID: String = "cid"
        // Intent 传递的初始选中位置 Key
        var POSITION: String = "position"
    }

    // 初始化视图组件，设置 Toolbar 标题与返回按钮
    override fun initView() {
        // 从 Intent 中获取标题并设置到 Toolbar
        setBarTitle(intent.getStringExtra(TITLE)!!)
        // 启用左上角返回按钮
        setBackEnabled()
    }

    // 初始化数据和子页面布局，动态创建 Fragment 并绑定到 ViewPager
    override fun initData() {
        // 从 Intent 中获取传递的子分类列表，并转换为 ArrayList<Children>
        val childList: ArrayList<Children> = intent.getSerializableExtra(CID) as ArrayList<Children>
        // 提取各子分类的名称，用于 Tab 标题
        val titles = java.util.ArrayList<String>()
        for (i in childList.indices) {
            titles.add(childList[i].name)
        }

        // 创建通用的 ViewPager 适配器，传入 FragmentManager 与标题列表
        val commonViewPagerAdapter = CommonViewPageAdapter(supportFragmentManager, titles)
        // 根据每个子分类的 ID 动态添加对应的 Fragment
        for (index in titles.indices) {
            commonViewPagerAdapter.addFragment(TreeChildFragment.newInstance(childList[index].id))
        }
        // 为 ViewPager 设置适配器，完成 Fragment 与标题的绑定
        binding.viewPager.adapter = commonViewPagerAdapter

        // 从 Intent 获取初始显示的位置，并设置到 ViewPager
        val index = intent.getIntExtra(POSITION, 0)
        binding.viewPager.currentItem = index

        // 将 TabLayout 与 ViewPager 联动，自动显示各页签标题并支持点击切换
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    // 统一点击事件处理（此 Activity 中暂无额外点击事件）
    override fun allClick() {
    }
}
