package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.currentstate.ManualControlCurrentStateFragment
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.ToolControlFragment

class ManualControlViewPagerAdapter(private val pagerFragment: Fragment) :
    FragmentStateAdapter(pagerFragment) {

    override fun getItemCount(): Int {
        return PagerPostion.ITEM_COUNT.number
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PagerPostion.DISPLAY_STATE.number -> ManualControlCurrentStateFragment()
            PagerPostion.TOOL_CONTROL.number -> ToolControlFragment()
            else -> ManualControlCurrentStateFragment()
        }
    }
}
