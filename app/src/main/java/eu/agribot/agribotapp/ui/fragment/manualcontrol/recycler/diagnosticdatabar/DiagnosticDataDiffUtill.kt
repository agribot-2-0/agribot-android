package eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.diagnosticdatabar

import androidx.recyclerview.widget.DiffUtil

class DiagnosticDataDiffUtill(
    private val oldList: List<DiagnosticDataViewModel>,
    private val newList: List<DiagnosticDataViewModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].diagnosticDataID == newList[newItemPosition].diagnosticDataID
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
