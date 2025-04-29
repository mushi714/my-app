package com.example.studymvpexampleapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.yechaoa.yutilskt.YUtils

/**
 * 网络状态工具类
 * 提供检查设备当前网络（移动数据和 Wi-Fi）是否可用的方法
 */
object NetWorkUtils {

    /**
     * 检查设备是否有可用网络（包含移动数据或 Wi-Fi）
     *
     * @return 若移动网络或 Wi-Fi 任一可用，则返回 true；否则返回 false
     */
    fun isConnected(): Boolean {
        // 获取全局 Application Context，判断任一网络类型是否可用
        return isNetworkConnected(YUtils.getAppContext())
                || isWifiConnected(YUtils.getAppContext())
    }

    /**
     * 判断移动数据网络（2G/3G/4G/5G）是否可用
     *
     * @param context 用于获取系统网络服务的 Context，若为 null 则直接返回 false
     * @return 若当前存在且可用的移动数据网络，则返回 true；否则返回 false
     */
    fun isNetworkConnected(context: Context?): Boolean {
        return context?.let {
            // 获取系统的 ConnectivityManager
            val cm = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // activeNetworkInfo 可能为 null，使用安全调用
            cm.activeNetworkInfo?.isAvailable ?: false
        } ?: false
    }

    /**
     * 判断 Wi-Fi 网络是否可用
     *
     * @param context 用于获取系统网络服务的 Context，若为 null 则直接返回 false
     * @return 若 Wi-Fi 网络接口存在且可用，则返回 true；否则返回 false
     */
    fun isWifiConnected(context: Context?): Boolean {
        return context?.let {
            // 获取系统的 ConnectivityManager
            val cm = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // 通过 TYPE_WIFI 获取 Wi-Fi 网络状态，可能为 null
            cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.isAvailable ?: false
        } ?: false
    }
}
