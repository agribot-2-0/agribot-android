package eu.agribot.agribotapp.ui.fragment.settings.recycler.settingsaction

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemSettingsActionBinding

class SettingsActionViewHolder(viewBinding: ListItemSettingsActionBinding) :
    BaseViewHolder<SettingsActionViewModel, ListItemSettingsActionBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: SettingsActionViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
