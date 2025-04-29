package com.example.studymvpexampleapplication.presentation.register

import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.User
import com.example.studymvpexampleapplication.data.http.RetrofitService
import com.example.studymvpexampleapplication.util.launchCoroutine


class RegisterModel : RegisterContract.Model {
    override suspend fun register(
        username: String,
        password: String,
        passwordAgain: String,
        listener: RetrofitResponseListener<User>
    ) = launchCoroutine({
        val userBaseBean = RetrofitService.getApiService()
            .register(username, password, passwordAgain)
        if (userBaseBean.errorCode != 0) {
            listener.onError(userBaseBean.errorCode, userBaseBean.errorMsg)
        } else {
            listener.onSuccess(userBaseBean.data)
        }
    }, onError = { e: Throwable ->
        e.printStackTrace()
    })

}