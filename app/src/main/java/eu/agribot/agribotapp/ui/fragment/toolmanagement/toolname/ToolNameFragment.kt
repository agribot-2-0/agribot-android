package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolname


import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent

import eu.agribot.agribotapp.common.navigation.events.ToolParamsNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementNameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToolNameFragment :
    BaseFragment<ToolNameViewModel, FragmentToolManagementNameBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_management_name
    override val viewModel: ToolNameViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentToolManagementNameBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolParamsNavigationEvent -> navigateToToolName()
        else -> Unit
    }

    private fun navigateToToolName() {
        navigationController?.navigate(R.id.action_toolManagementNameFragment_to_toolManagementParamsFragment)
    }


}
