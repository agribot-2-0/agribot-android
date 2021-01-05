package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementParamsBinding
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import kotlinx.android.synthetic.main.fragment_tool_management_params.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ToolParamsFragment :
    BaseFragment<ToolParamsViewModel, FragmentToolManagementParamsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_management_params
    override val viewModel: ToolParamsViewModel by viewModel { parametersOf(requireContext()) }

    private val hitchRecyclerAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO add tags scaning via bluetooth
        super.onViewCreated(view, savedInstanceState)
        viewModel.createToolsList()
        viewModel.paramsList.observe(this::addParams)
        setupRecyclers()
        viewModel.isScanOperation.observe(this::getButtonIdNumber)
    }

    private fun getButtonIdNumber(isScanOperation: Boolean) {
        if (isScanOperation)
            when (antennaGroup.checkedRadioButtonId) {
                frontAntenna.id -> {
                    viewModel.scanRfid(AntennaNumber.FRONT_ANTENNA.number)
                }
                backAntenna.id -> {
                    viewModel.scanRfid(AntennaNumber.BACK_ANTENNA.number)
                }
                bothAntenna.id -> {
                    viewModel.scanRfid(AntennaNumber.BOTH_ANTENNAS.number)
                }
                else -> showToast(getString(R.string.no_antenna_selected))
            }
    }

    private fun setupRecyclers() {
        params_recycler.adapter = hitchRecyclerAdapter
        params_recycler.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun onHitchRecyclerParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        viewModel.onHitchParametersChanged(sliderNumber, sliderValue, isActivated)
    }

    private fun addParams(tools: List<HitchListItemViewModel>) {
        hitchRecyclerAdapter.items = tools
    }

    override fun bindViewVariables(viewBinding: FragmentToolManagementParamsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNavigationEvent -> navigateToToolManagement()
        else -> Unit
    }

    private fun navigateToToolManagement() {
        navigationController?.navigate(R.id.action_toolManagementParamsFragment_to_toolManagementFragment)
    }
}
