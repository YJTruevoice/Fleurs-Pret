package com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean

import android.graphics.drawable.Drawable
import android.text.TextUtils

open class CommonChooseListItem(
    name: String,
    value: Any,
    //是否展示弱化灰色文字
    var weak: Boolean = false,
    val drawableStart: Drawable? = null,
    val rightDrawable: Drawable? = null,
    val nameEllipsize: TextUtils.TruncateAt = TextUtils.TruncateAt.END,
    val fakeBold: Boolean = true
) : BaseChooseItem(name, value) {
    override fun equals(other: Any?): Boolean {
        return other != null && other is CommonChooseListItem && other.name == name && other.value == value
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}