package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemToolParamBinding

class ListToolParamViewHolder(viewBinding: ListItemToolParamBinding) :
    BaseViewHolder<ListToolParamViewModel, ListItemToolParamBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: ListToolParamViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
