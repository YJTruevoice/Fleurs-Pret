package com.facile.immediate.electronique.fleurs.pret.net

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.arthur.baselib.utils.AppLanguageUtil
import com.arthur.commonlib.utils.ActivityManager
import com.arthur.commonlib.utils.AppUtils
import com.arthur.commonlib.utils.SPUtils
import com.arthur.network.BaseNetMgr
import com.arthur.network.NetConstant
import com.arthur.network.NetOptions
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.facile.immediate.electronique.fleurs.pret.BuildConfig
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.common.event.NetErrorRefresh
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseCountDownDialog
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseDialog
import com.facile.immediate.electronique.fleurs.pret.net.converter.CustomJsonConverter
import com.facile.immediate.electronique.fleurs.pret.net.interceptor.CommonParamsInterceptor
import com.facile.immediate.electronique.fleurs.pret.net.interceptor.ServerStatusInterceptor
import com.facile.immediate.electronique.fleurs.pret.utils.DeviceIdUtil
import com.facile.immediate.electronique.fleurs.pret.web.WebLoadTimeoutDialog
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference
import java.util.UUID
import java.util.concurrent.TimeUnit


/**
 * @Author arthur
 * @Date 2023/10/17
 * @Description
 */
class NetMgr : BaseNetMgr() {
    companion object {

        private val INSTANCE: NetMgr by lazy {
            NetMgr()
        }

        fun get(): NetMgr {
            return INSTANCE
        }
    }

    private var context: Context? = null

    private var netErrorDialog: BaseDialog? = null
    private var curActivityRef: WeakReference<FragmentActivity>? = null

    fun init(context: Context) {
        this.context = context
    }

    override val netOption: NetOptions
        get() = NetOptions().apply {
            // 设置超时时间
            setConnectTimeout(TimeOut.CONNECTION_TIME_OUT)
            setWriteTimeout(TimeOut.WRITE_TIME_OUT)
            setReadTimeout(TimeOut.READ_TIME_OUT)
            setTimeUnit(TimeUnit.SECONDS)

            // 设置主域名
            setDomainMain(BuildConfig.HOST)
            // 设置多个域名
            setDomainMap(
                mutableMapOf()
            )
            val appVersionName = context?.let { AppUtils.getAppVersionName(it) } ?: ""
            val appVersionCode = context?.let { AppUtils.getAppVersionCode(it).toString() } ?: "0"
            // 公共参数
            addCommonParams {
                hashMapOf(
                    CommonParamsKey.APP_SSID to CommonValue.DISK_NUM,
                    CommonParamsKey.USER_ID to UserManager.userId(),
                    CommonParamsKey.LBS to CommonValue.LBS_DEFAULT,// todo 定位成功后保持
                    CommonParamsKey.LANGUAGE to AppLanguageUtil.getAppLocale().language,
                    CommonParamsKey.VERSION_NAME to appVersionName,
                    CommonParamsKey.VERSION_CODE to appVersionCode,
                    CommonParamsKey.DEVICE_ID_ to DeviceIdUtil.getDeviceId(),
                    CommonParamsKey.IMEI to DeviceIdUtil.getDeviceId(),
                    CommonParamsKey.IP to "",
//                    CommonParamsKey.CHANNEL to CommonValue.GOOGLE_PLAY,
                    CommonParamsKey.SYSTEM_MODE to Build.MODEL,
                    CommonParamsKey.MOBILE_NO to "",
                    CommonParamsKey.GOOGLE_AD_ID to SPUtils.getString(
                        CommonParamsKey.GOOGLE_AD_ID,
                        defaultValue = UUID.randomUUID().toString().also {
                            SPUtils.putData(CommonParamsKey.GOOGLE_AD_ID, it)
                        }),
                    CommonParamsKey.GOOGLE_USER_AGENT to "",
                )
            }
            // 公共请求头
            addCommonHeaders {
                hashMapOf(
                    HeadersKey.CLIENT_ID to CommonValue.DISK_NUM,
                    HeadersKey.TOKEN to UserManager.token(),
                    HeadersKey.USER_ID to UserManager.userId(),
                    HeadersKey.CURRENT_USER_ID to "",
                    HeadersKey.CHANNEL to CommonValue.GOOGLE_PLAY,
                    HeadersKey.VERSION_NAME to appVersionName,
                    HeadersKey.VERSION_CODE to appVersionCode,
                    HeadersKey.DEVICE_ID to DeviceIdUtil.getDeviceId(),
                    HeadersKey.DEVICE_ID_ to DeviceIdUtil.getDeviceId(),
                    HeadersKey.IMEI to DeviceIdUtil.getDeviceId(),
//                    HeadersKey.MULTI_FLAG to CommonValue.MULTI_FLAG_DEFAULT,
                    HeadersKey.V_FLAG to "",
                )
            }

            setConverter(CustomJsonConverter())

            addInterceptor(CommonParamsInterceptor(getCommonParams()))
            addInterceptor(ServerStatusInterceptor())

            // stetho
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(StethoInterceptor())
            }

            // 设置是否debug环境
            setDebug(true)
            // 设置是否要证书验证 主要是抓包开关 Hybrid只是在测试期间使用 所以将这里开关关闭，可随时抓包
            setSslOpen(false)

            setTopActivityGetter {
                ActivityManager.getCurrentActivity() as? FragmentActivity
            }

            this@NetMgr.context?.let {
                registerServerStatusReceiver(it)

                setTipServiceTimeout(it.getString(R.string.text_d_lai_d_expiration_du_r_seau_veuillez_r_essayer_plus_tard))
                setTipConnectError(it.getString(R.string.text_le_r_seau_n_est_pas_disponible_veuillez_v_rifier_les_param_tres_r_seau))
                setTipConnectErrorOther(it.getString(R.string.text_anomalie_du_r_seau_veuillez_confirmer_que_le_r_seau_est_normal))
                setTipDataParseError(it.getString(R.string.text_exception_d_analyse_des_donn_es))
                setTipDataEmptyError(it.getString(R.string.text_exception_de_non_conformit_des_donn_es_les_donn_es_sont_vides))
                setTipDataIllegalError(it.getString(R.string.text_exception_de_non_conformit_des_donn_es))
                setTipUnknownError(it.getString(R.string.text_exception_inconnue))
                setTipDefaultBusinessError(it.getString(R.string.text_une_erreur_s_est_produite))
            }

            setErrorHandler { errorCode ->
                return@setErrorHandler if (
                    errorCode == NetConstant.ErrorCode.ERROR_SSL
                    || errorCode == NetConstant.ErrorCode.ERROR_NETWORK_UNKNOWN_HOST
                    || errorCode == NetConstant.ErrorCode.ERROR_NETWORK_CONNECT
                    || errorCode == NetConstant.ErrorCode.ERROR_SOCKET
                ) {
                    (ActivityManager.getCurrentActivity() as? FragmentActivity)?.let { curActivity ->
                        if (netErrorDialog?.isShowing == true) netErrorDialog?.dismiss()
                        if (curActivity.isFinishing) return@setErrorHandler true
                        getNetErrorDialog(curActivity)?.show()
                        true
                    } ?: false
                } else {
                    false
                }
            }
        }

    private fun getNetErrorDialog(curActivity: FragmentActivity): BaseDialog? {
        if (curActivityRef?.get() != curActivity) {
            curActivityRef = WeakReference(curActivity)
            netErrorDialog = createNetErrorDialog()
            return netErrorDialog!!
        }
        return netErrorDialog ?: let {
            netErrorDialog = createNetErrorDialog()
            netErrorDialog!!
        }
    }

    private fun createNetErrorDialog(): BaseDialog? {
        return curActivityRef?.get()?.let {
            WebLoadTimeoutDialog.with(it)
                .confirm(it.getString(R.string.text_actualizar)) {dialog ->
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    it.startActivity(intent)
                    EventBus.getDefault().post(NetErrorRefresh())
                }.build()
        }
    }


    /**
     * 注册网络请求状态 本地广播
     */
    private fun registerServerStatusReceiver(context: Context) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(context)
        val intentFilter = IntentFilter(NetBizConstant.ServerStatusAction.STATUS_ACTION)
        val serverStatusReceiver = ServerStatusReceiver()
        //注册本地广播接收器
        localBroadcastManager.registerReceiver(serverStatusReceiver, intentFilter)
    }

    /**
     * 超时时间
     */
    object TimeOut {
        const val CONNECTION_TIME_OUT = 30
        const val WRITE_TIME_OUT = 120
        const val READ_TIME_OUT = 30
    }

    /**
     * 公共参数 key
     */
    object CommonParamsKey {
        const val APP_SSID = "halfPainfulEverything"
        const val USER_ID = "radioactiveTeacherBasicBankLaserSuspect"
        const val LBS = "lbs"
        const val LANGUAGE = "thirstyUnderstandingDelightedEagle"
        const val VERSION_NAME = "upsetPrimaryEuropeanMatch"
        const val VERSION_CODE = "lemonLikelyFireworks"
        const val DEVICE_ID_ = "funnyLuckSaltyBat"
        const val IMEI = "holyDirectorAntarcticChildhood"
        const val IP = "facialLakeNaturalDigestProduction"
        const val CHANNEL = "nuclearCertainSauce"
        const val SYSTEM_MODE = "primaryPaperEitherPen"
        const val MOBILE_NO = "cottonFactBlindCancer"
        const val GOOGLE_AD_ID = "publicThemeJuicyHardshipFoolishDetermination"
        const val GOOGLE_USER_AGENT = "nextStopwatchInstitution"
    }

    /**
     * 请求头 key
     */
    object HeadersKey {
        const val CLIENT_ID = "russianTeapotScreen"//kindLazyForehead
        const val TOKEN = "dirtyCompositionUpsetRealBelief"
        const val USER_ID = "radioactiveTeacherBasicBankLaserSuspect"
        const val CURRENT_USER_ID = "taxSmogFallIndependentJourney"
        const val CHANNEL = "nuclearCertainSauce"
        const val VERSION_NAME = "upsetPrimaryEuropeanMatch"
        const val VERSION_CODE = "lemonLikelyFireworks"
        const val DEVICE_ID = "regularSignalHappinessLaserEaster"//hopefulGestureMiddle
        const val DEVICE_ID_ = "funnyLuckSaltyBat"
        const val IMEI = "holyDirectorAntarcticChildhood"
        const val MULTI_FLAG = "fondNecktieTram"
        const val V_FLAG = "justModestPlentyLiteraryMenu"
    }

    /**
     * 公共参数 和 通用请求头 以及其他 值 收集到这里
     */
    object CommonValue {
        const val DISK_NUM = "220"
        const val GOOGLE_PLAY = "googleplay"
        const val MULTI_FLAG_DEFAULT = "1"

        const val LANGUAGE_DEFAULT = "en"
        const val LBS_DEFAULT = "0,0"
    }
}