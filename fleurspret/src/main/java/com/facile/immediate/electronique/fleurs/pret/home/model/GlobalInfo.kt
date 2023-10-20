package com.facile.immediate.electronique.fleurs.pret.home.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
amountWithdrawalBanner    [string]	否	提额banner
banner    [string]	否	订单页-banner
recommendBanner    [string]	否	订单页-推荐产品banner
withdrawalVoucher    [string]	否	首页-提额券金额
maxCreditAmount    [string]	否	首页-最大金额
recommendedBannerReject    [string]	否	拒绝页-推荐banner
nextLoanDays    [string]	否	拒绝页-下次贷款天数
nextWithdrawalCouponRp    [string]	否	还款成功页-下一次提额券金额
guidePageAmount    [string]	否	进件页-引导金额
nextWithdrawalVoucher    [string]	否	金额选择页-下一次提额券金额
bottomCarouselContent    [string]	否	金额选择页-底部轮播文案
lockDuration    [string]	否	金额选择页-锁定期限
lockCreditAmount    [string]	否	金额选择页-锁定金额
marketingAmount    [string]	否	金额选择页-复贷营销金额（同首页提额券）
emailList    [string]	否	客服页-email
mobileList    [string]	否	客服页-phone
whatsappList    [string]	否	客服页-whatsApp
customerServiceContent    [string]	否	客服页-文案
praiseOpenFlag    [string]	否	好评弹窗-开关
praisePopupTimes    [string]	否	好评弹窗-弹出次数
extendConfirmSwitch    [string]	否	展期确认弹窗-开关
extendConfirmTitle    [string]	否	展期确认弹窗-标题（可配2标题）
extendConfirmContent    [string]	否	展期确认弹窗-内容（可配2内容）
msgBulletFrameOpenFlag    [string]	否	消息弹窗-开关
msgBulletFrameTitle    [string]	否	消息弹窗-标题
msgBulletFrameMsg    [string]	否	消息弹窗-内容
isOpenGpReview    [string]	否	谷歌评论-开关
contractLoan    [string]	否	借款合同url
 */
@Parcelize
data class GlobalInfo(
    val amountWithdrawalBanner: String? = null,
    val banner: String? = null,
    val recommendBanner: String? = null,
    val withdrawalVoucher: String? = null,
    val maxCreditAmount: String? = null,
    val recommendedBannerReject: String? = null,
    val nextLoanDays: String? = null,
    val nextWithdrawalCouponRp: String? = null,
    val guidePageAmount: String? = null,
    val nextWithdrawalVoucher: String? = null,
    val bottomCarouselContent: String? = null,
    val lockDuration: String? = null,
    val lockCreditAmount: String? = null,
    val marketingAmount: String? = null,
    val emailList: String? = null,
    val mobileList: String? = null,
    val whatsappList: String? = null,
    val customerServiceContent: String? = null,
    val praiseOpenFlag: String? = null,
    val praisePopupTimes: String? = null,
    val extendConfirmSwitch: String? = null,
    val extendConfirmTitle: String? = null,
    val extendConfirmContent: String? = null,
    val msgBulletFrameOpenFlag: String? = null,
    val msgBulletFrameTitle: String? = null,
    val msgBulletFrameMsg: String? = null,
    val isOpenGpReview: String? = null,
    val contractLoan: String? = null

) : Parcelable