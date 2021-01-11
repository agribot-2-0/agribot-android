package eu.agribot.agribotapp.ui.fragment.settings.recycler.settingslist

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemSettingsBinding

class SettingsListViewHolder(viewBinding: ListItemSettingsBinding) :
    BaseViewHolder<SettingsListViewModel, ListItemSettingsBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: SettingsListViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
