package com.example.studymvpexampleapplication.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.data.bean.Children
import com.example.studymvpexampleapplication.util.randomColor

// TreeChildAdapter：用于展示子标签并处理点击事件的适配器，继承自 BaseQuickAdapter
class TreeChildAdapter(
    // 父项在外层适配器中的位置，用于回调时区分父节点
    private val parentPos: Int,
    // 接收外层注册的子项点击回调接口
    private val listener: TreeAdapter.OnChildClickListener?
) : BaseQuickAdapter<Children, BaseViewHolder>(R.layout.item_tree_item) {

    /**
     * 将 Children 数据绑定到视图，每个子项调用一次
     * @param holder 当前项的 ViewHolder
     * @param item 当前 Children 数据对象
     */
    override fun convert(
        holder: BaseViewHolder,
        item: Children
    ) {
        // 设置标签文本
        holder.setText(R.id.tv_tag, item.name)
        // 设置随机文字颜色，保持与父适配器中同样的视觉效果
        holder.setTextColor(R.id.tv_tag, randomColor())
        // 获取标签 TextView 并设置点击事件
        holder.getView<TextView>(R.id.tv_tag).setOnClickListener {
            // 当子项被点击时，通过 listener 回调，并传递父位置、子位置和数据对象
            listener?.onChildClick(parentPos, holder.adapterPosition, item)
        }
    }
}
