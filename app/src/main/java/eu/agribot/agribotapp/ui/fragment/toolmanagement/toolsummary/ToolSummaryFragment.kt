package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementSummaryBinding
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import kotlinx.android.synthetic.main.fragment_tool_management_params.*
import kotlinx.android.synthetic.main.fragment_tool_management_summary.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class ToolSummaryFragment : BaseFragment<ToolSummaryViewModel, FragmentToolManagementSummaryBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_management_summary
    override val viewModel: ToolSummaryViewModel by viewModel { parametersOf(requireContext()) }


    private val hitchRecyclerAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSummaryParameters()
        setupRecycler()
        viewModel.paramsList.observe(this::addParams)
        setToolPhoto()
    }


    private fun setToolPhoto(){
        Glide.with(requireContext())
            .load(getOutputDirectory().toString() + viewModel.getToolPhotoName())
            .into(summary_tool_photo)
    }


    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNavigationEvent -> handleToolNavigationEvent()
        else -> Unit
    }

    private fun handleToolNavigationEvent(){
        navigationController?.navigate(R.id.action_toolManagementSummaryFragment_to_toolManagementFragment)
    }

    private fun setupRecycler() {
        summary_params_recycler.adapter = hitchRecyclerAdapter
        summary_params_recycler.layoutManager = LinearLayoutManager(requireActivity())

    }

    private fun onHitchRecyclerParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
       // viewModel.onHitchParametersChanged(sliderNumber, sliderValue)
    }


    private fun addParams(tools: List<HitchListItemViewModel>) {
        hitchRecyclerAdapter.items = tools
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    override fun bindViewVariables(viewBinding: FragmentToolManagementSummaryBinding) {
        viewBinding.viewModel = viewModel
    }
}