package at.shockbytes.core.example

import android.os.Bundle
import at.shockbytes.core.ShockbytesInjector
import at.shockbytes.core.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.simple_fragment.*

class SimpleFragment: BaseFragment<ShockbytesInjector>() {

    override val layoutId: Int = R.layout.simple_fragment

    override val snackBarForegroundColorRes: Int
        get() = R.color.colorPrimary
    override val snackBarBackgroundColorRes: Int
        get() = R.color.colorAccent

    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = arguments?.getString("title")
    }

    override fun setupViews() {
        text_fragment.text = title
    }

    override fun injectToGraph(appComponent: ShockbytesInjector?) {
    }

    override fun bindViewModel() {
    }

    override fun unbindViewModel() {
    }

    companion object {

        fun newInstance(title: String): SimpleFragment {
            return SimpleFragment().apply {
                this.arguments = Bundle().apply {
                    this.putString("title", title)
                }
            }
        }

    }

}