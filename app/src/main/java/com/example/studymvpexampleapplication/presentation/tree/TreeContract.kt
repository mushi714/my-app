package com.example.studymvpexampleapplication.presentation.tree

import com.example.studymvpexampleapplication.base.mvp.BaseContract
import com.example.studymvpexampleapplication.common.RetrofitResponseListener
import com.example.studymvpexampleapplication.data.bean.Tree

// 定义 Tree 模块的契约接口，包含 Model、View 和 Presenter 三个部分
interface TreeContract : BaseContract {

    // Model 层：负责与数据源交互，获取树形结构数据
    interface Model : BaseContract.IBaseModel {
        /**
         * 异步获取树形结构列表
         * @param listener 回调接口，返回结果为 MutableList<Tree>
         */
        suspend fun getTree(listener: RetrofitResponseListener<MutableList<Tree>>)
    }

    // View 层：负责显示数据或错误信息
    interface View : BaseContract.IBaseView {
        /**
         * 数据请求成功时回调
         * @param treeMutableList 返回的树形结构数据列表
         */
        fun getTreeSuccess(treeMutableList: MutableList<Tree>)

        /**
         * 数据请求失败时回调
         * @param errorMessage 错误信息描述
         */
        fun getTreeError(errorMessage: String)
    }

    // Presenter 层：负责处理 View 请求并协调 Model 获取数据
    interface Presenter : BaseContract.IBasePresenter {
        /**
         * 发起获取树形结构数据的请求
         */
        fun getTree()
    }
}
