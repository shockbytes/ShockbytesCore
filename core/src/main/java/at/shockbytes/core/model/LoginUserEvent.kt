package at.shockbytes.core.model

import android.content.Intent

sealed class LoginUserEvent {
    data class SuccessEvent(val user: ShockbytesUser?, val showWelcomeScreen: Boolean) : LoginUserEvent()
    class LoginEvent(val signInIntent: Intent?) : LoginUserEvent()
    data class ErrorEvent(val errorMsg: Int) : LoginUserEvent()
}