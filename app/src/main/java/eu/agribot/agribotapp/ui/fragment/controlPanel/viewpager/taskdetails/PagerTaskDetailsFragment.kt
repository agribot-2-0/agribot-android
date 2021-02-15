package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.PagerTaskDetailsBinding
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateAdapter
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import kotlinx.android.synthetic.main.pager_task_details.robotTaskRecycler
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PagerTaskDetailsFragment :
    BaseFragment<PagerTaskDetailsViewModel, PagerTaskDetailsBinding>() {
    override val layoutResourceId: Int = R.layout.pager_task_details
    override val viewModel: PagerTaskDetailsViewModel by viewModel { parametersOf(requireContext()) }

    private val robotTaskStateAdapter = RobotStateAdapter()
    override fun bindViewVariables(viewBinding: PagerTaskDetailsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getActualTask()
        observeViewModel()
        setupRecycler()
    }

    private fun observeViewModel() {
        viewModel.robotDataStateList.observe(this::addTaskStateItems)
    }

    private fun setupRecycler() {
        robotTaskRecycler.adapter = robotTaskStateAdapter
        robotTaskRecycler.layoutManager = GridLayoutManager(context, 2)
    }

    private fun addTaskStateItems(robotTaskStateItems: List<RobotStateViewModel>) {
        robotTaskStateAdapter.items = robotTaskStateItems
    }
}
