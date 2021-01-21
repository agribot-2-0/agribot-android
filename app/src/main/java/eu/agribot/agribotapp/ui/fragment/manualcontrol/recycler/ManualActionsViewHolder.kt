package eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemManualModeRecyclerBinding

class ManualActionsViewHolder(viewBinding: ListItemManualModeRecyclerBinding) :
    BaseViewHolder<ManualActionsViewModel, ListItemManualModeRecyclerBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: ManualActionsViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
