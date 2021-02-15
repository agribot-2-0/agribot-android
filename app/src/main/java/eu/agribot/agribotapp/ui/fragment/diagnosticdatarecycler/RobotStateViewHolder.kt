package eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemRobotStateBinding

class RobotStateViewHolder(viewBinding: ListItemRobotStateBinding) :
    BaseViewHolder<RobotStateViewModel, ListItemRobotStateBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: RobotStateViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}