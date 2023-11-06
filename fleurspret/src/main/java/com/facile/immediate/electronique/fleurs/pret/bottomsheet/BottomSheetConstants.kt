package com.facile.immediate.electronique.fleurs.pret.bottomsheet

/**
 *
 * created by guo.lei on 2022.08.25
 *
 */
class BottomSheetConstants {
    enum class Model {
        /**
         * 固定高度
         */
        FIXED,

        /**
         * 分段可折叠
         */
        COLLAPSIBLE
    }

    companion object {
        const val BOTTOM_SHEET_HEIGHT_DEFAULT = 500
    }
}