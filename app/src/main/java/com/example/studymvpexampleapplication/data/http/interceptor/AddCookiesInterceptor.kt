package com.example.studymvpexampleapplication.data.http.interceptor

import com.example.studymvpexampleapplication.common.MyConfig
import com.yechaoa.yutilskt.SpUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * 用于在每次 HTTP 请求中添加存储在本地的 Cookie 的拦截器
 * */
class AddCookiesInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // 从原始请求构造一个新的 Request.Builder，以便添加头部
        val builder: Request.Builder = chain.request().newBuilder()

        // 从 SharedPreferences 中读取所有已保存的 Cookie 字符串集合
        val stringSet = SpUtil.getStringSet(MyConfig.COOKIE)

        // 将每个 Cookie 都以 “Cookie” 头的形式添加到请求中
        if (stringSet != null && stringSet.isNotEmpty()) {
            for (cookie in stringSet) {
                builder.addHeader("Cookie", cookie)
            }
        }

        // 构建新的请求并交给下一个拦截器或网络执行
        return chain.proceed(builder.build())
    }
}
