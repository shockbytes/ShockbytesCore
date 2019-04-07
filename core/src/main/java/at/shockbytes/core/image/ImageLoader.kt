package at.shockbytes.core.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.annotation.Dimension
import android.support.annotation.DrawableRes
import android.widget.ImageView
import at.shockbytes.core.R
import io.reactivex.Single

interface ImageLoader {

    val defaultPlaceHolder: Int

    fun loadImage(context: Context,
                  url: String,
                  target: ImageView,
                  @DrawableRes placeholder: Int = defaultPlaceHolder,
                  circular: Boolean = false,
                  callback: ImageLoadingCallback? = null,
                  callbackHandleValues: Pair<Boolean, Boolean>? = null)

    fun loadImageResource(context: Context,
                          @DrawableRes resource: Int,
                          target: ImageView,
                          @DrawableRes placeholder: Int = defaultPlaceHolder,
                          withCrossFade: Boolean = false,
                          circular: Boolean = false,
                          callback: ImageLoadingCallback? = null,
                          callbackHandleValues: Pair<Boolean, Boolean>? = null)

    fun loadImageUri(context: Context,
                     uri: Uri,
                     target: ImageView,
                     @DrawableRes placeholder: Int = defaultPlaceHolder,
                     withCrossFade: Boolean = false,
                     circular: Boolean = false,
                     callback: ImageLoadingCallback? = null,
                     callbackHandleValues: Pair<Boolean, Boolean>? = null)


    fun loadImageWithCornerRadius(context: Context,
                                  url: String,
                                  target: ImageView,
                                  @DrawableRes placeholder: Int = defaultPlaceHolder,
                                  @Dimension cornerDimension: Int,
                                  callback: ImageLoadingCallback? = null,
                                  callbackHandleValues: Pair<Boolean, Boolean>? = null)

    // ------------------------------ Extension functions ------------------------------

    fun Uri.loadBitmap(context: Context): Single<Bitmap>

    fun Uri.loadRoundedBitmap(context: Context): Single<Bitmap>
}