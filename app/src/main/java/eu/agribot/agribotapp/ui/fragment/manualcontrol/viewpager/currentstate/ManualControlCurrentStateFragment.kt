package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.currentstate

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.PagerManualControlCurrentStateBinding
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateAdapter
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar.ManualActionsAdapter
import eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar.ManualActionsViewModel
import eu.agribot.agribotapp.ui.fragment.manualcontrol.videostream.VideoParameters
import kotlinx.android.synthetic.main.pager_manual_control_current_state.diagnosticDataRecycler
import kotlinx.android.synthetic.main.pager_manual_control_current_state.manualModeRecyclerActions
import kotlinx.android.synthetic.main.pager_manual_control_current_state.video_view
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tcking.github.com.giraffeplayer2.Option
import tcking.github.com.giraffeplayer2.VideoInfo
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class ManualControlCurrentStateFragment :
    BaseFragment<ManualControlCurrentStateViewModel, PagerManualControlCurrentStateBinding>() {

    override val layoutResourceId: Int = R.layout.pager_manual_control_current_state
    override val viewModel: ManualControlCurrentStateViewModel by viewModel {
        parametersOf(
            requireContext()
        )
    }
    private val actionsRecyclerAdapter =
        ManualActionsAdapter(onClickSelectedItem = this::onSelectedActionClicked)
    private val diagnosticDataRecyclerAdapter = RobotStateAdapter()

    override fun bindViewVariables(viewBinding: PagerManualControlCurrentStateBinding) {
        viewBinding.viewModel = viewModel
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewModel.prepareDiagnosticData()
        observeViewModel()
        viewModel.getDiagnosticData()
        viewModel.getUserRole()
        playVideoStream(createVideoInfo())
    }

    private fun setupRecycler() {
        viewModel.getUserRole()
        manualModeRecyclerActions.adapter = actionsRecyclerAdapter
        manualModeRecyclerActions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        diagnosticDataRecycler.adapter = diagnosticDataRecyclerAdapter
        diagnosticDataRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun onSelectedActionClicked(actionID: Int) {
        viewModel.onActionClicked(actionID)
    }

    private fun addDiagnosticData(diagnosticData: List<RobotStateViewModel>) {
        diagnosticDataRecyclerAdapter.items = diagnosticData
    }

    private fun addActions(actions: List<ManualActionsViewModel>) {
        actionsRecyclerAdapter.items = actions
    }

    private fun observeViewModel() {
        viewModel.toastMessage.observe(this::showToast)
        viewModel.actionsList.observe(this::addActions)
        viewModel.diagnosticDataList.observe(this::addDiagnosticData)
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
}
