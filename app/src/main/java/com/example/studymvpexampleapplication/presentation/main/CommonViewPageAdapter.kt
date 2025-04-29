package com.example.studymvpexampleapplication.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CommonViewPageAdapter : FragmentPagerAdapter {

    // 可选的页面标题列表，用于 getPageTitle() 返回
    private var mTitles: List<String>? = null

    // 存放实际要展示的 Fragment 列表
    private var mFragments: MutableList<Fragment> = ArrayList()

    /**
     * 构造函数：仅传入 FragmentManager
     * @param fm 管理 Fragment 的 FragmentManager，必须非空
     */
    constructor(fm: FragmentManager) : super(fm)

    /**
     * 构造函数：传入 FragmentManager 和页面标题列表
     * @param fm     管理 Fragment 的 FragmentManager
     * @param titles 每个页面对应的标题列表
     */
    constructor(fm: FragmentManager?, titles: List<String>?) : super(fm!!) {
        mTitles = titles
    }

    /**
     * 向适配器中动态添加一个 Fragment 页面
     * @param fragment 要添加的 Fragment 实例
     */
    fun addFragment(fragment: Fragment) {
        mFragments.add(fragment)
    }

    /**
     * 返回总页数，即 Fragment 列表的大小
     */
    override fun getCount(): Int {
        return mFragments.size
    }

    /**
     * 根据位置返回对应的 Fragment
     * @param position 页面索引，从 0 开始
     */
    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    /**
     * 返回每个页面的标题，用于 TabLayout 等组件显示
     * @param position 页面索引
     * @return 对应位置的标题字符串
     */
    override fun getPageTitle(position: Int): CharSequence? {
        // mTitles 非空并包含足够元素时返回对应标题
        return mTitles!![position]
    }
}
