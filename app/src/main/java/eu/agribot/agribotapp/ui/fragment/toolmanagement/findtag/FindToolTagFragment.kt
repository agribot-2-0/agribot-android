package eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ShotToolPhotoNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentFindToolTagBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FindToolTagFragment :
    BaseFragment<FindToolTagViewModel, FragmentFindToolTagBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_find_tool_tag
    override val viewModel: FindToolTagViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentFindToolTagBinding) {
        viewBinding.viewModel = viewModel
    }


    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ShotToolPhotoNavigationEvent -> navigateToShotPhoto()
        else -> Unit
    }

    private fun navigateToShotPhoto() {
        navigationController?.navigate(R.id.action_findToolFragment_to_shotToolPhoto)
    }

}
