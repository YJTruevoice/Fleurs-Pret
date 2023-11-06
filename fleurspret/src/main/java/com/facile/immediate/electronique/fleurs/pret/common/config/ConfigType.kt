package com.facile.immediate.electronique.fleurs.pret.common.config

import com.arthur.commonlib.ability.AppKit
import com.facile.immediate.electronique.fleurs.pret.R


/**
{
"constantNovelistMessSureLibrary": "relationship",
"hardPianistSkillfulCloudyFuneral": "workType",
"anotherChurchExcellentDate": "incomeLevel",
"famousLegCafeteria": "workYears",
"happyFreshTownPotato": "bankNameList",
"looseManSauce": "bankAccountType",
"thirstyConditionSaturdayPunctualMiniskirt": "secRelationship",
"lowMonthGladGym": "numberOfChildren",
"flamingHearingElectricTypewriter": "educational",
"uselessPillowJuniorMyselfRipeService": "stateOfStudies",
"freePyramidLocalAddress": "houseTime",
"punctualPrizeLanternTechnique": "newrealterm",
"sureChemistryBigFairness": "sex",
"clearHighTechnicalSeaman": "curpType",
"safeVirtueClinicNewJewel": "collectionType",
"roundLieTankerGreenChurch": "politicalExposedStatus",
"certainCriminalCarelessKitchenConvenience": "nationality",
"littleJourneyScottishFuel": "employmentCategory",
"hopelessSchoolLid": "maritalStatus",
"aggressiveEarlyMidnight": "houseOwnership",
"eitherFlourSecondhand": "livedYears",
"usedCigaretteMainTemptationStrawberry": "sourceOfIncome",
"usefulJeansPlasticCarelessCheer": "position",
"politicalGentleHeightUniversity": "monthlyIncome",
"blueNextEye": "workedYears",
"modestDollarRainfall": "loanPurpose",
"metalCancerVirtueRainfall": "payType",
"finalLivingGermany": "custlinkmannumber",
"holyFurnitureUnmarriedBeardNationality": "salaryLimit",
"anotherJacketCivilDate": "homeVideoAddress",
"recentHappinessStupidSlightCaptain": "messageBoard",
"normalShameRopeCourse": "repayTypeFlag",
"majorRapidFarmDisabledFlower": "frequencyOfPay",
"moreDescriptionThread": "payday"
}
 */
enum class ConfigType {
    constantNovelistMessSureLibrary,
    thirstyConditionSaturdayPunctualMiniskirt,
    sureChemistryBigFairness,
    safeVirtueClinicNewJewel,
    aggressiveEarlyMidnight,
    looseManSauce,
    happyFreshTownPotato,
    anotherChurchExcellentDate,
    punctualPrizeLanternTechnique
}

enum class Sex(val code: String, val value: String) {
    man("0", AppKit.context.getString(R.string.text_m_le)),
    womon("1", AppKit.context.getString(R.string.text_femelle)),
    other("2", AppKit.context.getString(R.string.text_troisi_me_sexe)),
}