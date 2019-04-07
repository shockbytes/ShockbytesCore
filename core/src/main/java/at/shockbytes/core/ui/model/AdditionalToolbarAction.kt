package at.shockbytes.core.ui.model

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

data class AdditionalToolbarAction(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val changeWithAnimation: Boolean,
    val onActionClick: (() -> Unit)
)