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

    private fun sliderValueTextPrecision(precision: Double): String {
        var string = "%f"
        when (precision) {
            0.01 -> {
                string = "%.2f"
            }
            0.1 -> {
                string = "%.1f"
            }
            else -> {
                string = "%.0f"
            }
        }
        return string
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ListToolParamViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.tool_param_value.isEnabled = false
        holder.itemView.tool_param_value.min = item.minValue
        holder.itemView.tool_param_value.max = item.maxValue
        holder.itemView.tool_param_value.progress =
            item.paramValue
        holder.itemView.tool_value_tx.text =
            sliderValueTextPrecision(item.precision).format(item.paramValue.toDouble() * item.precision)
    }
}
