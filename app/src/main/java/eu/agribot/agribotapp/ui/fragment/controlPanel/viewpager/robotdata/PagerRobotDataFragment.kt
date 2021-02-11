package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.robotdata

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.PagerRobotDataBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PagerRobotDataFragment :
    BaseFragment<PagerRobotDataViewModel, PagerRobotDataBinding>() {

    override val layoutResourceId: Int = R.layout.pager_robot_data
    override val viewModel: PagerRobotDataViewModel by viewModel()

    override fun bindViewVariables(viewBinding: PagerRobotDataBinding) {
        viewBinding.viewModel = viewModel
    }
}
