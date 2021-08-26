package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemToolParamBinding
import kotlinx.android.synthetic.main.list_item_hitch.view.*
import kotlinx.android.synthetic.main.list_item_tool_param.view.tool_param_value
import kotlinx.android.synthetic.main.list_item_tool_param.view.tool_value_tx
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager

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
        holder.itemView.tool_param_value.isEnabled = false
        when (item.toolParamID) {
            ListToolParamItem.VELOCITY.toolParamId -> {
                holder.itemView.tool_param_value.min = item.minValue
                holder.itemView.tool_param_value.max = item.maxValue
                holder.itemView.tool_param_value.progress = item.paramValue
                holder.itemView.tool_value_tx.text = String.format(
                    "%.1f",
                    item.paramValue.toDouble() * ToolDataManager.VELOCITY_PRECISION_TO_FINAL
                )
            }
            ListToolParamItem.PTO.toolParamId -> {
                holder.itemView.tool_param_value.min = item.minValue
                holder.itemView.tool_param_value.max = item.maxValue
                holder.itemView.tool_param_value.progress = item.paramValue
                holder.itemView.tool_value_tx.text =
                    item.paramValue.toString()
            }
            ListToolParamItem.TPL_PUSH_PULL.toolParamId -> {
                holder.itemView.tool_param_value.min = item.minValue
                holder.itemView.tool_param_value.max = item.maxValue
                holder.itemView.tool_param_value.progress = item.paramValue
                holder.itemView.tool_value_tx.text = String.format(
                    "%.2f",
                    item.paramValue.toDouble() * ToolDataManager.TPL_PUSH_PULL_PRECISION_TO_FINAL
                )
            }
            ListToolParamItem.TPL_UP_DOWN.toolParamId -> {
                holder.itemView.tool_param_value.min = item.minValue
                holder.itemView.tool_param_value.max = item.maxValue
                holder.itemView.tool_param_value.progress = item.paramValue
                holder.itemView.tool_value_tx.text = String.format(
                    "%.1f",
                    item.paramValue.toDouble() * ToolDataManager.TPL_UP_DOWN_PRECISION_TO_FINAL
                )
            }
            ListToolParamItem.YELLOW_FLOW.toolParamId -> {
                holder.itemView.tool_param_value.min = item.minValue
                holder.itemView.tool_param_value.max = item.maxValue
                holder.itemView.tool_param_value.progress = item.paramValue
                holder.itemView.tool_value_tx.text =
                    item.paramValue.toString()
            }
            ListToolParamItem.GREEN_FLOW.toolParamId -> {
                holder.itemView.tool_param_value.min = item.minValue
                holder.itemView.tool_param_value.max = item.maxValue
                holder.itemView.tool_param_value.progress = item.paramValue
                holder.itemView.tool_value_tx.text =
                    item.paramValue.toString()
            }
            ListToolParamItem.BLUE_FLOW.toolParamId -> {
                holder.itemView.tool_param_value.min = item.minValue
                holder.itemView.tool_param_value.max = item.maxValue
                holder.itemView.tool_param_value.progress = item.paramValue
                holder.itemView.tool_value_tx.text =
                    item.paramValue.toString()
            }
        }
    }
}
