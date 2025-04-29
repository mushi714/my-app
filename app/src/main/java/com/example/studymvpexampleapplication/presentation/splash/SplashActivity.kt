package com.example.studymvpexampleapplication.presentation.splash

import android.content.Intent
import com.example.studymvpexampleapplication.base.BaseActivity
import com.example.studymvpexampleapplication.common.MyConfig
import com.example.studymvpexampleapplication.databinding.ActivitySplashBinding
import com.example.studymvpexampleapplication.presentation.login.LoginActivity
import com.example.studymvpexampleapplication.presentation.main.MainActivity
import com.yechaoa.yutilskt.SpUtil
import java.io.InterruptedIOException

/**
 * SplashActivity 继承自 BaseActivity，承载应用启动页逻辑
 * 使用 ViewBinding 自动绑定布局：ActivitySplashBinding
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>({ inflater ->
    // 通过 lambda 传入 LayoutInflater，初始化 ViewBinding
    ActivitySplashBinding.inflate(inflater)
}) {

    /**
     * 初始化视图组件
     * 在此方法中可完成状态栏、UI 控件的配置或动画启动
     */
    override fun initView() {
        // TODO: 根据需要在此处初始化视图，例如启动动画
    }

    /**
     * 初始化数据或后台逻辑
     * 可在此方法中加载配置、检查版本、从本地/网络获取数据等
     */
    override fun initData() {
        // TODO: 根据需要在此处初始化数据，例如读取缓存或接口预热
    }

    /**
     * 统一注册点击或交互事件
     * 用于延时跳转到主界面
     */
    override fun allClick() {
        // 使用匿名 Thread 对象启动一个新线程
        object : Thread() {
            override fun run() {
                try {
                    // 暂停 1 秒（1000 毫秒），模拟停留时间
                    sleep(1000)
                    // 根据登录状态进行页面跳转
                    if (SpUtil.getBoolean(MyConfig.IS_LOGIN)) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    } else {
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                    // 结束当前 Activity，移除返回栈，避免用户返回到闪屏页
                    finish()
                } catch (e: InterruptedIOException) {
                    // 捕获可能的中断异常并打印堆栈
                    e.printStackTrace()
                }
            }
        }.start() // 启动线程
    }
}