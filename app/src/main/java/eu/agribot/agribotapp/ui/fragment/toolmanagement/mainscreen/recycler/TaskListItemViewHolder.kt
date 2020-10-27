package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ToolListItemBinding

class ToolListItemViewHolder(viewBinding: ToolListItemBinding) :
    BaseViewHolder<ToolListItemViewModel, ToolListItemBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: ToolListItemViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
