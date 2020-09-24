package eu.agribot.agribotapp.ui.fragment.toolcontrol

import android.view.View
import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion

object SeekBarBindings {
    @BindingConversion
    @JvmStatic
    fun convertBooleanToViewVisibility(isVisible: Boolean): Int {
// Working
        return if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("onSeekBarProgressChange")
    @JvmStatic
    fun setOnProgrssChange(seekBar: SeekBar, onProgressChange: (progress: Int) -> Unit) {
        onProgressChange(seekBar.progress)
    }

    @JvmStatic
    fun toTestString(input: Any): String {
        return "Test"
    }
}
