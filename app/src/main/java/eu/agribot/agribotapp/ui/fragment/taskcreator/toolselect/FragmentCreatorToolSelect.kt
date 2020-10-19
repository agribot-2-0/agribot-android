package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorToolSelectBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCreatorToolSelect :
    BaseFragment<CreatorToolSelectViewModel, FragmentCreatorToolSelectBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_creator_tool_select
    override val viewModel: CreatorToolSelectViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentCreatorToolSelectBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is DashboardNavigationEvent -> handleDashboardNavigationEvent()
        else -> Unit
    }

    private fun handleDashboardNavigationEvent(){
        navigationController?.navigate(R.id.action_taskCreatorTaskToolFragment_to_dashboardFragment)
    }
}
