package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparameters

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementParamsBinding
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import kotlinx.android.synthetic.main.fragment_path_management_new_path.*
import kotlinx.android.synthetic.main.fragment_tool_management_params.params_recycler
import kotlinx.android.synthetic.main.fragment_tool_management_params.params_tool_photo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ToolParamsFragment :
    BaseFragment<ToolParamsViewModel, FragmentToolManagementParamsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_management_params
    override val viewModel: ToolParamsViewModel by viewModel { parametersOf(requireContext()) }
    private val hitchRecyclerAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemDetails.itemNameText = viewModel.displayToolName.value?:""
        observeViewModel()
        viewModel.createToolsList()
        setupRecyclers()
        setToolPhoto()
    }

    private fun setToolPhoto() {
        Glide.with(requireContext())
            .load(viewModel.getToolPhotoDirection())
            .into(params_tool_photo)
    }

    @ExperimentalCoroutinesApi
    private fun observeViewModel() {
        viewModel.paramsList.observe(this::addParams)
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
        viewModel.onHitchParametersChanged(sliderNumber, sliderValue)
    }

    private fun addParams(tools: List<HitchListItemViewModel>) {
        hitchRecyclerAdapter.items = tools
    }

    override fun bindViewVariables(viewBinding: FragmentToolManagementParamsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNavigationEvent -> handleToolManagementNavigationEvent()
        else -> Unit
    }

    private fun handleToolManagementNavigationEvent() {
        navigationController?.navigate(R.id.action_toolManagementParamsFragment_to_toolManagementFragment)
    }
}
