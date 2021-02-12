package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemToolParamBinding
import kotlinx.android.synthetic.main.list_item_tool_param.view.tool_param_value
import kotlinx.android.synthetic.main.list_item_tool_param.view.tool_value_tx

class ListToolParamAdapter :
    RecyclerView.Adapter<ListToolParamViewHolder>() {

    var items = listOf<ListToolParamViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                ListToolParamDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListToolParamViewHolder =
        ListToolParamViewHolder(
            ListItemToolParamBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ListToolParamViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.tool_value_tx.text = item.paramValue.toString()
        holder.itemView.tool_param_value.isEnabled = false
    }
}
