package eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemManualModeRecyclerBinding
import kotlinx.android.synthetic.main.list_item_manual_mode_recycler.view.*

class ManualActionsAdapter(private val onClickSelectedItem: (itemID: Int) -> Unit) :
    RecyclerView.Adapter<ManualActionsViewHolder>() {

    var items = listOf<ManualActionsViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                ManualActionsDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManualActionsViewHolder =
        ManualActionsViewHolder(
            ListItemManualModeRecyclerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ManualActionsViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.manualModeActionButton.setOnClickListener {
            onClickSelectedItem(item.actionID)
        }
    }
}
