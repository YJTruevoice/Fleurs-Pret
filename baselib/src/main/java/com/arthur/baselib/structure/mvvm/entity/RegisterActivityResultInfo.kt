package com.arthur.baselib.structure.mvvm.entity

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import java.io.Serializable

class RegisterActivityResultInfo(
    val callback: ActivityResultCallback<ActivityResult>,
    val block: (ActivityResultLauncher<Intent>) -> Unit
) : Serializable {
    companion object {
        private const val serialVersionUID = -74L
    }
}