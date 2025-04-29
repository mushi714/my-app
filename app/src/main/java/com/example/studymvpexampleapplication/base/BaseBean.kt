package com.example.studymvpexampleapplication.base

/**
 * 通用响应包装类，用于封装后端返回的标准字段与业务数据。
 *
 * 该类为 Kotlin 的数据类（data class），编译器会自动生成 equals()/hashCode()/toString()/copy() 等方法，
 * 主要用于持有数据而非行为逻辑。
 *
 * @param T 泛型参数，代表业务数据的类型；在使用时由调用方指定具体类型，
 *           Kotlin 默认允许任何类型（上界为 Any?）
 *
 * @property errorMsg 后端返回的错误描述字符串，通常为空或 "success" 表示无错误
 * @property errorCode 后端返回的状态码，一般 0 表示成功，非 0 表示不同类型的失败
 * @property data      业务数据主体，类型为 T；成功时携带实际数据，失败时可能为 null 或包含错误信息
 */
data class BaseBean<T>(
    val errorMsg: String,
    val errorCode: Int,
    val data: T
)
