package eu.agribot.agribotapp.ui.fragment.pathmanagement.pathname

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.NewPathNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentPathManagementNameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PathNameFragment :
    BaseFragment<PathNameViewModel, FragmentPathManagementNameBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_path_management_name

    override val viewModel: PathNameViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentPathManagementNameBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.toastMessageEvent.observe(this::showToast)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) =
        when (navigationEvent) {
            is NewPathNavigationEvent -> handleNewPathNavigationEvent()
            else -> Unit
        }

    private fun handleNewPathNavigationEvent() {
        navigationController?.navigate(R.id.action_pathNameFragment_to_newPathFragment)
    }
}
