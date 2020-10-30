package eu.agribot.agribotapp.ui.fragment.taskcreator.taskname

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskPathNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorTaskNameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CreatorTaskNameFragment :
    BaseFragment<CreatorTaskNameViewModel, FragmentCreatorTaskNameBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_creator_task_name
    override val viewModel: CreatorTaskNameViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentCreatorTaskNameBinding) {
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
