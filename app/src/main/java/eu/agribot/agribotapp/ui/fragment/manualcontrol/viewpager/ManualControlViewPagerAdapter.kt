package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.agribot.agribotapp.ui.fragment.controlPanel.taskselect.TaskSelectFragment
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.robotdata.PagerRobotDataFragment
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails.PagerTaskDetailsFragment
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.currentstate.ManualControlCurrentStateFragment
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.ToolControlFragment

class ManualControlViewPagerAdapter (private val pagerFragment: Fragment) :
    FragmentStateAdapter(pagerFragment) {

    override fun getItemCount(): Int {
        return ITEM_COUNT_NUMBER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            ROBOT_DATA_POSITION -> ManualControlCurrentStateFragment()
            TASK_DETAILS_POSITION -> ToolControlFragment()
            else -> ManualControlCurrentStateFragment()
        }
    }

    companion object {
        const val ITEM_COUNT_NUMBER = 2

        const val ROBOT_DATA_POSITION = 0

        const val TASK_DETAILS_POSITION = 1

    }
}
