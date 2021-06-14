package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder.Tools
import eu.agribot.agribotapp.databinding.FragmentToolControlBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.TplSwitchesState
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemAdapter
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@ExperimentalCoroutinesApi
class ToolControlFragment : BaseFragment<ToolControlViewModel, FragmentToolControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_control

    @ExperimentalCoroutinesApi
    override val viewModel: ToolControlViewModel by viewModel { parametersOf(context) }
    override fun bindViewVariables(viewBinding: FragmentToolControlBinding) {
        viewBinding.viewModel = viewModel
    }

    private val hitchAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        viewModel.toolsList.observe(this::setTools)
        viewModel.switchesActive.observe(this::showSwitchesState)
        viewModel.tplEncoderValue.observe(this::setTplEncoderValue)
        setCheckboxListeners()
        setSeekbarListener()
    }

    private fun setSeekbarListener() {
        viewBinding.mainProgressBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                setSeekbarText(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // not used
            }
        })
    }

    private fun setSeekbarText(progress: Int) {
        viewBinding.seekbarText.text = viewModel.getSeekBarText(progress)
    }

    private fun setTplEncoderValue(tool: ToolControlViewModel.ToolEncoderValue) {
        when (tool.toolID) {
            Tools.TplHeight.Front.toolId -> viewBinding.leftUpCheckbox.text = tool.EncoderValue
            Tools.TplAngle.Front.toolId -> viewBinding.leftDownCheckbox.text = tool.EncoderValue
            Tools.TplHeight.Rear.toolId -> viewBinding.rightUpCheckbox.text = tool.EncoderValue
            Tools.TplAngle.Rear.toolId -> viewBinding.rightDownCheckbox.text = tool.EncoderValue
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCheckboxListeners() {
        viewBinding.leftUpCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplHeight.Front.toolId, isChecked)
            if (isChecked) {
                setMInMaxSeekBar(viewBinding.leftUpCheckbox)
            }

        }
        viewBinding.leftDownCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplAngle.Front.toolId, isChecked)
            if (isChecked) {
                setMInMaxSeekBar(viewBinding.leftDownCheckbox)
            }
        }

        viewBinding.rightUpCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplHeight.Rear.toolId, isChecked)
            if (isChecked) {
                setMInMaxSeekBar(viewBinding.rightUpCheckbox)
            }
        }
        viewBinding.rightDownCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplAngle.Rear.toolId, isChecked)
            if (isChecked) {
                setMInMaxSeekBar(viewBinding.rightDownCheckbox)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMInMaxSeekBar(view: View?) {
        when (view) {
            viewBinding.leftUpCheckbox -> {
                viewBinding.mainProgressBar.min = MIN_HEIGHT
                viewBinding.mainProgressBar.max = MAX_HEIGHT
                viewBinding.txtvProgressMin.text = MIN_HEIGHT.toString() + "m"
                viewBinding.txtvProgressMax.text = MAX_HEIGHT.toString() + "m"

            }
            viewBinding.leftDownCheckbox -> {
                viewBinding.mainProgressBar.min = MIN_ANGLE
                viewBinding.mainProgressBar.max = MAX_ANGLE
                viewBinding.txtvProgressMin.text = MIN_ANGLE.toString() + "rad"
                viewBinding.txtvProgressMax.text = MAX_ANGLE.toString() + "rad"
            }
            viewBinding.rightUpCheckbox -> {
                viewBinding.mainProgressBar.min = MIN_HEIGHT
                viewBinding.mainProgressBar.max = MAX_HEIGHT
                viewBinding.txtvProgressMin.text = MIN_HEIGHT.toString() + "m"
                viewBinding.txtvProgressMax.text = MAX_HEIGHT.toString() + "m"
            }

            viewBinding.rightDownCheckbox -> {
                viewBinding.mainProgressBar.min = MIN_ANGLE
                viewBinding.mainProgressBar.max = MAX_ANGLE
                viewBinding.txtvProgressMin.text = MIN_ANGLE.toString() + "rad"
                viewBinding.txtvProgressMax.text = MAX_ANGLE.toString() + "rad"
            }
        }
    }

    private fun showSwitchesState(tplSwitchesState: TplSwitchesState) {
        viewBinding.rightDownCheckbox.isChecked = tplSwitchesState.rearBottomTplChecked
        viewBinding.leftUpCheckbox.isChecked = tplSwitchesState.frontUpTplChecked
        viewBinding.leftDownCheckbox.isChecked = tplSwitchesState.frontBottomTplChecked
        viewBinding.rightUpCheckbox.isChecked = tplSwitchesState.rearUpTplChecked
    }

    private fun onHitchRecyclerParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        viewModel.onHitchParametersChanged(sliderNumber, sliderValue, isActivated)
    }

    private fun setupRecyclers() {
        viewBinding.toolHitchRecycler.adapter = hitchAdapter
    }

    private fun setTools(tools: List<HitchListItemViewModel>) {
        hitchAdapter.items = tools
    }

    companion object {
        const val MIN_HEIGHT = 0
        const val MAX_HEIGHT = 45
        const val MIN_ANGLE = -26
        const val MAX_ANGLE = 52
    }
}
