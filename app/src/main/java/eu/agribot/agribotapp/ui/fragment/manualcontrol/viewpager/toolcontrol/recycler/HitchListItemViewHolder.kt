package eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemHitchBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel

class HitchListItemViewHolder(viewBinding: ListItemHitchBinding) :
    BaseViewHolder<HitchListItemViewModel, ListItemHitchBinding>(
        viewBinding
    ) {

    override fun bind(
        viewModel: HitchListItemViewModel
    ) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
