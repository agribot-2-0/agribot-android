package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentManualControlBinding
import kotlinx.android.synthetic.main.fragment_manual_control.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ManualControlFragment : BaseFragment<ManualControlViewModel, FragmentManualControlBinding>() {

        val streamURI = Uri.parse(
            "rtmp://flashcloud.mediacdn.com:1935/live/kontratv"
        )

    override val layoutResourceId: Int = R.layout.fragment_manual_control
    override val viewModel: ManualControlViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentManualControlBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manual_mode_joystick.setOnMoveListener { angle, strength ->
            viewModel.onJoystickParametrChange(angle, strength)
        }
        viewModel.sendStartManualModeInfo()
        prepareStreamPlayer()
    }

    private fun prepareStreamPlayer() {
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        player.setMediaItem(MediaItem.fromUri(streamURI))
        myPlayerView.player = player
        player.playWhenReady = true;
        player.prepare()

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
