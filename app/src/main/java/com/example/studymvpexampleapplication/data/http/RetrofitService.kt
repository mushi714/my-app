package com.example.studymvpexampleapplication.data.http

import com.example.studymvpexampleapplication.data.http.interceptor.AddCookiesInterceptor
import com.example.studymvpexampleapplication.data.http.interceptor.ReceivedCookiesInterceptor
import com.example.studymvpexampleapplication.util.MLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitService 单例对象，用于初始化并提供全局唯一的 Retrofit API 服务实例
 */
object RetrofitService {

    // 持有 WAZApi 接口的实现，用于调用后端接口
    private var apiServer: API.WAZApi

    /**
     * 获取 API 服务实例
     *
     * @return WAZApi 的单例实现，可用于所有网络请求
     */
    fun getApiService(): API.WAZApi {
        return apiServer
    }

    // 在对象加载时（第一次访问时）执行一次，完成 Retrofit 和 OkHttpClient 的初始化
    init {
        // ==================== OkHttp 拦截器配置 ====================

        // 1. 日志拦截器：记录 HTTP 请求和响应的详细信息（包括请求头/响应体）
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            // 设置日志级别为 BODY，可打印请求和响应的全部内容
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 2. 自定义拦截器：打印每次请求的 URL，便于调试和埋点
        val requestLoggingInterceptor = Interceptor { chain ->
            val request = chain.request()
            MLog.d("RequestUrl = ${request.url}")
            chain.proceed(request)
        }

        // ==================== OkHttpClient 构建 ====================
        val okHttpClient = OkHttpClient.Builder()
            // 添加日志拦截器
            .addInterceptor(httpLoggingInterceptor)
            /**
             * 这两个拦截器结合起来，实现了在网络请求中处理 Cookie 的逻辑。
             * AddCookiesInterceptor 用于在请求中添加 Cookie，
             * 而 ReceivedCookiesInterceptor 用于从响应中提取新的 Cookie 并存储。
             * 这样可以实现在应用中对 Cookie 的管理与传递。
             * */
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            // 添加自定义请求日志拦截器
            .addInterceptor(requestLoggingInterceptor)
            // 设置连接超时时间：15 秒，防止网络请求长时间挂起
            .connectTimeout(15, TimeUnit.SECONDS)
            // 设置读取超时时间：15 秒，防止服务器响应过慢
            .readTimeout(15, TimeUnit.SECONDS)
            // 设置写入超时时间：15 秒，防止上传数据时卡住
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        // ==================== Retrofit 构建 ====================
        val retrofit = Retrofit.Builder()
            // 关联自定义的 OkHttpClient，实现拦截和超时配置
            .client(okHttpClient)
            // 添加 Gson 转换器工厂，将 JSON 数据自动序列化/反序列化为 Kotlin 对象
            .addConverterFactory(GsonConverterFactory.create())
            // 设置基础 URL，所有接口请求路径都会拼接到此地址后面
            .baseUrl(API.BASE_URL)
            .build()

        // 创建 WAZApi 接口的实现，并赋值给单例属性
        apiServer = retrofit.create(API.WAZApi::class.java)
    }
}
