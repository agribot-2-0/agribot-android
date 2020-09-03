package eu.agribot.agribotapp.ui.fragment.toolcontrol

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
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
/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showToastEvent.observe(this::showToast)
        viewModel.setDefaultToolsValue()

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

        left_pto_slider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.e("bar ")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Timber.e("bar ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onSliderParametrChange(1, seekBar.progress)
            }
        })

        left_blue_slider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.e("bar ")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Timber.e("bar ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onSliderParametrChange(2, seekBar.progress)
            }
        })

        left_yellow_slider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.e("bar ")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Timber.e("bar ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onSliderParametrChange(3, seekBar.progress)
            }
        })

        left_green_slider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.e("bar ")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Timber.e("bar ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onSliderParametrChange(4, seekBar.progress)
            }
        })

        right_pto_slider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.e("bar ")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Timber.e("bar ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onSliderParametrChange(5, seekBar.progress)
            }
        })

        right_blue_slider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.e("bar ")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Timber.e("bar ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onSliderParametrChange(6, seekBar.progress)
            }
        })

        right_yellow_slider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.e("bar ")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Timber.e("bar ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onSliderParametrChange(7, seekBar.progress)
            }
        })

        right_green_slider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.e("bar ")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Timber.e("bar ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onSliderParametrChange(8, seekBar.progress)
            }
        })

        left_pto_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.leftPTOSwitch = isChecked
        }

        left_blue_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.leftBlueSwitch = isChecked
        }

        left_yellow_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.leftYellowSwitch = isChecked
        }

        left_green_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.leftGreenSwitch = isChecked
        }

        right_pto_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.rightPTOSwitch = isChecked
        }

        right_blue_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.rightBlueSwitch = isChecked
        }

        right_yellow_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.rightYellowSwitch = isChecked
        }

        right_green_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.rightGreenSwitch = isChecked
        }
    }

 */
}
