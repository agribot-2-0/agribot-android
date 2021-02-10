package eu.agribot.agribotapp.ui.fragment.toolmanagement.displaytoolphoto

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ShotToolPhotoNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentDisplayToolPhotoBinding
import kotlinx.android.synthetic.main.fragment_display_tool_photo.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class DisplayToolPhotoFragment : BaseFragment<DisplayToolPhotoViewModel, FragmentDisplayToolPhotoBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_display_tool_photo
    override val viewModel: DisplayToolPhotoViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentDisplayToolPhotoBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolPhoto()
    }

    private fun setToolPhoto() {
        Glide.with(requireContext())
            .load(viewModel.getToolPhotoDirection())
            .into(imageView4)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ShotToolPhotoNavigationEvent -> handleShotToolPhotoNavigationEvent()
        is ToolNavigationEvent -> handleToolNavigationEvent()
        else -> Unit
    }

    private fun handleShotToolPhotoNavigationEvent() {
        navigationController?.navigate(R.id.action_displayToolPhoto_to_shotToolPhoto)
    }

    private fun handleToolNavigationEvent() {
        navigationController?.navigate(R.id.action_displayToolPhoto_to_toolManagementParamsFragment)
    }

}
