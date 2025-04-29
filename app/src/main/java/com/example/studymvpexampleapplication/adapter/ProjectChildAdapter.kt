package com.example.studymvpexampleapplication.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.data.bean.DataX

class ProjectChildAdapter : BaseQuickAdapter<DataX, BaseViewHolder>(R.layout.item_project_child),
    LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: DataX) {
        Glide.with(context).load(item.envelopePic).into(holder.getView(R.id.iv_project_img))
        holder.setText(R.id.tv_project_title, item.title)
        holder.setText(R.id.tv_project_desc, item.desc)
        holder.setText(R.id.tv_project_date, item.niceDate)
        holder.setText(R.id.tv_project_author, item.author)
    }
}