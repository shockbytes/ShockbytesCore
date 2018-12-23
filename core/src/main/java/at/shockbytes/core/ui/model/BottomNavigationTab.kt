package at.shockbytes.core.ui.model

import android.support.annotation.DrawableRes
import android.support.annotation.IdRes

data class BottomNavigationTab(
    @IdRes val id: Int,
    @DrawableRes val stateListResource: Int,
    @DrawableRes val icon: Int,
    val title: String
)