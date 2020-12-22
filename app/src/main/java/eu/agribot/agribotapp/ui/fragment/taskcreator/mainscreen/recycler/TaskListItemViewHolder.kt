package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.TaskListItemBinding

class TaskListItemViewHolder(viewBinding: TaskListItemBinding) :
    BaseViewHolder<TaskListItemViewModel, TaskListItemBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: TaskListItemViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
