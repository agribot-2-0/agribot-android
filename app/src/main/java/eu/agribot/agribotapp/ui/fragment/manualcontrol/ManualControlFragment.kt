package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.OverrideNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentManualControlBinding
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.fragment_manual_control.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ManualControlFragment : BaseFragment<ManualControlViewModel, FragmentManualControlBinding>() {

    companion object {
        private const val DELAY_MANUAL_STEERING_START = 500L
    }

    override val layoutResourceId: Int = R.layout.fragment_manual_control
    override val viewModel: ManualControlViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentManualControlBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sendManualSteeringInitRequest()
        delayManualSterring()
        viewModel.startWrongHeartbeatIcon()
        viewModel.setHeartbeatPositiveColor()
        observeLiveData()

        val joystick = view.findViewById(R.id.joyy) as JoystickView
        joystick.setOnMoveListener { angle, strength ->
            viewModel.onJoystickParametrsChanged(angle, strength)
        }
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolControlNavigationEvent -> navigateToToolControl()
        is BluetoothConnectionNavigationEvent -> navigateToBluetoothScreen()
        is OverrideNavigationEvent -> navigateToOverrideMode()
        else -> Unit
    }

    private fun observeLiveData() {
        viewModel.showToastEvent.observe(this::showToast)
        viewModel.heartBeatState.observe(this::setHearBeatWrongColor)
    }

    private fun navigateToBluetoothScreen() {
        navigationController?.navigate(R.id.action_manualControlFragment_to_bluetoothConnectionFragment)
    }

    private fun navigateToToolControl() {
        navigationController?.navigate(R.id.action_manualControlFragment_to_toolControlFragment)
    }

    private fun navigateToOverrideMode() {
        navigationController?.navigate(R.id.action_manualControlFragment_to_overrideFragment)
    }

    private fun delayManualSterring() {
        Handler().postDelayed({
            viewModel.sendManualSteeringStartFrame()
        }, DELAY_MANUAL_STEERING_START)
    }

    private fun setHearBeatWrongColor(heartbeatState: Boolean) {
        if (!heartbeatState) {
            heartbeat_icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
        } else {
            heartbeat_icon.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.red)
            )
        }
    }
}
