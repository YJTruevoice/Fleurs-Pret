package com.facile.immediate.electronique.fleurs.pret.main

import com.facile.immediate.electronique.fleurs.pret.R

object UniqueFeatureUtil {
    fun generateFeatures(): List<FeatureEntity> {
        return mutableListOf(
            FeatureEntity(
                backgroundTint = R.color.color_F6F3FF,
                featureImgSrc = R.mipmap.icon_feature_plus_bas,
                featureTextId = R.string.text_taux_plus_bas
            ),
            FeatureEntity(
                backgroundTint = R.color.color_EAF7FF,
                featureImgSrc = R.mipmap.icon_feature_rapide_des_fonds,
                featureTextId = R.string.text_d_blocage_rapide_des_fonds
            ),
            FeatureEntity(
                backgroundTint = R.color.color_FFF5DC,
                featureImgSrc = R.mipmap.icon_feature_options_flexibles,
                featureTextId = R.string.text_options_de_dur_e_plus_flexibles
            ),
        )
    }
}