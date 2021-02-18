package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentManualControlBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.ManualControlViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_manual_control.manualControlViewPager
import kotlinx.android.synthetic.main.fragment_manual_control.manual_mode_joystick
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ManualControlFragment : BaseFragment<ManualControlViewModel, FragmentManualControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_manual_control
    override val viewModel: ManualControlViewModel by viewModel { parametersOf(requireContext()) }


    override fun bindViewVariables(viewBinding: FragmentManualControlBinding) {
        viewBinding.viewModel = viewModel
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manual_mode_joystick.setOnMoveListener { angle, strength ->
            viewModel.onJoystickParametrChange(angle, strength)
        }
        setupViewPager()
    }

    private fun setupViewPager() {
        manualControlViewPager.adapter = ManualControlViewPagerAdapter(this)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolControlNavigationEvent -> navigateToToolControl()
        is BluetoothConnectionNavigationEvent -> navigateToBluetoothScreen()
        is TTControlNavigationEvent -> navigateToOverrideMode()
        else -> Unit
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
}
