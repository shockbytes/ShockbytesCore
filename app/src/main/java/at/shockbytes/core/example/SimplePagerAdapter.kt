package at.shockbytes.core.example

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import at.shockbytes.core.ui.model.BottomNavigationTab

class SimplePagerAdapter(fm: FragmentManager, private val tabs: List<BottomNavigationTab>): FragmentPagerAdapter(fm) {

    override fun getItem(index: Int): Fragment {
        return SimpleFragment.newInstance(tabs[index].title)
    }

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position].title
    }

}