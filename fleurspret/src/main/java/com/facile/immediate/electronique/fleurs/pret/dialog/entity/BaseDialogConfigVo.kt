package com.facile.immediate.electronique.fleurs.pret.dialog.entity

import android.os.Parcelable
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.StringUtil
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseDialog
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseConfirmCancelDialog
import kotlinx.parcelize.Parcelize

/**
 *
 * created by guo.lei on 2022.06.17
 *
 */
@Parcelize
open class BaseDialogConfigEntity(
    /**
     * 标题icon资源id
     */
    var titleIcResId: Int = 0,
    /**
     * 标题icon链接
     */
    var titleIcUrl: String? = null,
    /**
     * 标题
     */
    var title: String? = null,
    /**
     * 取消按钮文案
     */
    var cancelText: String? = null,
    /**
     * 确认按钮文案
     */
    var confirmText: String? = null,
    /**
     * 取消按钮回调
     */
    var cancelCallback: ((BaseDialog) -> Unit)? = null,
    /**
     * 确认按钮回调
     */
    var confirmCallback: ((BaseDialog) -> Unit)? = null,
    /**
     * 是否展示关闭按钮
     */
    var showClose: Boolean = false,
    /**
     * 是否可返回关闭
     */
    var backCancelAble: Boolean = true,
    /**
     * 点击外部是否消失
     */
    var touchOutsideCancelAble: Boolean = true,
    /**
     * 点击按钮时是否默认关闭
     */
    var dismissOnBtnClick: Boolean = true,
) : Parcelable

@Parcelize
class CommonDialogConfigEntity(
    /**
     * 主图资源id
     */
    var imgResId: Int = 0,
    /**
     * 主图链接
     */
    var imgUrl: String? = null,
    /**
     * 正文
     */
    var content: CharSequence? = null,
    /**
     * 富文本正文
     */
    var richContent: String? = null,
    /**
     * 说明
     */
    var hint: String? = null,
    /**
     * 图片高度
     */
    private var imageHeight: Int = 0
) : BaseDialogConfigEntity(), Parcelable {
    fun setImageHeight(height: Int) {
        imageHeight = height.coerceAtMost(BaseConfirmCancelDialog.MAX_IMG_HEIGHT.dp2px(null))
    }

    fun getImageHeight(): Int {
        return imageHeight
    }

    fun isImageModel(): Boolean {
        return imgResId > 0 || !StringUtil.isEmpty(imgUrl)
    }

    fun getImageSrc(): Any? {
        return if (isImageModel()) {
            if (!StringUtil.isEmpty(imgUrl)) {
                return imgUrl
            } else imgResId
        } else null
    }
}

@Parcelize
class CommonSingleInputDialogConfigEntity(
    /**
     * 输入信息，支持多项输入
     */
    var inputInfo: InputInfo? = null,

    /**
     * 确认按钮回调
     */
    var confirmInputCallback: ((String?, BaseDialog) -> Unit)? = null,
) : BaseDialogConfigEntity(), Parcelable

@Parcelize
class CommonMutiInputDialogConfigEntity(
    /**
     * 输入信息，支持多项输入
     */
    var inputInfoList: MutableList<InputInfo>? = null,

    /**
     * 确认按钮回调
     */
    var confirmInputCallback: ((List<Pair<Int, String>>?, BaseDialog) -> Unit)? = null,
) : BaseDialogConfigEntity(), Parcelable

@Parcelize
class InputInfo(
    /**
     * 输入文本
     */
    var text: String? = null,
    /**
     * 提示文字
     */
    var hint: String? = null,
    /**
     * 最大行数
     */
    var maxLines: Int = 3,

    /**
     * 最大长度
     */
    var maxLength: Int = 0,

    /**
     * 非法输入
     */
    var errorMsg: String? = null,

    /**
     * 是否必填，默认必填
     */
    var notNull: Boolean = true,

    /**
     * 回调标识
     */
    var token: Int = 0
) : Parcelable