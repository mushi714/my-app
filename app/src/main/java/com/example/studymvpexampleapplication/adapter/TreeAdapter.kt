package com.example.studymvpexampleapplication.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.data.bean.Children
import com.example.studymvpexampleapplication.data.bean.Tree

// TreeAdapter：基于 BaseQuickAdapter 显示 Tree 列表并支持子项点击回调
class TreeAdapter : BaseQuickAdapter<Tree, BaseViewHolder>(R.layout.item_tree) {
    /**
     * 数据与视图绑定方法，每个列表项会调用一次
     * @param holder 当前项的 ViewHolder
     * @param item 当前项的 Tree 数据对象
     */
    override fun convert(
        holder: BaseViewHolder,
        item: Tree
    ) {
        // 设置父节点标题文本
        holder.setText(R.id.tv_tree_title, item.name)
        // 获取父项布局中的内层 RecyclerView，用于显示子列表
        val rvItemTree = holder.getView<RecyclerView>(R.id.rv_item_tree)

        // 根据 item.isShow 控制子列表的可见性和指示图标方向
        if (item.isShow) {
            rvItemTree.visibility = View.VISIBLE
            holder.setImageResource(R.id.iv_toggle, R.drawable.ic_up)
        } else {
            rvItemTree.visibility = View.GONE
            holder.setImageResource(R.id.iv_toggle, R.drawable.ic_down)
        }

        // 创建子列表的适配器，传入当前父项的位置和点击回调
        val treeChildAdapter = TreeChildAdapter(holder.adapterPosition, childClickListener)

        // 使用瀑布流布局显示子项，这里示例纵向 8 列排列
        rvItemTree.layoutManager = StaggeredGridLayoutManager(
            8,
            StaggeredGridLayoutManager.VERTICAL
        )
        // 绑定子适配器
        rvItemTree.adapter = treeChildAdapter

        // 将当前父节点的 children 数据设置到子适配器
        treeChildAdapter.setList(item.children)
    }

    // 定义子项点击回调接口，供外部注册以接收事件
    interface OnChildClickListener {
        /**
         * @param parentPos 父项在外层列表中的位置
         * @param childPos 子项在当前子列表中的位置
         * @param child 对应的 Children 数据对象
         */
        fun onChildClick(parentPos: Int, childPos: Int, child: Children)
    }

    // 存储外部设置的回调实现
    private var childClickListener: OnChildClickListener? = null
    /**
     * 注册子项点击事件监听器
     * @param listener OnChildClickListener 实现，用于回调子项点击
     */
    fun setOnChildClickListener(listener: OnChildClickListener) {
        childClickListener = listener
    }

}