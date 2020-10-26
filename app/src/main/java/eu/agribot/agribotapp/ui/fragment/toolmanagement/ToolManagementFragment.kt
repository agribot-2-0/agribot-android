package eu.agribot.agribotapp.ui.fragment.toolmanagement

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToolManagementFragment :
    BaseFragment<ToolManagementViewModel, FragmentToolManagementBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_management
    override val viewModel: ToolManagementViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentToolManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNameNavigationEvent -> navigateToToolName()
        else -> Unit
    }

    private fun navigateToToolName() {
        navigationController?.navigate(R.id.action_toolManagementFragment_to_toolManagementNameFragment)
    }

}
