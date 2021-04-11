package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorToolSelectBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect.spinner.SpinnerSelectedItemAdapter
import kotlinx.android.synthetic.main.fragment_creator_tool_select.back_tool_mode
import kotlinx.android.synthetic.main.fragment_creator_tool_select.back_tool_name
import kotlinx.android.synthetic.main.fragment_creator_tool_select.front_tool_mode
import kotlinx.android.synthetic.main.fragment_creator_tool_select.front_tool_name
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CreatorToolSelectFragment :
    BaseFragment<CreatorToolSelectViewModel, FragmentCreatorToolSelectBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_creator_tool_select
    override val viewModel: CreatorToolSelectViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentCreatorToolSelectBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //right_button.setOnClickListener { viewModel.sendTaskData() }
        viewModel.getAvailableTool()
        viewModel.mToolNames.observe(this::createToolNameSpinner)
        viewModel.mToolModes.observe(this::createToolModeSpinner)
    }

    private fun createToolNameSpinner(tool: List<String>) {
        val spinnerArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            tool
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        front_tool_name.adapter = spinnerArrayAdapter
        front_tool_name.onItemSelectedListener =
            SpinnerSelectedItemAdapter(
                this::onFrontToolNameSelected
            )
        if (viewModel.mToolNames.value!!.contains(viewModel.taskCreatorDataManager.getTaskParemeters().frontToolName)) {
            front_tool_name.setSelection(viewModel.mToolNames.value!!.indexOf(viewModel.taskCreatorDataManager.getTaskParemeters().frontToolName))
        }

        back_tool_name.adapter = spinnerArrayAdapter
        back_tool_name.onItemSelectedListener =
            SpinnerSelectedItemAdapter(
                this::onBackToolNameSelected
            )
        if (viewModel.mToolNames.value!!.contains(viewModel.taskCreatorDataManager.getTaskParemeters().backToolName)) {
            back_tool_name.setSelection(viewModel.mToolNames.value!!.indexOf(viewModel.taskCreatorDataManager.getTaskParemeters().backToolName))
        }
    }

    private fun createToolModeSpinner(tool: List<String>) {
        val spinnerArrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            tool
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        front_tool_mode.adapter = spinnerArrayAdapter
        front_tool_mode.onItemSelectedListener =
            SpinnerSelectedItemAdapter(
                this::onFrontToolModeSelected
            )
        front_tool_mode.setSelection(viewModel.frontToolMode)

        back_tool_mode.adapter = spinnerArrayAdapter
        back_tool_mode.onItemSelectedListener =
            SpinnerSelectedItemAdapter(
                this::onBackToolModeSelected
            )
        back_tool_mode.setSelection(viewModel.backToolMode)
    }

    private fun onFrontToolNameSelected(position: Int) {
        viewModel.frontToolName = front_tool_name.getItemAtPosition(position).toString()
    }

    private fun onBackToolNameSelected(position: Int) {
        viewModel.backToolName = back_tool_name.getItemAtPosition(position).toString()
    }

    private fun onFrontToolModeSelected(position: Int) {
        viewModel.frontToolMode = position
    }

    private fun onBackToolModeSelected(position: Int) {
        viewModel.backToolMode = position
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is DashboardNavigationEvent -> navigateToDashboard()
        else -> Unit
    }

    private fun navigateToDashboard() {
        navigationController?.navigate(R.id.action_taskCreatorTaskToolFragment_to_creatorFragment)
    }
}
