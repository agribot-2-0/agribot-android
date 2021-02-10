package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementEditBinding
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import kotlinx.android.synthetic.main.fragment_tool_management_edit.edit_tool_photo
import kotlinx.android.synthetic.main.fragment_tool_management_edit.params_recycler
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ToolEditFragment : BaseFragment<ToolEditViewModel, FragmentToolManagementEditBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_management_edit
    override val viewModel: ToolEditViewModel by viewModel { parametersOf(requireContext()) }
    private val hitchRecyclerAdapter = HitchListItemAdapter(this::onHitchRecyclerParametersChanged)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSummaryParameters()
        setupRecycler()
        viewModel.paramsList.observe(this::addParams)
        setToolPhoto()
    }

    private fun setToolPhoto() {
        Glide.with(requireContext())
            .load(viewModel.getToolPhotoDirection())
            .into(edit_tool_photo)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNavigationEvent -> handleToolNavigationEvent()
        else -> Unit
    }

    private fun handleToolNavigationEvent() {
        navigationController?.navigate(R.id.action_toolManagementSummaryFragment_to_toolManagementFragment)
    }

    private fun setupRecycler() {
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

    override fun bindViewVariables(viewBinding: FragmentToolManagementEditBinding) {
        viewBinding.viewModel = viewModel
    }
}
