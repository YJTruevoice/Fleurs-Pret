package com.arthur.baselib.structure.mvvm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.arthur.baselib.structure.mvvm.entity.LaunchActivityResultInfo
import com.arthur.baselib.structure.mvvm.entity.RegisterActivityResultInfo

/**
 *
 * created by guo.lei
 * on 2022.04.01
 */
open class BaseUIChangeLiveData {

    init {
        initStartActivityForResultEvent()
        initStartAndFinishEvent()
    }

    /*
        关闭页面
     */
    var finishPageEvent: SingleLiveEvent<Any?>? = null
    var finishPageEventWithResult: SingleLiveEvent<Pair<Int, Intent?>?>? = null
    var finishPageDelayEvent: SingleLiveEvent<Pair<Long?, String?>?>? = null
    /*
        打开页面
     */
    var startActivityByNameEvent: SingleLiveEvent<Pair<String, Bundle?>>? = null
    var startActivityEvent: SingleLiveEvent<Class<out Activity>>? = null
    var startActivityClearTaskEvent: SingleLiveEvent<Class<out Activity>>? = null
    var startActivityWithMapEvent: SingleLiveEvent<Pair<Class<out Activity>, Map<String, *>>>? =
        null
    var startActivityEventWithBundle: SingleLiveEvent<Pair<Class<out Activity>, Bundle?>>? =
        null
    /*
        打开页面 - 待返回结果
     */
    var registerForResultEvent: SingleLiveEvent<RegisterActivityResultInfo>? = null
    var startActivityForResultEvent: SingleLiveEvent<LaunchActivityResultInfo>? = null
    var startActivityForResultEventWithMap: SingleLiveEvent<LaunchActivityResultInfo>? = null
    var startActivityForResultEventWithBundle: SingleLiveEvent<LaunchActivityResultInfo>? = null

    private fun initStartActivityForResultEvent() {
        registerForResultEvent = SingleLiveEvent()
        startActivityForResultEvent = SingleLiveEvent()
        startActivityForResultEventWithMap = SingleLiveEvent()
        startActivityForResultEventWithBundle = SingleLiveEvent()
    }

    private fun initStartAndFinishEvent() {
        startActivityByNameEvent = SingleLiveEvent()
        startActivityEvent = SingleLiveEvent()
        startActivityClearTaskEvent = SingleLiveEvent()
        startActivityWithMapEvent = SingleLiveEvent()
        startActivityEventWithBundle = SingleLiveEvent()
        finishPageEvent = SingleLiveEvent()
        finishPageEventWithResult = SingleLiveEvent()
        finishPageDelayEvent = SingleLiveEvent()
    }
}