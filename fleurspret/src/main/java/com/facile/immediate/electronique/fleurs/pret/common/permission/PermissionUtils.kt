package com.facile.immediate.electronique.fleurs.pret.common.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Pair
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData

class PermissionUtils {

    companion object {

        fun with(activity: FragmentActivity): PermissionRequestManager {
            return PermissionRequestManager(activity.supportFragmentManager)
        }

        fun with(fragment: Fragment): PermissionRequestManager {
            return PermissionRequestManager(fragment.childFragmentManager)
        }

        fun with(fragmentManager: FragmentManager): PermissionRequestManager {
            return PermissionRequestManager(fragmentManager)
        }

        /**
         * 申请相机权限提示语
         */
        val MESSAGE_CAMERA = Pair("需要授权相机及存储权限，以正常使用拍照、扫码等功能",
                "在设置-应用-牛客-权限中开启相机及存储权限，以正常使用拍照、扫码等功能")
        /**
         * 申请存储权限提示语
         */
        val EXTERNAL_STORAGE = Pair("需要授权存储权限，以正常使用照片、视频、下载等相关功能",
                "在设置-应用-牛客-权限中开启存储权限，以正常使用下载、图片查看及选择、设置头像等功能")
        /**
         * 申请地理位置权限提示语
         */
        val FINE_LOCATION = Pair("需要授权地理位置权限，用于获取周边学校信息",
            "在设置-应用-牛客-权限中开启位置权限，可以获取周边学校信息")
    }

    class PermissionRequestManager(private val fm: FragmentManager) {
        private var permissionRequestFragment: PermissionRequestFragment? = null
        private val tag = "permissionRequestFragment"

        /**
         * @param message first:申请权限弹窗文字内容  second:对于被拒绝的权限给用户的提示弹窗的内容
         */
        @JvmOverloads
        fun requestPermissions(permissions: Array<out String>, message: Pair<String, String>? = null, cancelCallBack: (() -> Unit)? = null): MutableLiveData<PermissionRequestResult> {
            val msg = message ?: when {
                permissions.contains(Manifest.permission.CAMERA) -> {
                    MESSAGE_CAMERA
                }
                permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    EXTERNAL_STORAGE
                }
                permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    FINE_LOCATION
                }
                permissions.contains(Manifest.permission.READ_MEDIA_IMAGES) -> {
                    EXTERNAL_STORAGE
                }
                else -> {
                    null
                }
            }
            return getFragment().requestPermissions(permissions, msg, cancelCallBack)
        }

        /**
         * 无告知，直接申请权限
         */
        @JvmOverloads
        fun requestPermissionsDirect(permissions: Array<out String>): MutableLiveData<PermissionRequestResult> {
            return getFragment().requestPermissionsDirect(permissions)
        }

        private fun getFragment(): PermissionRequestFragment {
            permissionRequestFragment = permissionRequestFragment
                    ?: if (fm.findFragmentByTag(tag) == null) {
                        PermissionRequestFragment().apply {
                            if (Looper.myLooper() != Looper.getMainLooper()) {
                                val handler = Handler(Looper.getMainLooper())
                                handler.post {
                                    fm.beginTransaction().add(this, tag).commitNowAllowingStateLoss()
                                }
                            } else {
                                fm.beginTransaction().add(this, tag).commitNowAllowingStateLoss()
                            }
                        }
                    } else {
                        fm.findFragmentByTag(tag) as PermissionRequestFragment
                    }
            return permissionRequestFragment!!
        }
    }

}

class PermissionRequestFragment : Fragment() {

    private val permissionRequestCode = 666

    private val mLiveData: MutableLiveData<PermissionRequestResult> = MutableLiveData()

    private val permissionResultMap: HashMap<String, Int> = HashMap(4)

    private var specialPermissions: Array<out String>? = null

    private var requestMessage: Pair<String, String>? = null

    private var mCancelCallBack: (() -> Unit)? = null

    /**
     * 先弹出告知用户需要申请权限的弹窗，经用户同意后再进行权限申请
     */
    fun requestPermissions(permissions: Array<out String>, message: Pair<String, String>?, cancelCallBack: (() -> Unit)? = null): MutableLiveData<PermissionRequestResult> {
        requestMessage = message
        mCancelCallBack = cancelCallBack
        activity?.let { ac ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return request(permissions)
            } else {
//                NCCommonDialog.with(ac)
//                    .title("权限申请")
//                    .content(if (StringUtil.isEmpty(requestMessage?.first)) "需要授权权限，以正常使用相应功能" else requestMessage?.first)
//                    .cancel(ValuesUtils.getString(R.string.btn_str_cancel)){
//                        mCancelCallBack?.invoke()
//                    }
//                    .confirm(ValuesUtils.getString(R.string.btn_str_confirm)){
//                        specialPermissions = permissions
//                        request(permissions)
//                    }
//                    .backCancelAble(false)
//                    .touchOutsideCancelAble(false)
//                    .show()
            }
        }
        return mLiveData
    }

    /**
     * 直接申请，无告知
     */
    fun requestPermissionsDirect(permissions: Array<out String>): MutableLiveData<PermissionRequestResult> {
        activity?.let { ac ->
            request(permissions)
        }
        return mLiveData
    }

    private fun request(permissions: Array<out String>): MutableLiveData<PermissionRequestResult> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissions.isNotEmpty()) {
                requestPermissions(permissions, permissionRequestCode)
            } else {
                mLiveData.postValue(PermissionRequestResult(permissionResultMap))
            }
        } else {
            for (index in permissions.indices) {
                permissionResultMap[permissions[index]] = PackageManager.PERMISSION_GRANTED
            }
            mLiveData.postValue(PermissionRequestResult(permissionResultMap))
        }
        return mLiveData
    }

    /**
     * 判断是否需要弹出引导用户赋予权限的弹窗
     */
    private fun shouldShowRequestPermissionRationale(permissions: Array<out String>): Boolean {
        var shouldShow = false
        activity?.let {
            for (permission in permissions) {
                if ((ContextCompat.checkSelfPermission(it, permission) != PackageManager.PERMISSION_GRANTED
                                && !ActivityCompat.shouldShowRequestPermissionRationale(it, permission))
                        || ActivityCompat.shouldShowRequestPermissionRationale(it, permission)) {
                    shouldShow = true
                    break
                }
            }
        }
        return shouldShow
    }

    /**
     * 对于频繁请求的权限，在用户拒绝过之后再次需要请求时，引导用户在系统设置中赋予权限
     */
    private fun showRequestPermissionRationale() {
        activity?.let { ac ->
//            NCCommonDialog.with(ac)
//                .content(if (!StringUtil.isEmpty(requestMessage?.second)) requestMessage?.second else "请在设置-应用-牛客-权限中开启对应权限，以正常使用相应功能")
//                .cancel(ValuesUtils.getString(R.string.btn_str_cancel)) {
//                    mCancelCallBack?.invoke()
//                }
//                .confirm("去设置") {
//                    val intent = Intent()
//                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                    intent.data = Uri.fromParts("package", ac.packageName, null)
//                    startActivity(intent)
//                }
//                .backCancelAble(false)
//                .touchOutsideCancelAble(false)
//                .show()
        }
        specialPermissions = null
        requestMessage = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (index in grantResults.indices) {
            permissionResultMap[permissions[index]] = grantResults[index]
        }
        mLiveData.postValue(PermissionRequestResult(permissionResultMap))

        specialPermissions?.let {
            if (it.isNotEmpty() && shouldShowRequestPermissionRationale(it)) {
                showRequestPermissionRationale()
            }
        }
    }

}

class PermissionRequestResult(val permissionsResultMap: HashMap<String, Int>)

