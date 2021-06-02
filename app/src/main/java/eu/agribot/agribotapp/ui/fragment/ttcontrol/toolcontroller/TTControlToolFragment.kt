package eu.agribot.agribotapp.ui.fragment.ttcontrol.toolcontroller

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentTtcontrolToolBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TTControlToolFragment : BaseFragment<TTControlToolViewModel, FragmentTtcontrolToolBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_ttcontrol_tool
    override val viewModel: TTControlToolViewModel by viewModel { parametersOf(context) }

    override fun bindViewVariables(viewBinding: FragmentTtcontrolToolBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeViewModel()
    }

    private val adapter = HitchListItemAdapter(this::onToolItemParamChanged)

    private fun setupRecycler() {
        viewBinding.toolHitchRecycler.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.toolsLabelsList.observe(this::addLabelItems)
        viewModel.toolsSwitchList.observe(this::addToolsSwitchItems)

    }

    private fun addLabelItems(items: List<ToolControlLabelViewModel>) {
       // adapter.labelItems = items
    }

    private fun addToolsSwitchItems(items: List<HitchListItemViewModel>) {
        adapter.items = items
    }

    private fun onToolItemParamChanged(position: Int, sliderValue: Int, isActivated: Boolean) {

    }

}