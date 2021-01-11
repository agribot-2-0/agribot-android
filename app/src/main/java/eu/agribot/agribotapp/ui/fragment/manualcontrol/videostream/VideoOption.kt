package eu.agribot.agribotapp.ui.fragment.manualcontrol.videostream

import tcking.github.com.giraffeplayer2.Option
import tcking.github.com.giraffeplayer2.VideoInfo
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class VideoOption(private val videoInfo: VideoInfo) {

    fun addVideoOption() {
        videoInfo.addOption(
            Option.create(
                IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                VideoParameters.START_WHEN_READY.parameterName,
                VideoParameters.START_WHEN_READY.parametersValue
            )
        )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                    VideoParameters.NOBUFFER.parameterName,
                    VideoParameters.NOBUFFER.parametersValueName
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                    VideoParameters.FLUSH_PACKETS.parameterName,
                    VideoParameters.FLUSH_PACKETS.parametersValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                    VideoParameters.MAX_DELAY.parameterName,
                    VideoParameters.MAX_DELAY.parametersValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                    VideoParameters.INBUFFOR.parameterName,
                    VideoParameters.INBUFFOR.parametersValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                    VideoParameters.PACKET_BUFFERING.parameterName,
                    VideoParameters.PACKET_BUFFERING.parametersValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                    VideoParameters.FRAME_DROP.parameterName,
                    VideoParameters.FRAME_DROP.parametersValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_CODEC,
                    VideoParameters.SKIP_FRAME.parameterName,
                    VideoParameters.SKIP_FRAME.parametersValue
                )
            )
            .addOption(
                Option.create(
                    IjkMediaPlayer.OPT_CATEGORY_CODEC,
                    VideoParameters.SKIP_LOOP_FILTER.parameterName,
                    VideoParameters.SKIP_LOOP_FILTER.parametersValue
                )
            )
    }
}
