package com.example.studymvpexampleapplication.util

import android.graphics.Color
import com.example.studymvpexampleapplication.util.ExceptionUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.Random

/**
 * 用于标识对象的类名
 * */
val Any.TAG: String
    get() {
        return javaClass.simpleName
    }

/**
 * toJsonString 函数则用于将对象转换为 JSON 格式的字符串
 * */
fun Any.toJsonString(): String {
    return Gson().toJson(this)
}

/**
 * 这个函数的作用是简化启动协程的过程，并提供了统一的异常处理方式
 *
 * @param block: 这是一个挂起函数，它以 CoroutineScope 作为接收者并不返回任何结果。这个函数表示要在协程中执行的代码块。
 * @param onError: 这是一个接受 Throwable 类型参数的函数，用于处理在协程执行过程中发生的异常。
 * @param onComplete: 这是一个不带参数的函数，表示在协程执行完成后要执行的操作。
 * */
fun launchCoroutine(
    block: suspend CoroutineScope.() -> Unit,
    onError: (e: Throwable) -> Unit = { _: Throwable -> },
    onComplete: () -> Unit = {}
) {
    MainScope().launch(
        CoroutineExceptionHandler { _, throwable ->
            run {
                // 统一处理错误
                ExceptionUtil.catchException(throwable)
                onError(throwable)
            }
        }
    ) {
        try {
            block.invoke(this)
        } finally {
            onComplete()
        }
    }
}

/**
 * 随机颜色
 */
fun randomColor(): Int {
    Random().run {
        //rgb取值0-255，但是值过大,就越接近白色,会看不清,所以限制在200
        val red = nextInt(200)
        val green = nextInt(200)
        val blue = nextInt(200)
        return Color.rgb(red, green, blue)
    }
}