package eu.agribot.agribotapp.ui.fragment.manualcontrol.videostream

enum class VideoParameters(
    val parameterName: String,
    val parametersValueName: String,
    val parametersValue: Long
) {
    START_WHEN_READY("start-on-prepared", "", 1),

    NOBUFFER("fflags", "nobuffer", 0),

    FLUSH_PACKETS("flush_packets", "", 1),

    MAX_DELAY("max_delay", "", 0),

    INBUFFOR("infbuf", "", 1),

    PACKET_BUFFERING("packet-buffering", "", 0),

    FRAME_DROP("framedrop", "", 5),

    SKIP_FRAME("skip_frame", "", 10),

    SKIP_LOOP_FILTER("skip_loop_filter", "", 48)
}
