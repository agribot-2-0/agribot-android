package eu.agribot.agribotapp.ui.fragment.toolcontrol

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentToolControlBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
<<<<<<< HEAD
=======
import timber.log.Timber
>>>>>>> 0bd8dbd... static code analyze fix

class ToolControlFragment : BaseFragment<ToolControlViewModel, FragmentToolControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_control
    override val viewModel: ToolControlViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentToolControlBinding) {
        viewBinding.viewModel = viewModel
    }
<<<<<<< HEAD
=======

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
>>>>>>> 0bd8dbd... static code analyze fix
}
