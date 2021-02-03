package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary.recycler

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemRfidDataBinding

class ReceivedRfidDataViewHolder(viewBinding: ListItemRfidDataBinding) :
    BaseViewHolder<ReceivedRfidDataViewModel, ListItemRfidDataBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: ReceivedRfidDataViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
