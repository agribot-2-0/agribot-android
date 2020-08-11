package eu.agribot.agribotapp.ui.fragment.toolcontrol

import android.os.Bundle
import android.view.View
import com.warkiz.tickseekbar.OnSeekChangeListener
import com.warkiz.tickseekbar.SeekParams
import com.warkiz.tickseekbar.TickSeekBar
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentToolControlBinding
import kotlinx.android.synthetic.main.fragment_tool_control.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ToolControlFragment : BaseFragment<ToolControlViewModel, FragmentToolControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_control
    override val viewModel: ToolControlViewModel by viewModel()

    companion object {
        const val LEFT_UP_TUZ = 6
        const val LEFT_DOWN_TUZ = 5
        const val RIGHT_UP_TUZ = 12
        const val RIGHT_DOWN_TUZ = 11
    }

    override fun bindViewVariables(viewBinding: FragmentToolControlBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showToastEvent.observe(this::showToast)

        listener1.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
            }

            override fun onStartTrackingTouch(seekBar: TickSeekBar?) {
                Timber.e("on start")
            }

            override fun onStopTrackingTouch(seekBar: TickSeekBar) {
                viewModel.setSwitchesStatus(LEFT_UP_TUZ, seekBar.progress, true)
            }
        }

        listener2.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
            }

            override fun onStartTrackingTouch(seekBar: TickSeekBar?) {
                Timber.e("on start")
            }

            override fun onStopTrackingTouch(seekBar: TickSeekBar) {
                viewModel.setSwitchesStatus(LEFT_DOWN_TUZ, seekBar.progress, true)
            }
        }

        listener3.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
            }

            override fun onStartTrackingTouch(seekBar: TickSeekBar?) {
                Timber.e("on start")
            }

            override fun onStopTrackingTouch(seekBar: TickSeekBar) {
                viewModel.setSwitchesStatus(RIGHT_UP_TUZ, seekBar.progress, true)
            }
        }

        listener4.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
            }

            override fun onStartTrackingTouch(seekBar: TickSeekBar?) {
                Timber.e("on start")
            }

            override fun onStopTrackingTouch(seekBar: TickSeekBar) {
                viewModel.setSwitchesStatus(RIGHT_DOWN_TUZ, seekBar.progress, true)
            }
        }
    }
}
