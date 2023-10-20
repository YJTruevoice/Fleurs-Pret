package com.arthur.commonlib.utils

import android.content.Context
import android.content.SharedPreferences
import com.arthur.commonlib.ability.AppKit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.ref.SoftReference

object SPUtils {

    private var mSPFactory: ISPFactory = DefaultSPFactory()

    /**
     * 默认sp实例，存放全局通用配置
     */
    private var mDefaultSP: SharedPreferences = mSPFactory.getDefaultSP(AppKit.context)

    /**
     * 其他sp实例，如按用户、业务功能分组等
     */
    private var mSPHashMap: HashMap<String, SoftReference<SharedPreferences>> = HashMap(8)

    /**
     * 用于解析处理存取的实体的gson对象
     */
    val gson: Gson by lazy {

        Gson()
    }

    fun setSPFactory(factory: ISPFactory) {
        mSPFactory = factory
        //设置factory后防止已经初始化过某些sp实例，重新获取
        mDefaultSP = mSPFactory.getDefaultSP(AppKit.context)
        mSPHashMap.clear()
    }


    @JvmOverloads
    fun putData(key: String, value: Any, group: String = "") {
        val sp = getSP(group)
        putData(key, value, sp)
    }

    fun putData(key: String, value: Any, sp: SharedPreferences) {
        when(value) {
            is Int -> {
                sp.edit().putInt(key, value).apply()
            }
            is String -> {
                sp.edit().putString(key, value).apply()
            }
            is Long -> {
                sp.edit().putLong(key, value).apply()
            }
            is Float -> {
                sp.edit().putFloat(key, value).apply()
            }
            is Boolean -> {
                sp.edit().putBoolean(key, value).apply()
            }
            else -> {
                sp.edit().putString(key, gson.toJson(value)).apply()
            }
        }
    }


    inline fun <reified T> getData(key: String, defaultValue: T, group: String = ""): T {
        val sp = getSP(group)
        return getData(key, defaultValue, sp)
    }

    inline fun <reified T> getData(key: String, defaultValue: T, sp: SharedPreferences): T {
        return when(defaultValue) {
            is Int -> {
                (sp.getInt(key, defaultValue as Int) as? T)?: defaultValue
            }
            is String -> {
                (sp.getString(key, defaultValue as String) as? T)?: defaultValue
            }
            is Long -> {
                (sp.getLong(key, defaultValue as Long) as? T)?: defaultValue
            }
            is Float -> {
                (sp.getFloat(key, defaultValue as Float) as? T)?: defaultValue
            }
            is Boolean -> {
                (sp.getBoolean(key, defaultValue as Boolean) as? T)?: defaultValue
            }
            else -> {
                getObj<T>(key, sp)?: defaultValue
            }
        }
    }

    /**
     * 移除指定sp下指定key内容
     */
    fun remove(key: String, group: String = "") {
        remove(key, getSP(group))
    }

    fun remove(key: String, sp: SharedPreferences) {
        if (sp.contains(key)) {
            sp.edit().remove(key).apply()
        }
    }

    inline fun <reified T> getObj(key: String, group: String = ""): T? {
        val sp = getSP(group)
        return getObj(key, sp)
    }

    inline fun <reified T> getObj(key: String, sp: SharedPreferences): T? {
        var result: T? = null
        try {
            result =
                gson.fromJson<T>(sp.getString(key, null), object : TypeToken<T>() {}.type) ?: null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 这五个兼容java用的
     */
    @JvmOverloads
    fun getString(key: String, defaultValue: String = "", group: String = ""): String {
        return getData(key, defaultValue, group)
    }
    @JvmOverloads
    fun getInt(key: String, defaultValue: Int = 0, group: String = ""): Int {
        return getData(key, defaultValue, group)
    }
    @JvmOverloads
    fun getLong(key: String, defaultValue: Long = 0L, group: String = ""): Long {
        return getData(key, defaultValue, group)
    }
    @JvmOverloads
    fun getFloat(key: String, defaultValue: Float = 0f, group: String = ""): Float {
        return getData(key, defaultValue, group)
    }
    @JvmOverloads
    fun getBoolean(key: String, defaultValue: Boolean = false, group: String = ""): Boolean {
        return getData(key, defaultValue, group)
    }

    /**
     * 获取sp实例，如果名字为空则获取默认sp
     */
    fun getSP(name: String = ""): SharedPreferences {
        return if (name == "${AppKit.context.packageName}_preferences" || name.isEmpty()) {
            mDefaultSP
        } else {
            var sp = mSPHashMap[name]?.get()
            if (sp == null) {
                sp = mSPFactory.getSP(AppKit.context, name)
                mSPHashMap[name] = SoftReference(sp)
            }
            sp
        }
    }

    /**
     * 清空指定sp
     */
    fun clearSP(name: String) {
        //防止传空字符串误清空默认sp
        if (name.isNotEmpty()) {
            getSP(name).edit().clear().apply()
        }
    }
}

interface ISPFactory {
    fun getDefaultSP(context: Context): SharedPreferences
    fun getSP(context: Context, name: String): SharedPreferences
}

class DefaultSPFactory : ISPFactory {
    override fun getDefaultSP(context: Context): SharedPreferences {
        return getSP(context, "${context.packageName}_preferences")
    }

    override fun getSP(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(
            name,
            Context.MODE_PRIVATE
        )
    }

}


/**
 * 扩展方法，方便链式调用SPUtils.getSP().putData()
 */
fun SharedPreferences.putData(key: String, value: Any) {
    SPUtils.putData(key, value, this)
}

/**
 * 扩展方法，方便链式调用SPUtils.getSP().getData()
 */
inline fun <reified T> SharedPreferences.getData(key: String, defaultValue: T) : T {
    return SPUtils.getData(key, defaultValue, this)
}

/**
 * 扩展方法，方便链式调用SPUtils.getSP().getObj()
 */
inline fun <reified T> SharedPreferences.getObj(key: String): T? {
    return SPUtils.getObj(key)
}

/**
 * 扩展方法，方便链式调用SPUtils.getSP().remove()
 */
fun SharedPreferences.remove(key: String) {
    SPUtils.remove(key, this)
}
