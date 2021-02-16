package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.robotdata

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.PagerRobotDataBinding
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateAdapter
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import kotlinx.android.synthetic.main.pager_robot_data.robotDataRecycler
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PagerRobotDataFragment :
    BaseFragment<PagerRobotDataViewModel, PagerRobotDataBinding>() {

    private val robotDataAdapter = RobotStateAdapter()

    override val layoutResourceId: Int = R.layout.pager_robot_data
    override val viewModel: PagerRobotDataViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: PagerRobotDataBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRobotStateData()
        observeViewModel()
        setupRecycler()
    }

    private fun observeViewModel() {
        viewModel.robotDataStateList.observe(this::addRobotStateItems)
    }

    private fun setupRecycler() {
        robotDataRecycler.adapter = robotDataAdapter
        robotDataRecycler.layoutManager = GridLayoutManager(context, 2)
    }

    private fun addRobotStateItems(robotStateItems: List<RobotStateViewModel>) {
        robotDataAdapter.items = robotStateItems
    }
}
