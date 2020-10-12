package eu.agribot.agribotapp.ui.fragment.toolcontrol

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentToolControlBinding
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BACK_DOWN_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BACK_UP_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.FRONT_DOWN_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.FRONT_UP_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TuzSwitchesState
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import kotlinx.android.synthetic.main.fragment_tool_control.back_hitch_recycler
import kotlinx.android.synthetic.main.fragment_tool_control.checkBox_back_tuz_down
import kotlinx.android.synthetic.main.fragment_tool_control.checkBox_back_tuz_up
import kotlinx.android.synthetic.main.fragment_tool_control.checkBox_front_tuz_down
import kotlinx.android.synthetic.main.fragment_tool_control.checkBox_front_tuz_up
import kotlinx.android.synthetic.main.fragment_tool_control.front_hitch_recycler
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToolControlFragment : BaseFragment<ToolControlViewModel, FragmentToolControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_control
    override val viewModel: ToolControlViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentToolControlBinding) {
        viewBinding.viewModel = viewModel
    }

    private val frontHitchAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)
    private val backHitchAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        viewModel.createToolsLists()
        viewModel.frontToolsList.observe(this::addFrontTools)
        viewModel.backToolsList.observe(this::addBackTools)
        viewModel.switchesActive.observe(this::showSwitchesState)
        setCheckboxListeners()
    }

    private fun setCheckboxListeners() {
        checkBox_front_tuz_up.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTuzParametrChanged(FRONT_UP_TUZ, isChecked)
        }
        checkBox_front_tuz_down.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTuzParametrChanged(FRONT_DOWN_TUZ, isChecked)
        }
        checkBox_back_tuz_down.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTuzParametrChanged(BACK_DOWN_TUZ, isChecked)
        }
        checkBox_back_tuz_up.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActivateTuzParametrChanged(BACK_UP_TUZ, isChecked)
        }
    }

    private fun showSwitchesState(tuzSwitchesState: TuzSwitchesState) {
        checkBox_back_tuz_down.isChecked = tuzSwitchesState.rearBottomTuzChecked
        checkBox_front_tuz_up.isChecked = tuzSwitchesState.frontUpTuzChecked
        checkBox_front_tuz_down.isChecked = tuzSwitchesState.frontBottomTuzChecked
        checkBox_back_tuz_up.isChecked = tuzSwitchesState.rearUpTuzChecked
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
}
