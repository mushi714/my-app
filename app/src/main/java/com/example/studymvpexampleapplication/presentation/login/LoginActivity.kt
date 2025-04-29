package com.example.studymvpexampleapplication.presentation.login

import android.app.Activity
import android.content.Intent
import com.example.studymvpexampleapplication.base.mvp.BaseMVPActivity
import com.example.studymvpexampleapplication.common.MyConfig
import com.example.studymvpexampleapplication.data.bean.User
import com.example.studymvpexampleapplication.databinding.ActivityLoginBinding
import com.example.studymvpexampleapplication.presentation.main.MainActivity
import com.example.studymvpexampleapplication.presentation.register.RegisterActivity
import com.example.studymvpexampleapplication.util.ObjectUtil
import com.yechaoa.yutilskt.SpUtil
import com.yechaoa.yutilskt.YUtils
import com.yechaoa.yutilskt.show
/**
 * LoginActivity 基于 MVP 模式的 Activity，实现了 LoginContract.View，
 * 并通过 BaseMVPActivity 提供的 ViewBinding 与 Presenter 统一管理逻辑。
 */
class LoginActivity :
    BaseMVPActivity<ActivityLoginBinding, LoginContract.Presenter>({ ActivityLoginBinding.inflate(it) }),
    LoginContract.View {
    /**
     * 创建并返回当前 Activity 对应的 Presenter 实例
     */
    override fun createPresenter(): LoginContract.Presenter = LoginPresenter(this)
    /**
     * 初始化视图组件，可在此处完成 Toolbar、状态栏等 UI 设置
     */
    override fun initView() {
    }
    /**
     * 初始化数据，如从本地或网络预加载必要内容
     */
    override fun initData() {
        setBarTitle("登录")
    }
    /**
     * 注册所有点击事件
     */
    override fun allClick() {
        // 登录按钮点击事件
        binding.btnLogin.setOnClickListener {
            // 获取用户输入
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            // 非空校验
            if (ObjectUtil.isEmpty(username)) {
                show("请输入用户名")
            } else if (ObjectUtil.isEmpty(password)) {
                show("请输入密码")
            } else {
                // 显示加载框
                YUtils.showLoading(this@LoginActivity, "登录中")
                // 调用 Presenter 发起登录
                mPresenter.login(username, password)
            }
        }
        // 注册跳转到注册页面
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
    /**
     * 登录成功回调
     *
     * @param baseUserBean 登录成功后返回的用户数据
     */
    override fun loginSuccess(baseUserBean: User) {
        // 隐藏加载框
        YUtils.hideLoading()
        // 提示登录成功
        show("登录成功 ${baseUserBean.username}")
        // 保存登录状态与用户名到 SharedPreferences
        SpUtil.setString(MyConfig.USER_NAME,baseUserBean.username)
        SpUtil.setBoolean(MyConfig.IS_LOGIN, true)
        // 跳转到主页面
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        // 结束当前页,避免返回时停留此页
        finish()
    }
    /**
     * 登录失败回调
     *
     * @param errorMessage 登录失败的错误信息
     */
    override fun loginError(errorMessage: String) {
        // 隐藏加载框
        YUtils.hideLoading()
        // 提示登录失败
        show("登录失败 $errorMessage")
    }
    /**
     * IBaseView 要求实现的方法，返回当前 Activity 用于通用操作（如显示 Toast）
     */
    override fun getActivity(): Activity = this

}