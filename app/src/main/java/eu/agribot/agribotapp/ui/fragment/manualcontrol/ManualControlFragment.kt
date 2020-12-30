package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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
import tcking.github.com.giraffeplayer2.Option
import tcking.github.com.giraffeplayer2.VideoInfo
import tv.danmaku.ijk.media.player.IjkMediaPlayer


class ManualControlFragment : BaseFragment<ManualControlViewModel, FragmentManualControlBinding>() {

    private val streamURI: Uri = Uri.parse(
        "rtmp://192.168.0.139/live"
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
        val videoView = video_view
        val myVideoInfo = VideoInfo(streamURI)
        myVideoInfo.addOption(
            Option.create(
                IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                "start-on-prepared",
                1
            )
        )
            .addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer"))
            .addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1))
            .addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max_delay", 0))
            .addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_CODEC, "low_delay", 10000))
            videoView.videoInfo(myVideoInfo)
            Log.e("xd", "video player: " + videoView.videoInfo)
         videoView.setVideoPath(streamURI.toString()).player.start()
       // GiraffePlayer.play(requireContext(), videoInfo)

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
