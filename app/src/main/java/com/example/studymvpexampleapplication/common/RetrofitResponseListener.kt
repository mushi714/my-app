package com.example.studymvpexampleapplication.common

/**
 * RetrofitResponseListener 接口定义了一个通用的网络请求回调契约，
 * 使用泛型 T 来表示成功时返回的数据类型。实现该接口后，
 * 调用方可以在 onSuccess/onError 中编写具体的业务逻辑。
 *
 * 该接口属于典型的“监听者（Listener）”模式，用于异步事件通知，
 * 与 Android 中的 View 点击监听、Retrofit 1.x 中的 Callback<T> 类似。
 *
 * @param T : Any  表示回调成功时返回的数据类型，使用 Kotlin 的泛型来保证类型安全。
 */
interface RetrofitResponseListener<T : Any> {

    /**
     * 当网络请求成功且响应体被正确解析为 T 类型时回调此方法。
     *
     * @param response 从服务器返回并解析后的数据对象，类型为 T
     */
    fun onSuccess(response: T)

    /**
     * 当网络请求失败、服务器返回错误码或解析异常时回调此方法。
     *
     * @param errorCode    HTTP 或应用级错误码（如非 2xx）
     * @param errorMessage 错误描述信息，便于提示或日志记录
     */
    fun onError(errorCode: Int, errorMessage: String)
}
