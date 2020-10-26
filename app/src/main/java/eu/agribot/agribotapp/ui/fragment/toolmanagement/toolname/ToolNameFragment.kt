package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolname

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementNameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToolNameFragment :
    BaseFragment<ToolNameViewModel, FragmentToolManagementNameBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_creator_task_name
    override val viewModel: ToolNameViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentToolManagementNameBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.toastEvent.observe(this::showToast)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskPathNavigationEvent -> navigateToCreatorPathName()
        else -> Unit
    }

    private fun navigateToCreatorPathName() {
        navigationController?.navigate(R.id.action_taskNameCreatorFragment_to_taskPathCreatorFragment)
    }
}
