package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentToolControlBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BACK_DOWN_TPL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BACK_UP_TPL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.FRONT_DOWN_TPL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.FRONT_UP_TPL
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TplSwitchesState
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import kotlinx.android.synthetic.main.fragment_tool_control.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@ExperimentalCoroutinesApi
class ToolControlFragment : BaseFragment<ToolControlViewModel, FragmentToolControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_control
    @ExperimentalCoroutinesApi
    override val viewModel: ToolControlViewModel by viewModel{ parametersOf(context)}
    private var toolFeedbackSnackbar: Snackbar? = null
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
        viewModel.snackBarEvent.observe(this::showToolFeedbackSnackbar)
        setCheckboxListeners()
    }

    private fun setCheckboxListeners() {
        checkBox_front_tpl_up.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(FRONT_UP_TPL, isChecked)
        }
        checkBox_front_tpl_down.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(FRONT_DOWN_TPL, isChecked)
        }
        checkBox_back_tpl_down.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(BACK_DOWN_TPL, isChecked)
        }
        checkBox_back_tpl_up.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTplParametrChanged(BACK_UP_TPL, isChecked)
        }
    }

    private fun showSwitchesState(tplSwitchesState: TplSwitchesState) {
        checkBox_back_tpl_down.isChecked = tplSwitchesState.rearBottomTplChecked
        checkBox_front_tpl_up.isChecked = tplSwitchesState.frontUpTplChecked
        checkBox_front_tpl_down.isChecked = tplSwitchesState.frontBottomTplChecked
        checkBox_back_tpl_up.isChecked = tplSwitchesState.rearUpTplChecked
    }

    private fun onHitchRecyclerParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        viewModel.onHitchParametersChanged(sliderNumber, sliderValue, isActivated)
    }

    private fun setupRecyclers() {
        front_hitch_recycler.adapter = frontHitchAdapter
        front_hitch_recycler.layoutManager = LinearLayoutManager(requireActivity())

        back_hitch_recycler.adapter = backHitchAdapter
        back_hitch_recycler.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun addFrontTools(tools: List<HitchListItemViewModel>) {
        frontHitchAdapter.items = tools
    }

    private fun addBackTools(tools: List<HitchListItemViewModel>) {
        backHitchAdapter.items = tools
    }

    private fun showToolFeedbackSnackbar(toolFeedbackText: String) {
        if (toolFeedbackSnackbar != null) {
            toolFeedbackSnackbar = null
        }
        view?.let {
            toolFeedbackSnackbar = Snackbar.make(it, toolFeedbackText, Snackbar.LENGTH_SHORT)
        }
        toolFeedbackSnackbar?.show()
    }
}
