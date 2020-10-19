package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemCloudfilesBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler.CloudFileListItemViewModel

class CloudFileListItemViewHolder(viewBinding: ListItemCloudfilesBinding) :
    BaseViewHolder<CloudFileListItemViewModel, ListItemCloudfilesBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: CloudFileListItemViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
