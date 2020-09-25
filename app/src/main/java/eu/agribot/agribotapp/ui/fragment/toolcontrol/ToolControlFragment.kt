package eu.agribot.agribotapp.ui.fragment.toolcontrol

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentToolControlBinding
import eu.agribot.agribotapp.domain.model.TuzSwitchModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.BACK_DOWN_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.BACK_UP_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.FRONT_DOWN_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.FRONT_UP_TUZ
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
        HitchListItemAdapter(this::onFrontHitchRecyclerParametersChanged)
    private val backHitchAdapter =
        HitchListItemAdapter(this::onBackHitchRecyclerParametersChanged)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        viewModel.createToolsLists()
        viewModel.frontToolsList.observe(this::addFrontTools)
        viewModel.backToolsList.observe(this::addBackTools)
        setCheckboxListeners()
    }

    private fun setCheckboxListeners() {
        checkBox_front_tuz_up.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox_front_tuz_down.isChecked = false
                checkBox_back_tuz_down.isChecked = false
                checkBox_back_tuz_up.isChecked = false
                viewModel.onActivateTuzParametrChanged(TuzSwitchModel(FRONT_UP_TUZ, true))
            } else {
                viewModel.onActivateTuzParametrChanged(TuzSwitchModel(FRONT_UP_TUZ, false))
            }
        }
        checkBox_front_tuz_down.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox_front_tuz_up.isChecked = false
                checkBox_back_tuz_down.isChecked = false
                checkBox_back_tuz_up.isChecked = false
                viewModel.onActivateTuzParametrChanged(TuzSwitchModel(FRONT_DOWN_TUZ, true))
            } else {
                viewModel.onActivateTuzParametrChanged(TuzSwitchModel(FRONT_DOWN_TUZ, false))
            }
        }
        checkBox_back_tuz_down.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox_back_tuz_up.isChecked = false
                checkBox_front_tuz_down.isChecked = false
                checkBox_front_tuz_up.isChecked = false
                viewModel.onActivateTuzParametrChanged(TuzSwitchModel(BACK_DOWN_TUZ, true))
            } else {
                viewModel.onActivateTuzParametrChanged(TuzSwitchModel(BACK_DOWN_TUZ, false))
            }
        }
        checkBox_back_tuz_up.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox_front_tuz_up.isChecked = false
                checkBox_back_tuz_down.isChecked = false
                checkBox_front_tuz_down.isChecked = false
                viewModel.onActivateTuzParametrChanged(TuzSwitchModel(BACK_UP_TUZ, true))
            } else {
                viewModel.onActivateTuzParametrChanged(TuzSwitchModel(BACK_UP_TUZ, false))
            }
        }
    }

    private fun onFrontHitchRecyclerParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        viewModel.onHitchParametersChanged(sliderNumber, sliderValue, isActivated)
    }

    private fun onBackHitchRecyclerParametersChanged(
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
