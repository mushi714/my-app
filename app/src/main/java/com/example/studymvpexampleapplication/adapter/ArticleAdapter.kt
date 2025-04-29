package com.example.studymvpexampleapplication.adapter

import android.text.Html
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.data.bean.ArticleDetail

/**
 * ArticleAdapter 继承自 BRVAH 提供的 BaseQuickAdapter，
 * 用于为 RecyclerView 提供文章列表的 ViewHolder 绑定与加载更多功能。
 *
 * @param com.example.studymvpexampleapplication.data.bean.ArticleDetail 数据类型，对应单条文章详情
 * @param com.chad.library.adapter.base.viewholder.BaseViewHolder BRVAH 自带的简单 ViewHolder
 * @param com.example.studymvpexampleapplication.R.layout.item_article 列表项布局资源 ID
 */
class ArticleAdapter : BaseQuickAdapter<ArticleDetail, BaseViewHolder>(R.layout.item_article),
    LoadMoreModule {  // 启用“加载更多”模块

    init {
        // 注册 child view 点击事件，这里指定点赞图标可响应点击
        addChildClickViewIds(R.id.article_favorite)
    }

    /**
     * 将数据绑定到 ViewHolder
     *
     * @param holder BRVAH 提供的 BaseViewHolder，封装了 itemView
     * @param item   当前绑定的数据对象 ArticleDetail
     */
    override fun convert(holder: BaseViewHolder, item: ArticleDetail) {
        // 将 HTML 格式的标题字符串转换为富文本并设置到标题 TextView
        holder.setText(R.id.article_title, Html.fromHtml(item.title))

        // 设置章节名称
        holder.setText(R.id.article_chapter, item.chapterName)

        // 设置友好日期字符串
        holder.setText(R.id.article_date, item.niceDate)

        // 根据是否已收藏，加载不同的图标到点赞 ImageView
        val favView = holder.getView<ImageView>(R.id.article_favorite)
        if (item.collect) {
            // 已收藏时，显示高亮的心形图标
            Glide.with(context)
                .load(R.drawable.ic_like_checked)
                .into(favView)
        } else {
            // 未收藏时，显示灰色的心形图标
            Glide.with(context)
                .load(R.drawable.ic_like_normal)
                .into(favView)
        }
    }

}