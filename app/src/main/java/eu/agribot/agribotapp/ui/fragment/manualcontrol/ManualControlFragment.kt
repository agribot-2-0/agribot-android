package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.os.Bundle
import android.os.Handler
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentManualControlBinding
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.fragment_manual_control.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ManualControlFragment : BaseFragment<ManualControlViewModel, FragmentManualControlBinding>() {

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

        val joystick = view.findViewById(R.id.manual_control_joystick) as JoystickView
        joystick.setOnMoveListener { angle, strength ->
            viewModel.onJoystickParametrsChanged(angle, strength)
        }
<<<<<<< HEAD

        viewModel.heartBeatState.observe(this::setHearBeatWrongColor)
=======
>>>>>>> 0bd8dbd... static code analyze fix
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolControlNavigationEvent -> navigateToToolControl()
        is BluetoothConnectionNavigationEvent -> navigateToBluetoothScreen()
        else -> Unit
    }

    private fun navigateToBluetoothScreen() {
        navigationController?.navigate(R.id.action_manualControlFragment_to_bluetoothConnectionFragment)
    }

    private fun navigateToToolControl() {
        navigationController?.navigate(R.id.action_manualControlFragment_to_toolControlFragment)
    }

<<<<<<< HEAD
=======
    private fun navigateToOverrideMode() {
        navigationController?.navigate(R.id.action_manualControlFragment_to_overrideFragment)
    }

>>>>>>> 0bd8dbd... static code analyze fix
    private fun delayManualSterring() {
        Handler().postDelayed({
            viewModel.sendManualSteeringStartFrame()
        }, 500)
    }

    fun setHearBeatWrongColor(heartbeatState: Boolean) {
        if (heartbeatState) {
            heartbeatTEXT.setBackgroundColor(resources.getColor(R.color.red))
        } else {
            heartbeatTEXT.setBackgroundColor(resources.getColor(R.color.base_green))
        }
    }
}
