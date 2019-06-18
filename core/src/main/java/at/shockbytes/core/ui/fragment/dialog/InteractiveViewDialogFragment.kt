package at.shockbytes.core.ui.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.view.View
import at.shockbytes.core.ShockbytesInjector

/**
 * @author Martin Macheiner
 * Date: 16.01.2018.
 *
 * DialogFragment without usual frame and DialogFragment specific buttons
 *
 */
abstract class InteractiveViewDialogFragment<T : ShockbytesInjector, L> : BaseDialogFragment<T>() {

    protected var applyListener: ((L) -> Unit)? = null

    abstract val containerView: View

    /**
     * Most probably R.style.AppTheme
     */
    abstract val appTheme: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, appTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setView(containerView)
            .create()
    }

    fun setOnApplyListener(listener: (L) -> Unit): InteractiveViewDialogFragment<T, L> {
        applyListener = listener
        return this
    }


    override fun onResume() {
        super.onResume()
        setupViews()
    }

    abstract fun setupViews()

}