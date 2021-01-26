package eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.diagnosticdatabar

import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemManualDiagnosticDataBinding

class DiagnosticDataViewHolder(viewBinding: ListItemManualDiagnosticDataBinding) :
    BaseViewHolder<DiagnosticDataViewModel, ListItemManualDiagnosticDataBinding>(
        viewBinding
    ) {
    override fun bind(viewModel: DiagnosticDataViewModel) {
        viewBinding.viewModel = viewModel
        viewBinding.executePendingBindings()
    }
}
