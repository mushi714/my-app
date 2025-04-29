package com.example.studymvpexampleapplication.data.http.interceptor

import com.example.studymvpexampleapplication.common.MyConfig
import com.yechaoa.yutilskt.SpUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 *用于从服务器响应中接收并持久化 Cookie 的拦截器
 * */
class ReceivedCookiesInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // 先执行请求，获取原始响应
        val originalResponse: Response = chain.proceed(chain.request())

        // 判断响应头中是否包含 Set-Cookie
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            // 创建一个 HashSet 用于去重存储所有 Cookie 字符串
            val cookies: HashSet<String> = HashSet()

            // 遍历所有 Set-Cookie 头，将其添加到 HashSet 中
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }

            // 将去重后的 Cookie 集合保存到本地（如 SharedPreferences）
            SpUtil.setStringSet(MyConfig.COOKIE, cookies)
        }

        // 返回原始响应，保证拦截器链的后续执行和响应传递
        return originalResponse
    }
}
