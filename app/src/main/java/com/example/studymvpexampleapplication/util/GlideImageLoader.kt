package com.example.studymvpexampleapplication.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

/**
 * GlideImageLoader 继承自 Banner 库（或其他框架）中定义的 ImageLoader 抽象类，
 * 用于统一管理图片加载逻辑。这里以 Glide 为示例实现，实际项目中可替换为 Picasso、Coil 等。
 */
class GlideImageLoader : ImageLoader() {

    /**
     * displayImage：图片加载回调方法
     *
     * @param context    上下文对象，用来启动 Glide 请求（通常传入 Activity 或 Fragment 的 context）
     * @param path       图片路径，这里类型定义为 Any 是为了兼容多种数据源：
     *                   - URL（String）
     *                   - 本地文件（File 或 Uri）
     *                   - 资源 ID（Int）
     *                   在使用时，需要根据自己传入的类型进行强转，切勿随意强转导致 ClassCastException。
     * @param imageView  目标 ImageView，用来显示加载后的图片
     *
     * 注意：
     * 1. 这里使用 Kotlin 的非空断言（!!），若 context 或 imageView 为 null，会抛出 NullPointerException，
     *    推荐在调用处确保不传入 null，或改为使用安全调用（?.）并做空值判断。
     * 2. Glide 会自动处理列表滚动时的取消与重用，无需手动清除旧请求。
     */
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        // 如果 context 或 imageView 为 null，直接返回，避免崩溃
        if (context == null || imageView == null) return

        // 使用 Glide 加载图片：
        // - with(context)：指定生命周期，与 Activity/Fragment 同步，自动在 onDestroy 时取消请求
        // - load(path)：加载不同类型的图片资源（String, Uri, File, Int 等）
        // - into(imageView)：将图片展示到目标 ImageView
        Glide.with(context)
            .load(path)
            .into(imageView)
    }
}
