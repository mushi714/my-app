package com.example.studymvpexampleapplication.presentation.register

import com.example.studymvpexampleapplication.base.mvp.BasePresenter
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.User
import kotlinx.coroutines.launch

class RegisterPresenter(view: RegisterContract.View) :
    BasePresenter<RegisterContract.View, RegisterContract.Model>(view), RegisterContract.Presenter {

    override fun createModel(): RegisterContract.Model = RegisterModel()

    override fun register(username: String, password: String, passwordAgain: String) {
        coroutineScope.launch {
            mModel?.register(
                username,
                password,
                passwordAgain,
                object : RetrofitResponseListener<User> {

                    override fun onSuccess(response: User) {
                        mView?.get()?.registerSuccess(response)
                    }

                    override fun onError(errorCode: Int, errorMessage: String) {
                        mView?.get()?.registerError(errorMessage)
                    }

                })
        }
    }

}