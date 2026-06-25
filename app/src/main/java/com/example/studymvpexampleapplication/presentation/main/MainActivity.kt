package com.example.studymvpexampleapplication.presentation.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.base.BaseActivity
import com.example.studymvpexampleapplication.databinding.ActivityMainBinding
import com.example.studymvpexampleapplication.presentation.home.HomeFragment
import com.example.studymvpexampleapplication.presentation.navi.NaviFragment
import com.example.studymvpexampleapplication.presentation.project.ProjectFragment
import com.example.studymvpexampleapplication.presentation.tree.TreeFragment
import androidx.viewpager.widget.ViewPager
import com.example.studymvpexampleapplication.common.MyConfig
import com.example.studymvpexampleapplication.presentation.about.AboutActivity
import com.example.studymvpexampleapplication.presentation.collect.CollectActivity
import com.example.studymvpexampleapplication.presentation.login.LoginActivity
import com.yechaoa.yutilskt.ActivityUtil
import com.yechaoa.yutilskt.SpUtil
import com.yechaoa.yutilskt.show

class MainActivity :
// 继承项目通用 BaseActivity，绑定 ActivityMainBinding 布局
    BaseActivity<ActivityMainBinding>({ ActivityMainBinding.inflate(it) }) {

    // 视图初始化，在这里设置 Toolbar、侧边栏头部、抽屉与 Fragment
    override fun initView() {
        // 设置 Toolbar 标题
        binding.includeMain.toolbar.title = getString(R.string.app_name)
        // 从 SharedPreferences 获取用户名并显示在 NavigationView 的头部
        binding.navView
            .getHeaderView(0)
            .findViewById<TextView>(R.id.tv_username)
            .text = SpUtil.getString(MyConfig.USER_NAME, "username")
        // 将布局中的 Toolbar 绑定为 Activity 的 ActionBar
        setSupportActionBar(binding.includeMain.toolbar)
        // 初始化 DrawerLayout 与 Toolbar 的联动
        initActionBarDrawer()
        // 初始化底部 ViewPager 与其 Fragment 列表
        initFragments()
    }

    // 数据初始化，此处暂无业务逻辑
    override fun initData() {
    }

    // 统一注册点击与滑动事件
    override fun allClick() {
        /*** 侧边栏菜单点击事件 ***/
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_collect -> {
                    // 跳转到收藏页
                    startActivity(Intent(this, CollectActivity::class.java))
                }
                R.id.nav_share -> {
                    // 调用系统分享
                    shareProject()
                }
                R.id.nav_about -> {
                    // 跳转到关于页
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                R.id.nav_logout -> {
                    // 退出登录弹窗提示
                    AlertDialog.Builder(this@MainActivity).apply {
                        setTitle("提示")
                        setMessage("确定退出登录吗?")
                        setPositiveButton("确定") { _, _ ->
                            // 清除登录状态与用户信息，跳转到登录页并关闭当前
                            SpUtil.setBoolean(MyConfig.IS_LOGIN, false)
                            SpUtil.removeByKey(MyConfig.COOKIE)
                            SpUtil.removeByKey(MyConfig.USER_NAME)
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        setNegativeButton("取消", null)
                    }.create().show()
                }
            }
            // 关闭侧边栏
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        /*** ViewPager 滑动监听：同步底部导航栏与 Toolbar 标题 ***/
        binding.includeMain.includeContentMain.viewPager
            .addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    // 根据当前页位置，更新 BottomNavigationView 的选中状态
                    binding.includeMain.includeContentMain.bottomNavigation
                        .menu.getItem(position).isChecked = true
                    // 更新 Toolbar 标题（setChecked 不会触发点击事件）
                    binding.includeMain.toolbar.title = when (position) {
                        0 -> getString(R.string.app_name)
                        1 -> getString(R.string.title_tree)
                        2 -> getString(R.string.title_navi)
                        else -> getString(R.string.title_project)
                    }
                }

                override fun onPageSelected(position: Int) {
                    // 页面完全切换时的回调，可用于统计或动画
                }

                override fun onPageScrollStateChanged(state: Int) {
                    // 滚动状态改变时的回调
                }
            })

        /*** BottomNavigationView 点击事件：切换 ViewPager ***/
        binding.includeMain.includeContentMain.bottomNavigation
            .setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_home -> {
                        binding.includeMain.includeContentMain.viewPager.currentItem = 0
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.navigation_tree -> {
                        binding.includeMain.includeContentMain.viewPager.currentItem = 1
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.navigation_navi -> {
                        binding.includeMain.includeContentMain.viewPager.currentItem = 2
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.navigation_project -> {
                        binding.includeMain.includeContentMain.viewPager.currentItem = 3
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                false
            }
    }

    /**
     * 调用系统分享功能，分享项目 GitHub 地址
     */
    private fun shareProject() {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "玩安卓")
            putExtra(Intent.EXTRA_TEXT, "https://blog.csdn.net/m0_56334538/article/details/147562633?spm=1011.2124.3001.6209")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }.also {
            startActivity(Intent.createChooser(it, "玩安卓"))
        }
    }

    /**
     * 配置 DrawerLayout 与 Toolbar 的汉堡按钮联动
     */
    private fun initActionBarDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.includeMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        // 添加开关监听并同步状态
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private var mExitTime: Long = 0 // 记录上次点击返回键的时间戳

    /**
     * 拦截返回键：侧边栏打开时先关闭，未打开时双击退出
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // 如果侧边栏处于打开状态，则先关闭它
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // 判断两次返回间隔，小于2秒则退出应用
            if (System.currentTimeMillis() - mExitTime > 2000) {
                show("再按一次退出")
                mExitTime = System.currentTimeMillis()
            } else {
                // 关闭所有 Activity
                ActivityUtil.closeAllActivity()
            }
        }
    }

    /**
     * 初始化 ViewPager 并添加四个主页面 Fragment
     */
    private fun initFragments() {
        val viewPagerAdapter = CommonViewPageAdapter(supportFragmentManager).apply {
            addFragment(HomeFragment())     // 首页
            addFragment(TreeFragment())     // 体系
            addFragment(NaviFragment())     // 导航
            addFragment(ProjectFragment())  // 项目
        }
        // 设置预加载页面数量，左右各保留最多3个 Fragment
        binding.includeMain.includeContentMain.viewPager.offscreenPageLimit = 3
        // 绑定适配器到 ViewPager，实现滑动切换
        binding.includeMain.includeContentMain.viewPager.adapter = viewPagerAdapter
    }
}
