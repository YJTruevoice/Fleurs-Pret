package com.arthur.baselib.structure.base

import android.content.Intent
import android.os.Bundle

interface IArgumentsSetter {
    /**
     * 设置传参intent
     */
    fun setArgumentsIntent(intent: Intent?)

    /**
     * 传递 bundle，在这里可以获取
     */
    fun setArgumentsBundle(bundle: Bundle?)
}