package at.shockbytes.core.ui.activity.base

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import at.shockbytes.core.ShockbytesInjector
import at.shockbytes.core.util.CoreUtils

/**
 * Author:  Martin Macheiner
 * Date:    02.01.2018
 */
abstract class TintableBackNavigableActivity<T : ShockbytesInjector> : BackNavigableActivity<T>() {

    /**
     * @ColorRes
     */
    abstract val abDefColor: Int
    /**
     * @ColorRes
     */
    abstract val abTextDefColor: Int
    /**
     * @ColorRes
     */
    abstract val sbDefColor: Int

    /**
     * @ColorRes
     */
    abstract val colorPrimary: Int

    /**
     * @ColorRes
     */
    abstract val colorPrimaryDark: Int

    /**
     * @ColorRes
     */
    abstract val colorPrimaryText: Int

    /**
     * @DrawableRes
     */
    abstract val upIndicator: Int

    @ColorInt
    private var textColor: Int = 0 // Will be initialized in the onCreate method

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textColor = ContextCompat.getColor(this, colorPrimaryText)
    }

    @JvmOverloads
    fun tintHomeAsUpIndicator(
        tint: Boolean = false,
        @ColorInt tintColor: Int = Color.WHITE
    ) {

        if (tint) {
            val drawable = CoreUtils.vector2Drawable(applicationContext, upIndicator)
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            supportActionBar?.setHomeAsUpIndicator(drawable)
        } else {
            supportActionBar?.setHomeAsUpIndicator(upIndicator)
        }
    }

    fun tintTitle(title: String) {
        val text = SpannableString(title)
        text.setSpan(
            ForegroundColorSpan(textColor), 0, text.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        supportActionBar?.title = text
    }

    fun tintSystemBarsWithText(
        @ColorInt actionBarColor: Int?, @ColorInt actionBarTextColor: Int?,
        @ColorInt statusBarColor: Int?, title: String? = null,
        animated: Boolean = false,
        useSameColorsForBoth: Boolean = true
    ) {

        // Default initialize if not set
        val abColor = actionBarColor ?: ContextCompat.getColor(applicationContext, abDefColor)
        var sbColor = statusBarColor ?: ContextCompat.getColor(applicationContext, sbDefColor)
        val abtColor = actionBarTextColor ?: ContextCompat.getColor(applicationContext, abTextDefColor)
        textColor = actionBarTextColor ?: textColor

        if (useSameColorsForBoth) {
            sbColor = abColor
        }

        // Set and tint text of action bar
        val newTitle = title ?: supportActionBar?.title
        tintTitle(newTitle?.toString() ?: "")

        if (animated) {
            tintSystemBarsAnimated(abColor, sbColor)
        } else {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(abColor))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = sbColor
            }
        }
        tintHomeAsUpIndicator(tint = true, tintColor = abtColor)
    }

    private fun tintSystemBarsAnimated(@ColorInt newColor: Int, @ColorInt newColorDark: Int) {

        val primary = ContextCompat.getColor(this, colorPrimary)
        val primaryDark = ContextCompat.getColor(this, colorPrimaryDark)

        val animatorToolbar = ValueAnimator.ofObject(ArgbEvaluator(), primary, newColor)
            .setDuration(300)
        animatorToolbar.addUpdateListener { valueAnimator ->
            supportActionBar?.setBackgroundDrawable(ColorDrawable(valueAnimator.animatedValue as Int))
        }
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), primaryDark, newColorDark)
            .setDuration(300)
        // Suppress lint, because we are only setting applyListener, when api is available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAnimation.addUpdateListener { valueAnimator ->
                window.statusBarColor = valueAnimator.animatedValue as Int
            }
        }

        val set = AnimatorSet()
        set.playTogether(animatorToolbar, colorAnimation)
        set.start()
    }

}