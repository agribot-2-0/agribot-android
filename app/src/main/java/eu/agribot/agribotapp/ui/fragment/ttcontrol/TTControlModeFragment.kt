package eu.agribot.agribotapp.ui.fragment.ttcontrol

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.TTControlToolNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentTtcontrolBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TTControlModeFragment : BaseFragment<TTControlViewModel, FragmentTtcontrolBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_ttcontrol
    override val viewModel: TTControlViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentTtcontrolBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setDefaultCartState()
        viewModel.frontCart.observe(this::onFrontKartChanged)
        viewModel.rearCart.observe(this::onRearKartChanged)
        viewBinding.horizontalJoystick.setOnMoveListener { angle, _ ->
            viewModel.onHorizontalJoystickParametersChanged(angle)
        }
        viewBinding.verticalJoystick.setOnMoveListener { angle, _ ->
            viewModel.onVerticalJoystickParametersChanged(angle)
        }
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is TTControlToolNavigationEvent -> navigateToToolControl()
        else -> Unit
    }

    private fun navigateToToolControl() {
        navigationController?.navigate(
            R.id.action_overrideMode_to_overrideToolControl
        )
    }

    private fun onFrontKartChanged(isActive: Boolean) {
        if (isActive) {
            viewBinding.frontKart.setColorFilter(ContextCompat.getColor(requireContext(), R.color.base_green))
        } else {
            viewBinding.frontKart.colorFilter = null
        }
    }

    private fun onRearKartChanged(isActive: Boolean) {
        if (isActive) {
            viewBinding.rearKart.setColorFilter(ContextCompat.getColor(requireContext(), R.color.base_green))
        } else {
            viewBinding.rearKart.colorFilter = null
        }
    }
}
