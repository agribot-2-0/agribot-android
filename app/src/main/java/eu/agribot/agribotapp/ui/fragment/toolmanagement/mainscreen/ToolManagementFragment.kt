package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolManagementSummaryNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementBinding
import eu.agribot.agribotapp.ui.fragment.swiperecyclerutils.SwipeHelper
import eu.agribot.agribotapp.ui.fragment.swiperecyclerutils.SwipeRecyclerClickListener
import eu.agribot.agribotapp.ui.fragment.swiperecyclerutils.SwipeRecyclerHelper
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toollist.ToolListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toollist.ToolListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters.ListToolParamAdapter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters.ListToolParamViewModel
import kotlinx.android.synthetic.main.fragment_tool_management.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class ToolManagementFragment :
    BaseFragment<ToolManagementViewModel, FragmentToolManagementBinding>() {

    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }
    override val layoutResourceId: Int = R.layout.fragment_tool_management
    override val viewModel: ToolManagementViewModel by viewModel { parametersOf(requireContext()) }
    private var toolListAdapter = ToolListItemAdapter(this::onItemClicked)
    private var toolParamAdapter = ListToolParamAdapter()

    override fun bindViewVariables(viewBinding: FragmentToolManagementBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNameNavigationEvent -> navigateToToolName()
        is ToolManagementSummaryNavigationEvent -> handleToolManagementSummaryNavigationEvent()
        else -> Unit
    }

    private fun onItemClicked(toolListItemViewModel: ToolListItemViewModel) {
        viewModel.onToolSelected(toolListItemViewModel)
    }

    private fun setToolPhoto(photoPath: String) {
        Glide.with(requireContext())
            .load(getOutputDirectory().toString() + photoPath)
            .into(main_tool_photo)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.toastMessageEvent.observe(this::showToast)
        setupRecycler()
        viewModel.getAvailableTool()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.toolList.observe(this::addToToolList)
        viewModel.listToolParamList.observe(this::addToolParamItem)
        viewModel.setPhotoEvent.observe(this::setToolPhoto)
    }

    private fun addToolParamItem(listToolParamItem: List<ListToolParamViewModel>) {
        toolParamAdapter.items = listToolParamItem
    }

    private fun setupRecycler() {
        setupToolsNameRecycler()
        setupToolParamRecycler()
    }

    private fun setupToolsNameRecycler() {
        main_tool_name_recycler.adapter = toolListAdapter
        main_tool_name_recycler.layoutManager = LinearLayoutManager(requireActivity())
        main_tool_name_recycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        ItemTouchHelper(
            SwipeRecyclerHelper(
                main_tool_name_recycler,
                deleteButton = this::deleteButton,
                editButton = this::editButton
            )
        ).attachToRecyclerView(main_tool_name_recycler)
    }

    private fun setupToolParamRecycler() {
        main_tool_parameter_recycler.adapter = toolParamAdapter
        main_tool_parameter_recycler.layoutManager = LinearLayoutManager(requireActivity())
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
        toolListAdapter.items = task
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

    private fun handleToolManagementSummaryNavigationEvent() {
        navigationController?.navigate(R.id.action_toolManagementFragment_to_toolManagementSummaryFragment)
    }
}
