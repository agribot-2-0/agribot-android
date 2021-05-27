package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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

    private val frontHitchAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)
    private val backHitchAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        viewModel.frontToolsList.observe(this::addFrontTools)
        viewModel.backToolsList.observe(this::addBackTools)
        viewModel.switchesActive.observe(this::showSwitchesState)
        viewModel.tplEncoderValue.observe(this::setTplEncoderValue)
        setCheckboxListeners()
    }

    private fun setTplEncoderValue(tool: ToolControlViewModel.ToolEncoderValue) {
        when (tool.toolID) {
            Tools.TplHeight.Front.toolId -> viewBinding.checkBoxFrontTplUp.text = tool.EncoderValue
            Tools.TplAngle.Front.toolId -> viewBinding.checkBoxFrontTplDown.text = tool.EncoderValue
            Tools.TplHeight.Rear.toolId -> viewBinding.checkBoxBackTplUp.text = tool.EncoderValue
            Tools.TplAngle.Rear.toolId -> viewBinding.checkBoxBackTplDown.text = tool.EncoderValue
        }
    }

    private fun setCheckboxListeners() {
        viewBinding.checkBoxFrontTplUp.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplHeight.Front.toolId, isChecked)
        }
        viewBinding.checkBoxFrontTplDown.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplAngle.Front.toolId, isChecked)
        }

        viewBinding.checkBoxBackTplDown.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplHeight.Rear.toolId, isChecked)
        }
        viewBinding.checkBoxBackTplUp.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(Tools.TplAngle.Rear.toolId, isChecked)
        }
    }

    private fun showSwitchesState(tplSwitchesState: TplSwitchesState) {
        viewBinding.checkBoxBackTplDown.isChecked = tplSwitchesState.rearBottomTplChecked
        viewBinding.checkBoxFrontTplUp.isChecked = tplSwitchesState.frontUpTplChecked
        viewBinding.checkBoxBackTplDown.isChecked = tplSwitchesState.frontBottomTplChecked
        viewBinding.checkBoxBackTplUp.isChecked = tplSwitchesState.rearUpTplChecked
    }

    private fun onHitchRecyclerParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        viewModel.onHitchParametersChanged(sliderNumber, sliderValue, isActivated)
    }

    private fun setupRecyclers() {

        viewBinding.frontHitchRecycler.adapter = frontHitchAdapter
        viewBinding.frontHitchRecycler.layoutManager = LinearLayoutManager(requireActivity())
        viewBinding.backHitchRecycler.adapter = backHitchAdapter
        viewBinding.backHitchRecycler.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun addFrontTools(tools: List<HitchListItemViewModel>) {
        frontHitchAdapter.items = tools
    }

    private fun addBackTools(tools: List<HitchListItemViewModel>) {
        backHitchAdapter.items = tools
    }
}
