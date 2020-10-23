package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskWeatherNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorToolSelectBinding
import kotlinx.android.synthetic.main.view_bottom_creator_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCreatorToolSelect :
    BaseFragment<CreatorToolSelectViewModel, FragmentCreatorToolSelectBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_creator_tool_select
    override val viewModel: CreatorToolSelectViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentCreatorToolSelectBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        left_button.setOnClickListener { navigateToWeatherAttributionsFragment() }
        right_button.setOnClickListener { viewModel.sendTaskData() }
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is DashboardNavigationEvent -> navigateToDashboard()
        else -> Unit
    }

    private fun navigateToDashboard() {
        navigationController?.navigate(R.id.action_taskCreatorTaskToolFragment_to_creatorFragment)
    }

    private fun navigateToWeatherAttributionsFragment() {
        navigationController?.navigate(R.id.action_taskCreatorTaskToolFragment_to_taskCreatorTaskWeatherFragment)
    }
}
