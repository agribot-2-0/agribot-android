package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol

import android.os.Bundle
import android.util.Log
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder.Tools
import eu.agribot.agribotapp.databinding.FragmentToolControlBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TplSwitchesState
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        viewModel.toolsList.observe(this::setTools)
        viewModel.switchesActive.observe(this::showSwitchesState)
        viewModel.tplEncoderValue.observe(this::setTplEncoderValue)
        setCheckboxListeners()
    }

    private fun setTplEncoderValue(tool: ToolControlViewModel.ToolEncoderValue) {
        when (tool.toolID) {
            Tools.TplHeight.Front.toolId -> viewBinding.leftUpCheckbox.text = tool.EncoderValue
            Tools.TplAngle.Front.toolId -> viewBinding.leftDownCheckbox.text = tool.EncoderValue
            Tools.TplHeight.Rear.toolId -> viewBinding.rightUpCheckbox.text = tool.EncoderValue
            Tools.TplAngle.Rear.toolId -> viewBinding.rightDownCheckbox.text = tool.EncoderValue
        }
    }

    private fun setCheckboxListeners() {
        viewBinding.leftUpCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplHeight.Front.toolId, isChecked)
        }
        viewBinding.leftDownCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplAngle.Front.toolId, isChecked)
        }

        viewBinding.rightUpCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplHeight.Rear.toolId, isChecked)
        }
        viewBinding.rightDownCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplAngle.Rear.toolId, isChecked)
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

}
