package at.shockbytes.core.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Author:  Martin Macheiner
 * Date:    08.06.2018
 */
@Parcelize
data class ShockbytesUser(
    val givenName: String?,
    val displayName: String?,
    val email: String?,
    val photoUrl: Uri?,
    val providerId: String?,
    val authToken: String?
) : Parcelable