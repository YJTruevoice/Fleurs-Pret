package com.facile.immediate.electronique.fleurs.pret.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arthur.baselib.structure.base.BaseModel
import com.arthur.baselib.structure.mvvm.BaseViewModel

class FirstViewModel(application: Application) : BaseViewModel<BaseModel>(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}