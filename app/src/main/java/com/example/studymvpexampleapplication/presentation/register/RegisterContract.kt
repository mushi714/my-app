package com.example.studymvpexampleapplication.presentation.register

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.User


interface RegisterContract : BaseContract {

    interface Model : BaseContract.IBaseModel {
        suspend fun register(
            username: String,
            password: String,
            passwordAgain: String,
            listener: RetrofitResponseListener<User>
        )
    }

    interface View : BaseContract.IBaseView {
        fun registerSuccess(baseUserBean: User)
        fun registerError(errorMessage: String)
    }

    interface Presenter : BaseContract.IBasePresenter {
        fun register(username: String, password: String, passwordAgain: String)
    }

}