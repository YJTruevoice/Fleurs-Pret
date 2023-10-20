package com.arthur.baselib.structure.mvvm.entity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import java.io.Serializable

class LaunchActivityResultInfo(
        val launcher: ActivityResultLauncher<Intent>,
        val clazz: Class<out Activity>,
        val bundle: Bundle? = null,
        val map: Map<String, *>? = null
) : Serializable {
        companion object {
                private const  val serialVersionUID = -15266L
        }
}