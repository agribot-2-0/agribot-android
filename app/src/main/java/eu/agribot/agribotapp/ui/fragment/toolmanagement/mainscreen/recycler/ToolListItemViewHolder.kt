package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemToolBinding

class ToolListItemViewHolder(viewBinding: ListItemToolBinding) :
    BaseViewHolder<ToolListItemViewModel, ListItemToolBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: ToolListItemViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
