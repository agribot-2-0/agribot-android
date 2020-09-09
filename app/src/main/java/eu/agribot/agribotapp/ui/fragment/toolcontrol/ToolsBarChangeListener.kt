package eu.agribot.agribotapp.ui.fragment.toolcontrol

import android.widget.SeekBar

class ToolsBarChangeListener(
    val onProgressChange: (actualProgress: Int) -> Unit,
    val onProgressChanged: (progress: Int) -> Unit
) :
    SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onProgressChange(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        onProgressChanged(seekBar.progress)
    }
}
