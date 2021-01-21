package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.net.Uri
import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentManualControlBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.videostream.VideoParameters
import kotlinx.android.synthetic.main.fragment_manual_control.manual_mode_joystick
import kotlinx.android.synthetic.main.fragment_manual_control.video_view
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tcking.github.com.giraffeplayer2.Option
import tcking.github.com.giraffeplayer2.VideoInfo
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class ManualControlFragment : BaseFragment<ManualControlViewModel, FragmentManualControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_manual_control
    override val viewModel: ManualControlViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentManualControlBinding) {
        viewBinding.viewModel = viewModel
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setDiagnosticData()
        manual_mode_joystick.setOnMoveListener { angle, strength ->
            viewModel.onJoystickParametrChange(angle, strength)
        }
        playVideoStream(createVideoInfo())
    }

    private fun createVideoInfo(): VideoInfo {
        val myVideoInfo = VideoInfo(Uri.parse(viewModel.getStreamUrl()))
        myVideoInfo.addOption(
            Option.create(
                IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                VideoParameters.START_WHEN_READY.parameterName,
                VideoParameters.START_WHEN_READY.parameterValue
            )
        )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                    VideoParameters.NOBUFFER.parameterName,
                    VideoParameters.NOBUFFER.parameterValueName
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                    VideoParameters.FLUSH_PACKETS.parameterName,
                    VideoParameters.FLUSH_PACKETS.parameterValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                    VideoParameters.MAX_DELAY.parameterName,
                    VideoParameters.MAX_DELAY.parameterValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                    VideoParameters.INBUFFOR.parameterName,
                    VideoParameters.INBUFFOR.parameterValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                    VideoParameters.PACKET_BUFFERING.parameterName,
                    VideoParameters.PACKET_BUFFERING.parameterValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                    VideoParameters.FRAME_DROP.parameterName,
                    VideoParameters.FRAME_DROP.parameterValue
                )
            )

            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_CODEC,
                    VideoParameters.SKIP_FRAME.parameterName,
                    VideoParameters.SKIP_FRAME.parameterValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_CODEC,
                    VideoParameters.SKIP_LOOP_FILTER.parameterName,
                    VideoParameters.SKIP_LOOP_FILTER.parameterValue
                )
            )

        return myVideoInfo
    }

    private fun playVideoStream(videoInfo: VideoInfo) {
        video_view.videoInfo(videoInfo)
        video_view.setVideoPath(Uri.parse(viewModel.getStreamUrl()).toString()).player.start()
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
