package eu.agribot.agribotapp.ui.fragment.autonomousmode.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemCloudfilesBinding

class CloudFileListItemViewHolder(viewBinding: ListItemCloudfilesBinding) :
    BaseViewHolder<CloudFileListItemViewModel, ListItemCloudfilesBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: CloudFileListItemViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
