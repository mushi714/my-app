package com.example.studymvpexampleapplication.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.data.bean.ArticleX
import com.example.studymvpexampleapplication.util.randomColor

class NaviRightAdapter(private val onItemClick: (position: Int) -> Unit): BaseQuickAdapter<ArticleX, BaseViewHolder>((R.layout.item_navi_right)) {
    override fun convert(
        holder: BaseViewHolder,
        item: ArticleX
    ) {
        holder.setText(R.id.tv_tag, item.title)
        holder.setTextColor(R.id.tv_tag, randomColor())

        holder.itemView.setOnClickListener {
            onItemClick(getItemPosition(item))
        }
    }
}