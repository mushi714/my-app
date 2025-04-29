package com.example.studymvpexampleapplication.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.data.bean.Navi

class NaviLeftAdapter(private val onItemClick: (position: Int) -> Unit) : BaseQuickAdapter<Navi, BaseViewHolder>(R.layout.item_navi_left) {
    override fun convert(
        holder: BaseViewHolder,
        item: Navi
    ) {
        holder.setText(R.id.text_category, item.name)

        holder.itemView.setOnClickListener {
            onItemClick(getItemPosition(item))
        }
    }

}