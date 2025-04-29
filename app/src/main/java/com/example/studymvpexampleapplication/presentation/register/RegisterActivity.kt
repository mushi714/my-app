package com.example.studymvpexampleapplication.presentation.register

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.base.mvp.BaseMVPActivity
import com.example.studymvpexampleapplication.data.bean.User
import com.example.studymvpexampleapplication.databinding.ActivityRegisterBinding
import com.example.studymvpexampleapplication.util.ObjectUtil
import com.yechaoa.yutilskt.YUtils
import com.yechaoa.yutilskt.show
import kotlin.toString

class RegisterActivity : BaseMVPActivity<ActivityRegisterBinding, RegisterContract.Presenter>({
    ActivityRegisterBinding.inflate(it)
}), RegisterContract.View {

    override fun createPresenter(): RegisterContract.Presenter {
        return RegisterPresenter(this)
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun initView() {
        setBackEnabled()
    }

    override fun initData() {
        setBarTitle("注册")
    }

    override fun allClick() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordAgain = binding.etPasswordAgain.text.toString()
            // 注册信息筛选
            if (ObjectUtil.isEmpty(username)) {
                show("请输入注册账号")
            } else if (ObjectUtil.isEmpty(password)) {
                show("请输入密码")
            } else if (ObjectUtil.isEmpty(passwordAgain)) {
                show("请再次输入密码")
            } else if (password != passwordAgain) {
                show("请确保两次密码输入一致")
            } else {
                YUtils.showLoading(this, "注册中")
                mPresenter.register(username, password, passwordAgain)
            }
        }
    }

    override fun registerSuccess(baseUserBean: User) {
        YUtils.hideLoading()
        show("注册成功,请登录")
        finish() // 结束当前注册界面, 退出到登录界面
    }

    override fun registerError(errorMessage: String) {
        YUtils.hideLoading()
        show("$errorMessage") // 提示注册失败信息
    }

}