package com.example.studymvpexampleapplication.base.mvp

import android.app.Activity
import androidx.lifecycle.LifecycleObserver

/**
 * MVP 架构的基础契约接口，定义了 View、Presenter、Model 三层的公共方法和规范。
 */
interface BaseContract {

    /**
     * View 层接口，需要由具体的 Activity/Fragment 实现。
     */
    interface IBaseView {
        /**
         * 获取当前绑定的 Activity 实例，用于 UI 操作和上下文需求。
         * @return 当前 View 所在的 Activity，如果已经销毁则返回 null。
         */
        fun getActivity(): Activity?
    }

    /**
     * Presenter 层接口，负责业务逻辑处理并协调 View 与 Model。
     * 继承 LifecycleObserver 以便监听宿主生命周期。
     */
    interface IBasePresenter : LifecycleObserver {
        /**
         * 判断 Presenter 是否已与 View 建立关联。
         * 可在调用业务方法前进行检查，避免空指针或内存泄漏。
         *
         * @return true 表示已经 attach，false 表示尚未 attach 或已 detach
         */
        fun isViewAttach(): Boolean

        /**
         * 在 View 销毁时调用，断开 Presenter 与 View 的引用关联，
         * 以便垃圾回收器回收，防止内存泄漏。
         */
        fun detachView()
    }

    /**
     * Model 层接口，负责数据获取与处理，可由具体实现类定义网络请求、数据库操作等。
     */
    interface IBaseModel {
        // 可在此定义全局通用的数据处理方法，例如统一的错误封装或返回数据类型
    }
}