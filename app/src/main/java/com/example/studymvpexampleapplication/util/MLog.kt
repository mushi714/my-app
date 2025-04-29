package com.example.studymvpexampleapplication.util

import android.util.Log

/**
 * MLog 工具类，用于统一管理日志输出。
 * 通过 isDebug 常量控制是否打印日志，方便在发布版中关闭日志。
 */
object MLog {
    /** 是否开启调试模式，若为 false 则不会输出日志 */
    private const val isDebug = true

    /**
     * 输出错误日志
     *
     * @param msg 要输出的错误信息
     */
    fun e(msg: String) {
        if (isDebug) {
            Log.e(TAG, msg)
        }
    }

    /**
     * 输出调试日志
     *
     * @param msg 要输出的调试信息
     */
    fun d(msg: String) {
        if (isDebug) {
            Log.d(TAG, msg)
        }
    }

    /**
     * 输出信息日志
     *
     * @param msg 要输出的信息
     */
    fun i(msg: String) {
        if (isDebug) {
            Log.i(TAG, msg)
        }
    }

    /**
     * 输出警告日志
     *
     * @param msg 要输出的警告信息
     */
    fun w(msg: String) {
        if (isDebug) {
            Log.w(TAG, msg)
        }
    }
}
