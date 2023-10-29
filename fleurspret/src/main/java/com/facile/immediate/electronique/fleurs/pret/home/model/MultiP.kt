package com.facile.immediate.electronique.fleurs.pret.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
"afraidDecemberSlimClassicalTechnology": "maxCreditAmount",
"energeticFightTobaccoSpade": "overdueDays",
"lowTopicStraightImpression": "reapplicationDays",
"foggyEveryoneLivingArmyPrinter": "appName",
"chineseGeographyTenseHopefulSpirit": "logoUrl",
"simplePlanetAugust": "mobiKey",
"rudeReceptionCyclistArcticHunger": "viewStatus",
"bigGrandsonPetrolLetter": "curUserId",
"neitherFilmReligiousBuddhism": "extendDuration",
"normalBillClinicMercifulBay": "orderId",
"successfulStayIceHim": "appSsid",
"theseMommyBroadHour": "jumpFlag",
"comfortableMountain": "jumpStateTitle",
"passiveEntireUnhappyValley": "jumpButtonTitle"
参数名	类型	必含	说明
maxCreditAmount    [string]	否	可申请金额
appName    [string]	否	app名称
logoUrl    [string]	否	app logo地址
viewStatus    [string]	否	产品状态: 0-可申请, 1-还款中, 2-逾期, 3-审核中, 4-拒绝, 5-放款失败,6-放款处理中
curUserId    [string]	否	当前产品 userId
orderId    [string]	否	订单ID
appSsid    [string]	否	当前产品 产品号
overdueDays    [string]	否	逾期天数 ---秘鲁
 */
@Parcelize
data class MultiP(
    val afraidDecemberSlimClassicalTechnology: String? = null,
    val energeticFightTobaccoSpade: String? = null,
    val lowTopicStraightImpression: String? = null,
    val foggyEveryoneLivingArmyPrinter: String? = null,
    val chineseGeographyTenseHopefulSpirit: String? = null,
    val simplePlanetAugust: String? = null,
    val rudeReceptionCyclistArcticHunger: String? = null,
    val bigGrandsonPetrolLetter: String? = null,
    val neitherFilmReligiousBuddhism: String? = null,
    val normalBillClinicMercifulBay: String? = null,
    val successfulStayIceHim: String? = null,
    val theseMommyBroadHour: String? = null,
    val comfortableMountain: String? = null,
    val passiveEntireUnhappyValley: String? = null,
    var rankingSrc: Int = 0,
) : Parcelable
