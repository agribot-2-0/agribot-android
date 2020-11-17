package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorToolSelectBinding
import kotlinx.android.synthetic.main.fragment_creator_tool_select.front_tool_name
import kotlinx.android.synthetic.main.view_bottom_creator_bar.right_button
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CreatorToolSelectFragment :
    BaseFragment<CreatorToolSelectViewModel, FragmentCreatorToolSelectBinding>(){

    override val layoutResourceId: Int = R.layout.fragment_creator_tool_select
    override val viewModel: CreatorToolSelectViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentCreatorToolSelectBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        right_button.setOnClickListener { viewModel.sendTaskData() }
        viewModel.getAvailableTool()

        val itemsAdapter =
            ArrayAdapter(view.context, android.R.layout.simple_spinner_item, viewModel.mToolNames)
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        front_tool_name.adapter = itemsAdapter
        Log.w("CreatorToolSelect", "start")
        front_tool_name.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                Log.w("CreatorToolSelect", "spinner selected")
                Log.w("CreatorToolSelect", position.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
        Log.w("CreatorToolSelect", "end")
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is DashboardNavigationEvent -> navigateToDashboard()
        else -> Unit
    }

    private fun navigateToDashboard() {
        navigationController?.navigate(R.id.action_taskCreatorTaskToolFragment_to_creatorFragment)
    }
}
