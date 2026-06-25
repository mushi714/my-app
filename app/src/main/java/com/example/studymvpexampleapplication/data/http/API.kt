package com.example.studymvpexampleapplication.data.http

import com.example.studymvpexampleapplication.base.BaseBean
import com.example.studymvpexampleapplication.data.bean.Article
import com.example.studymvpexampleapplication.data.bean.Banner
import com.example.studymvpexampleapplication.data.bean.Collect
import com.example.studymvpexampleapplication.data.bean.Hotkey
import com.example.studymvpexampleapplication.data.bean.Navi
import com.example.studymvpexampleapplication.data.bean.Project
import com.example.studymvpexampleapplication.data.bean.ProjectChild
import com.example.studymvpexampleapplication.data.bean.Tree
import com.example.studymvpexampleapplication.data.bean.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API 接口封装类，用于定义网络请求的基础地址和各项具体接口。
 */
class API {

    companion object {
        /**
         * 应用中所有 Retrofit 请求的基础 URL，
         * 所有子接口中的相对路径都会拼接到此地址后面
         */
        const val BASE_URL = "https://wanandroid.com/"
    }

    /**
     * WAZApi 接口定义了与 WanAndroid 后端交互的所有 HTTP 请求方法，
     * 使用 Retrofit 的注解来声明请求类型、路径和参数，
     * 并统一返回封装在 BaseBean<T> 中的响应体。
     */
    interface WAZApi {

        // -----------------------【登录注册】----------------------

        /**
         * 登录接口
         *
         * @param username 用户名（账号）
         * @param password 用户密码
         * @return 返回 BaseBean<User>，其中 data 部分包含登录成功后的用户信息
         */
        @FormUrlEncoded
        @POST("user/login")
        suspend fun login(
            @Field("username") username: String?,
            @Field("password") password: String?
        ): BaseBean<User>

        /**
         * 注册接口
         *
         * @param username 用户名（账号）
         * @param password 密码
         * @param repassword 重复输入的密码，用于校验
         * @return 返回 BaseBean<User>，其中 data 部分包含注册成功后的用户信息
         */
        @FormUrlEncoded
        @POST("user/register")
        suspend fun register(
            @Field("username") username: String?,
            @Field("password") password: String?,
            @Field("repassword") repassword: String?
        ): BaseBean<User>

        // -----------------------【首页相关】----------------------

        /**
         * 获取首页文章列表
         *
         * @param page 页码，从 0 开始
         * @return 返回 BaseBean<Article>，其中 data 部分包含文章列表及分页信息
         */
        @GET("article/list/{page}/json")
        suspend fun getArticleList(@Path("page") page: Int): BaseBean<Article>

        /**
         * 获取首页轮播图数据
         *
         * @return 返回 BaseBean<MutableList<Banner>>，其中 data 部分是 Banner 对象列表
         */
        @GET("banner/json")
        suspend fun getBanner(): BaseBean<MutableList<Banner>>

        // -----------------------【体系】----------------------

        /**
         * 获取体系数据列表
         *
         * @return 返回 BaseBean<MutableList<Tree>>，其中 data 部分是体系分类列表
         */
        @GET("tree/json")
        suspend fun getTree(): BaseBean<MutableList<Tree>>

        /**
         * 获取某个体系下的详情文章列表
         *
         * @param page 页码
         * @param cid 体系 ID
         * @return 返回 BaseBean<Article>，其中 data 部分包含该体系下的文章列表
         */
        @GET("article/list/{page}/json?")
        suspend fun getTreeChild(
            @Path("page") page: Int,
            @Query("cid") cid: Int
        ): BaseBean<Article>

        // -----------------------【导航】----------------------

        /**
         * 获取导航数据
         *
         * @return 返回 BaseBean<MutableList<Navi>>，其中 data 部分包含导航分类列表
         */
        @GET("navi/json")
        suspend fun getNavi(): BaseBean<MutableList<Navi>>

        // -----------------------【项目】----------------------

        /**
         * 获取项目分类数据
         *
         * @return 返回 BaseBean<MutableList<Project>>，其中 data 部分是项目分类列表
         */
        @GET("project/tree/json")
        suspend fun getProject(): BaseBean<MutableList<Project>>

        /**
         * 获取项目列表数据
         *
         * @param page 页码
         * @param cid 项目分类 ID
         * @return 返回 BaseBean<ProjectChild>，其中 data 部分包含该分类下项目列表
         */
        @GET("project/list/{page}/json?")
        suspend fun getProjectChild(
            @Path("page") page: Int,
            @Query("cid") cid: Int
        ): BaseBean<ProjectChild>

        // -----------------------【搜索】----------------------

        /**
         * 搜索文章
         *
         * @param page 页码
         * @param k 搜索关键词
         * @return 返回 BaseBean<Article>，其中 data 部分包含搜索到的文章列表
         */
        @FormUrlEncoded
        @POST("article/query/{page}/json?")
        suspend fun getSearchList(
            @Path("page") page: Int,
            @Field("k") k: String
        ): BaseBean<Article>

        /**
         * 获取搜索热词
         *
         * @return 返回 BaseBean<MutableList<Hotkey>>，其中 data 部分是热词列表
         */
        @GET("hotkey/json")
        suspend fun getHotkey(): BaseBean<MutableList<Hotkey>>

        // -----------------------【收藏】----------------------

        /**
         * 获取收藏的文章列表
         *
         * @param page 页码
         * @return 返回 BaseBean<Collect>，其中 data 部分包含收藏文章分页数据
         */
        @GET("lg/collect/list/{page}/json?")
        suspend fun getCollectList(@Path("page") page: Int): BaseBean<Collect>

        /**
         * 收藏文章（站内）
         *
         * @param id 文章 ID
         * @return 返回 BaseBean<String>，其中 data 部分一般为空或提示信息
         */
        @POST("lg/collect/{id}/json")
        suspend fun collect(@Path("id") id: Int): BaseBean<String>

        /**
         * 取消收藏文章（文章列表入口）
         *
         * @param id 文章 ID
         * @return 返回 BaseBean<String>，其中 data 部分一般为空
         */
        @POST("lg/uncollect_originId/{id}/json")
        suspend fun unCollect(@Path("id") id: Int): BaseBean<String>

        /**
         * 取消收藏文章（我的收藏页面入口）
         *
         * @param id 文章 ID
         * @param originId 原始文章 ID，用于服务端还原来源
         * @return 返回 BaseBean<String>，其中 data 部分一般为空
         */
        @FormUrlEncoded
        @POST("lg/uncollect/{id}/json")
        suspend fun unCollect1(
            @Path("id") id: Int,
            @Field("originId") originId: Int
        ): BaseBean<String>
    }
}
