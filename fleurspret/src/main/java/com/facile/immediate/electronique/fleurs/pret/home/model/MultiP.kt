package com.facile.immediate.electronique.fleurs.pret.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultiP(
    var rankingSrc: Int = 0,
) : ProInfo(), Parcelable
