package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolParamsNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementParamsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToolParamsFragment :
    BaseFragment<ToolParamsViewModel, FragmentToolManagementParamsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_management_params
    override val viewModel: ToolParamsViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentToolManagementParamsBinding) {
        viewBinding.viewModel = viewModel
    }


    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNavigationEvent -> navigateToToolManagement()
        else -> Unit
    }

    private fun navigateToToolManagement() {
        navigationController?.navigate(R.id.action_toolManagementParamsFragment_to_toolManagementFragment)
    }

}
