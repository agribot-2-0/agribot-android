package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.agribot.agribotapp.ui.fragment.controlPanel.taskselect.TaskSelectFragment
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.robotdata.PagerRobotDataFragment
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails.PagerTaskDetailsFragment

class DashboardPagerAdapter(private val pagerFragment: Fragment) :
    FragmentStateAdapter(pagerFragment) {

    override fun getItemCount(): Int {
        return ITEM_COUNT_NUMBER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            ROBOT_DATA_POSITION -> PagerRobotDataFragment()
            TASK_DETAILS_POSITION -> PagerTaskDetailsFragment()
            TASK_SELECT_POSITION -> TaskSelectFragment()
            else -> PagerRobotDataFragment()
        }
    }

    companion object {
        const val ITEM_COUNT_NUMBER = 3

        const val ROBOT_DATA_POSITION = 0

        const val TASK_DETAILS_POSITION = 1

        const val TASK_SELECT_POSITION = 2
    }
}
