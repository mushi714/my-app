package com.example.studymvpexampleapplication.util

import android.accounts.NetworkErrorException
import android.util.MalformedJsonException
import com.google.gson.JsonSyntaxException
import com.yechaoa.yutilskt.show
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * 异常处理工具类，用于统一捕获并处理各种运行时异常，
 * 并通过日志或吐司等方式向用户展示友好提示。
 */
object ExceptionUtil {

    /**
     * 捕获并处理通用异常，根据不同异常类型进行分类提示。
     *
     * @param e 捕获到的 Throwable 异常
     */
    fun catchException(e: Throwable) {
        // 打印异常堆栈，方便调试和定位问题
        e.printStackTrace()

        when (e) {
            is HttpException -> {
                // HTTP 错误，使用状态码分类处理
                catchHttpException(e.code())
            }
            is InterruptedIOException -> {
                // I/O 操作被中断，可能是超时或主动取消
                MLog.e("服务器连接失败，请稍后重试")
            }
            is UnknownHostException, is NetworkErrorException -> {
                // 无法解析主机名或网络不可达
                MLog.e("网络连接异常，请检查您的网络设置")
            }
            is MalformedJsonException, is JsonSyntaxException -> {
                // JSON 格式不正确，服务器返回的数据有误
                MLog.e("服务器返回数据格式错误，请稍后重试")
            }
            is ConnectException -> {
                // 连接服务器失败
                MLog.e("连接服务器失败")
            }
            else -> {
                // 其他未知异常，给出通用提示
                MLog.e("操作失败，请稍后重试")
            }
        }
    }

    /**
     * 处理 HTTP 异常，根据状态码决定提示信息。
     *
     * @param errorCode HTTP 响应状态码
     */
    private fun catchHttpException(errorCode: Int) {
        // 2xx 范围内视为正常，不做处理
        if (errorCode in 200 until 300) return
        // 非成功状态码，给出对应提示
        val msg = catchHttpExceptionCode(errorCode)
        MLog.e(msg)
    }

    /**
     * 根据 HTTP 状态码判断错误类型并返回对应提示文字。
     *
     * @param errorCode HTTP 响应状态码
     * @return 用户可见的错误提示
     */
    private fun catchHttpExceptionCode(errorCode: Int): String = when (errorCode) {
        in 500..599 -> {
            // 服务器内部错误
            "服务器异常，请稍后重试"
        }
        else -> {
            // 客户端请求错误或其他未知情况
            "请求错误，请稍后重试"
        }
    }
}
