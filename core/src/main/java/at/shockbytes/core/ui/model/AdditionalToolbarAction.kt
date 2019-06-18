package at.shockbytes.core.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AdditionalToolbarAction(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val changeWithAnimation: Boolean,
    val onActionClick: (() -> Unit)
)