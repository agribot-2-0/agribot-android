package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolParamsNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeHelper
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeRecyclerClickListener
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils.SwipeRecyclerHelper
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.ToolListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.ToolListItemViewModel
import kotlinx.android.synthetic.main.fragment_tool_management.tool_recycler
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ToolManagementFragment :
    BaseFragment<ToolManagementViewModel, FragmentToolManagementBinding>() {

    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }
    override val layoutResourceId: Int = R.layout.fragment_tool_management
    override val viewModel: ToolManagementViewModel by viewModel { parametersOf(requireContext()) }
    private var adapter = ToolListItemAdapter(this::onItemClicked)

    override fun bindViewVariables(viewBinding: FragmentToolManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNameNavigationEvent -> navigateToToolName()
        is ToolParamsNavigationEvent -> navigateToToolParams()
        else -> Unit
    }

    private fun onItemClicked(toolListItemViewModel: ToolListItemViewModel) {
        viewModel.onToolSelected(toolListItemViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.toastMessageEvent.observe(this::showToast)
        setupRecycler()
        viewModel.getAvailableTool()
        viewModel.toolList.observe(this::addToToolList)
    }

    private fun setupRecycler() {
        tool_recycler.adapter = adapter
        tool_recycler.layoutManager = LinearLayoutManager(requireActivity())
        tool_recycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        ItemTouchHelper(
            SwipeRecyclerHelper(
                tool_recycler,
                deleteButton = this::deleteButton,
                editButton = this::editButton
            )
        ).attachToRecyclerView(tool_recycler)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.delete_task_button),
            resources.getDimension(R.dimen.text_size_small),
            android.R.color.holo_red_light,
            SwipeRecyclerClickListener(position, onButtonClicked = this::showDeleteTaskDialog)
        )
    }

    private fun addToToolList(task: List<ToolListItemViewModel>) {
        adapter.items = task
    }

    private fun showDeleteTaskDialog(itemPosition: Int) {
        dialogManager.showDeleteTaskDialog(itemPosition, viewModel::onDeleteButtonClicked)
    }

    private fun editButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.edit_task_button),
            resources.getDimension(R.dimen.text_size_small),
            android.R.color.holo_blue_light,
            SwipeRecyclerClickListener(position, onButtonClicked = viewModel::onEditButtonClicked)
        )
    }

    private fun navigateToToolName() {
        navigationController?.navigate(R.id.action_toolManagementFragment_to_toolManagementNameFragment)
    }

    private fun navigateToToolParams() {
        navigationController?.navigate(R.id.action_toolManagementFragment_to_toolManagementParamsFragment)
    }
}
