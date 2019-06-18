package at.shockbytes.core.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatDrawableManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.leinardi.android.speeddial.SpeedDialView

object CoreUtils {

    fun vector2Drawable(c: Context, res: Int): Drawable = AppCompatDrawableManager.get().getDrawable(c, res)

    fun String.colored(@ColorInt color: Int): SpannableString {
        val spannable = SpannableString(this)
        spannable.setSpan(
            ForegroundColorSpan(color),
            0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    fun SpeedDialView.toggleVisibility(millis: Long = 300) {
        this.hide()
        Handler().postDelayed({ this.show() }, millis)
    }
}