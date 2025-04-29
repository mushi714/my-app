package com.example.studymvpexampleapplication.base

import android.app.Application
import com.yechaoa.yutilskt.ActivityUtil
import com.yechaoa.yutilskt.YUtils

/**
 * BaseApplication
 * 通过在 AndroidManifest 声明
 * 在应用程序创建时第一个被实例化的
 * 其 onCreate() 在整个应用的生命周期仅执行一次
 * 一般在这个继承 Application 的类中, 我们会进行一些通用工具类、模块的初始化操作
 * */
class BaseApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        // 初始化YUtils工具类
        YUtils.init(this)
        // 注册 Activity 声明周期回调
        registerActivityLifecycleCallbacks(ActivityUtil.activityLifecycleCallbacks)
    }
}