package at.shockbytes.core.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

data class BottomNavigationTab(
    @IdRes val id: Int,
    @DrawableRes val stateListResource: Int,
    @DrawableRes val icon: Int,
    val title: String
)