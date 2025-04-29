plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.studymvpexampleapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.studymvpexampleapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        /**
         * ViewBinding 是 Jetpack 提供的一项轻量级功能，
         * 它能够在编译时为每个布局文件生成对应的绑定类，
         * 使得我们可以通过类型安全和空安全的方式直接访问布局中的视图，从而几乎完全替代 findViewById()，并显著减少样板代码
         *
         * ViewBinding 会为每一个启用该特性的布局文件生成一个绑定类（<LayoutName>Binding），
         * 该类包含了所有带 android:id 的视图引用以及一个 getRoot() 方法，用于获取根视图
         * */
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /**
     * Gson（JSON 序列化/反序列化）
     * 实现 JSON 数据与 Java/Kotlin 对象的互相转换
     * */
    implementation ("com.google.code.gson:gson:2.7")
    /**
     * Glide（图片加载）
     * 高效加载网络/本地图片，支持缓存和图片处理
     * */
    implementation ("com.github.bumptech.glide:glide:3.7.0")
    /**
     * YUtils（工具集合）
     * 提供常用工具类（日志、屏幕适配、权限管理等）
     * */
    implementation ("com.github.yechaoa.YUtils:yutilskt:3.4.0")
    /**
     * Banner（轮播图）
     * 实现图片/视图轮播效果
     * */
    implementation ("com.youth.banner:banner:1.4.10")
    /**
     * Lifecycle 系列
     *
     * LiveData：实现数据观察
     * ViewModel：管理界面相关数据
     * lifecycle-extensions：旧版扩展库（已废弃，建议迁移到独立组件）
     * */
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0-rc01")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // 极大简化 RecyclerView 适配器 的编写，并提供强大而灵活的分页、头尾布局、多类型、拖拽、滑动删除等功能
    implementation ("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.1") {
        /**
         * 此依赖中排除了 lifecycle-viewmodel-ktx 模块 解决模块冲突
         * */
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
    }
    /**
     * Collection KTX
     * 为 Android 集合框架提供 Kotlin 扩展方法
     * */
    api("androidx.collection:collection-ktx:1.2.0")
    /**
     * Kotlin 协程
     * 提供协程支持，简化异步编程
     * */
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    /**
     * 网络请求
     * Retrofit + OkHttp
     * Retrofit：声明式 REST API 客户端
     * OkHttp：底层网络引擎
     * Logging Interceptor：网络请求日志拦截器
     * Gson Converter：响应数据转换
     * */
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.okhttp3:okhttp:4.9.1")
    api("com.squareup.okhttp3:logging-interceptor:4.9.1")
    api("com.squareup.retrofit2:converter-gson:2.9.0")


}