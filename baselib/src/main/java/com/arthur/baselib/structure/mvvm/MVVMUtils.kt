package com.arthur.baselib.structure.mvvm

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.arthur.baselib.structure.base.IBaseModel
import com.arthur.commonlib.utils.ReflectUtils.getTargetTFromObj
import java.io.Serializable

object MVVMUtils {
    fun getIntentByMapOrBundle(
        context: Context?,
        clz: Class<out Activity>?,
        map: Map<String, *>? = null,
        bundle: Bundle? = null
    ): Intent {
        val intent = Intent(context, clz)
        map?.forEach { entry ->
            @Suppress("UNCHECKED_CAST")
            when (val value = entry.value) {
                is Boolean -> {
                    intent.putExtra(entry.key, value)
                }
                is BooleanArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Byte -> {
                    intent.putExtra(entry.key, value)
                }
                is ByteArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Char -> {
                    intent.putExtra(entry.key, value)
                }
                is CharArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Short -> {
                    intent.putExtra(entry.key, value)
                }
                is ShortArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Int -> {
                    intent.putExtra(entry.key, value)
                }
                is IntArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Long -> {
                    intent.putExtra(entry.key, value)
                }
                is LongArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Float -> {
                    intent.putExtra(entry.key, value)
                }
                is FloatArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Double -> {
                    intent.putExtra(entry.key, value)
                }
                is DoubleArray -> {
                    intent.putExtra(entry.key, value)
                }
                is String -> {
                    intent.putExtra(entry.key, value)
                }
                is CharSequence -> {
                    intent.putExtra(entry.key, value)
                }
                is Parcelable -> {
                    intent.putExtra(entry.key, value)
                }
                is Serializable -> {
                    intent.putExtra(entry.key, value)
                }
                is Bundle -> {
                    intent.putExtra(entry.key, value)
                }
                is Intent -> {
                    intent.putExtra(entry.key, value)
                }
                is ArrayList<*> -> {
                    val any = if (value.isNotEmpty()) {
                        value[0]
                    } else null
                    when (any) {
                        is String -> {
                            intent.putExtra(entry.key, value as ArrayList<String>)
                        }
                        is Parcelable -> {
                            intent.putExtra(entry.key, value as ArrayList<Parcelable>)
                        }
                        is Int -> {
                            intent.putExtra(entry.key, value as ArrayList<Int>)
                        }
                        is CharSequence -> {
                            intent.putExtra(entry.key, value as ArrayList<CharSequence>)
                        }
                        else -> {
                            throw RuntimeException("不支持此类型 $value")
                        }
                    }
                }
                is Array<*> -> {
                    when {
                        value.isArrayOf<String>() -> {
                            intent.putExtra(entry.key, value as Array<String>)
                        }
                        value.isArrayOf<Parcelable>() -> {
                            intent.putExtra(entry.key, value as Array<Parcelable>)
                        }
                        value.isArrayOf<CharSequence>() -> {
                            intent.putExtra(entry.key, value as Array<CharSequence>)
                        }
                        else -> {
                            throw RuntimeException("不支持此类型 $value")
                        }
                    }
                }
                else -> {
                    throw RuntimeException("不支持此类型 $value")
                }
            }
        }
        bundle?.let { intent.putExtras(bundle) }
        return intent
    }
}

/**
 * 从MVVM框架的View声明中创建ViewModel，必定是一个BaseViewModel
 */
@Suppress("UNCHECKED_CAST")
fun <VM : BaseViewModel<out IBaseModel>> generateDeclaredViewModel(
    host: Any,
    clz: Class<*>,
    application: Application,
    viewModelStoreOwner: ViewModelStoreOwner
): VM {
    return generateViewModel(
        viewModelStoreOwner, application, getTargetTFromObj(
            host,
            clz,
            BaseViewModel::class.java
        ) ?: BaseViewModel::class.java as Class<VM>
    )
}

/**
 * 生成ViewModel实例
 */
@Suppress("UNCHECKED_CAST")
fun <VM : ViewModel> generateViewModel(
    viewModelStoreOwner: ViewModelStoreOwner,
    application: Application,
    clz: Class<VM>
): VM {
    return ViewModelProvider(
        viewModelStoreOwner,
        ViewModelProvider.AndroidViewModelFactory(application)
    )[clz]
}