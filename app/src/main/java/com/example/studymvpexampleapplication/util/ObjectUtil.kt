package com.example.studymvpexampleapplication.util

import android.os.Build
import android.util.LongSparseArray
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.util.SparseLongArray
import androidx.collection.SimpleArrayMap

/**
 * ObjectUtil 工具类，用于判断任意对象是否“为空”
 * - 支持常见集合、数组、CharSequence、Map 以及 Android 优化集合类型（SparseArray 系列、SimpleArrayMap）等。
 * - 若对象为 null，或内容/长度为 0，则视为空。
 */
object ObjectUtil {

    /**
     * 判断对象是否为空
     *
     * @param obj 任意对象，可能为：
     *            - null
     *            - 原生数组（Array<*>）
     *            - CharSequence（String、Spannable 等）
     *            - Collection（List、Set 等）
     *            - Map（HashMap、LinkedHashMap 等）
     *            - SimpleArrayMap（androidx.collection.SimpleArrayMap）
     *            - SparseArray（android.util.SparseArray）
     *            - SparseBooleanArray、SparseIntArray、SparseLongArray（API ≥ 18）
     * @return true 代表“空”或不可用；false 代表至少有一个元素或非空字符串
     */
    fun isEmpty(obj: Any?): Boolean {
        // 1. 空对象
        if (obj == null) {
            return true
        }
        // 2. 原生数组，长度为 0
        if (obj.javaClass.isArray && java.lang.reflect.Array.getLength(obj) == 0) {
            return true
        }
        // 3. 字符序列（String、CharSequence 等），长度为 0
        if (obj is CharSequence && obj.isEmpty()) {
            return true
        }
        // 4. Java 通用集合 Collection（List、Set 等），isEmpty() 即可判断
        if (obj is Collection<*> && obj.isEmpty()) {
            return true
        }
        // 5. Java 通用 Map（HashMap、TreeMap 等），isEmpty() 即可判断
        if (obj is Map<*, *> && obj.isEmpty()) {
            return true
        }
        // 6. AndroidX 提供的优化版 Map：SimpleArrayMap，底层以数组实现，isEmpty() 可判断是否无元素&#8203;
        if (obj is androidx.collection.SimpleArrayMap<*, *> && obj.isEmpty()) {
            return true
        }
        // 7. Android 原生 SparseArray，key→value 映射，避免自动装箱；size() 为 0 时无元素&#8203;
        if (obj is SparseArray<*> && obj.size() == 0) {
            return true
        }
        // 8. Android 原生 SparseBooleanArray，存布尔值映射；size() 为 0 时无元素&#8203;
        if (obj is SparseBooleanArray && obj.size() == 0) {
            return true
        }
        // 9. Android 原生 SparseIntArray，存整型映射；size() 为 0 时无元素&#8203;
        if (obj is SparseIntArray && obj.size() == 0) {
            return true
        }
        // 10. Android Lollipop MR2 及以上新增的 SparseLongArray，存长整型映射；size() 为 0 时无元素&#8203;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {  // API 18+
            if (obj is SparseLongArray && obj.size() == 0) {
                return true
            }
        }
        // 11. 以上情况都不满足，则视为非空
        return false
    }

    /**
     * 判断对象是否非空，等价于 !isEmpty(obj)
     *
     * @param obj 任意对象
     * @return true 代表至少有一个元素或非空字符串；false 代表“空”或 null
     */
    fun isNotEmpty(obj: Any?): Boolean {
        return !isEmpty(obj)
    }
}
