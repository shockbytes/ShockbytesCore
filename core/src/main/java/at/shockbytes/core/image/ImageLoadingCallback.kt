package at.shockbytes.core.image

import android.graphics.drawable.Drawable

interface ImageLoadingCallback {

    fun onImageResourceReady(resource: Drawable?)

    fun onImageLoadingFailed(e: Exception?)

}