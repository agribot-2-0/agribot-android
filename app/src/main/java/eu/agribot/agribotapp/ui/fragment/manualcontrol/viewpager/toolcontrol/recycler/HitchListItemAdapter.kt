package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.communication.bluetooth.basic.message.toolcontrolmessage.Tools
import eu.agribot.agribotapp.databinding.ListItemHitchBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.ToolControlViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewHolder
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.ToolRecylerItemId
import kotlinx.android.synthetic.main.list_item_hitch.view.*

class HitchListItemAdapter(
    val onItemCharacteristicChange: (position: Int, sliderValue: Int, isActivated: Boolean) -> Unit
) :
    RecyclerView.Adapter<HitchListItemViewHolder>() {

    var items = listOf<HitchListItemViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                HitchDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HitchListItemViewHolder =
        HitchListItemViewHolder(
            ListItemHitchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HitchListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        var isItemActive = false
        var itemSliderValue = 0

        if (item.isNewToolControl) {
            when (item.switchNumber) {
                Tools.Pto.Front.toolId -> {
                    holder.itemView.recycler_seekbar.min = 0
                    holder.itemView.recycler_seekbar.max = 1000
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + holder.itemView.context.getString(
                            R.string.rpm
                        )
                }
                Tools.Pto.Rear.toolId -> {
                    holder.itemView.recycler_seekbar.min = 0
                    holder.itemView.recycler_seekbar.max = 1000
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + holder.itemView.context.getString(
                            R.string.rpm
                        )
                }
                else -> {
                    holder.itemView.recycler_seekbar.min = 0
                    holder.itemView.recycler_seekbar.max = 100
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + holder.itemView.context.getString(
                            R.string.percent
                        )
                }
            }
        } else {
            when (item.switchNumber) {
                ToolRecylerItemId.V_MAX.ID -> {
                    holder.itemView.recycler_seekbar.min = item.hitchListItemDetail.minValue
                    holder.itemView.recycler_seekbar.max = item.hitchListItemDetail.maxValue
                    holder.itemView.recycler_tool_value.text = String.format(
                        "%.1f",
                        holder.itemView.recycler_seekbar.progress.toDouble() * 0.1
                    ) + item.hitchListItemDetail.toolUnit
                }
                ToolRecylerItemId.PTO.ID -> {
                    holder.itemView.recycler_seekbar.min = item.hitchListItemDetail.minValue
                    holder.itemView.recycler_seekbar.max = item.hitchListItemDetail.maxValue
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + item.hitchListItemDetail.toolUnit
                }
                ToolRecylerItemId.TPL_PUSH_PULL.ID -> {
                    holder.itemView.recycler_seekbar.min = item.hitchListItemDetail.minValue
                    holder.itemView.recycler_seekbar.max = item.hitchListItemDetail.maxValue
                    holder.itemView.recycler_tool_value.text = String.format(
                        "%.2f",
                        holder.itemView.recycler_seekbar.progress.toDouble() * 0.01
                    ) + item.hitchListItemDetail.toolUnit
                }
                ToolRecylerItemId.TPL_UP_DOWN.ID -> {
                    holder.itemView.recycler_seekbar.min = item.hitchListItemDetail.minValue
                    holder.itemView.recycler_seekbar.max = item.hitchListItemDetail.maxValue
                    holder.itemView.recycler_tool_value.text = String.format(
                        "%.2f",
                        holder.itemView.recycler_seekbar.progress.toDouble() * 0.01
                    ) + item.hitchListItemDetail.toolUnit
                }
                ToolRecylerItemId.YELLOW.ID -> {
                    holder.itemView.recycler_seekbar.min = item.hitchListItemDetail.minValue
                    holder.itemView.recycler_seekbar.max = item.hitchListItemDetail.maxValue
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + item.hitchListItemDetail.toolUnit
                }
                ToolRecylerItemId.GREEN.ID -> {
                    holder.itemView.recycler_seekbar.min = item.hitchListItemDetail.minValue
                    holder.itemView.recycler_seekbar.max = item.hitchListItemDetail.maxValue
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + item.hitchListItemDetail.toolUnit
                }
                ToolRecylerItemId.BLUE.ID -> {
                    holder.itemView.recycler_seekbar.min = item.hitchListItemDetail.minValue
                    holder.itemView.recycler_seekbar.max = item.hitchListItemDetail.maxValue
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + item.hitchListItemDetail.toolUnit
                }
                else -> {
                    holder.itemView.recycler_seekbar.min = 0
                    holder.itemView.recycler_seekbar.progress =
                        item.hitchListItemDetail.toolStartValue
                    holder.itemView.recycler_seekbar.max = 100
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + holder.itemView.context.getString(
                            R.string.percent
                        )
                }
            }
        }

        holder.itemView.recycler_seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                if (!item.isNewToolControl) {
                    when (item.switchNumber) {
                        ToolRecylerItemId.V_MAX.ID -> {
                            holder.itemView.recycler_tool_value.text = String.format(
                                "%.1f",
                                holder.itemView.recycler_seekbar.progress.toDouble() * 0.1
                            ) + item.hitchListItemDetail.toolUnit
                        }
                        ToolRecylerItemId.TPL_PUSH_PULL.ID -> {
                            holder.itemView.recycler_tool_value.text = String.format(
                                "%.2f",
                                holder.itemView.recycler_seekbar.progress.toDouble() * 0.01
                            ) + item.hitchListItemDetail.toolUnit
                        }
                        ToolRecylerItemId.TPL_UP_DOWN.ID -> {
                            holder.itemView.recycler_tool_value.text = String.format(
                                "%.2f",
                                holder.itemView.recycler_seekbar.progress.toDouble() * 0.01
                            ) + item.hitchListItemDetail.toolUnit
                        }
                        else -> {
                            holder.itemView.recycler_tool_value.text =
                                "$progress ${item.hitchListItemDetail.toolUnit}"
                        }
                    }
                } else {
                    when (item.switchNumber) {
                        Tools.Pto.Front.toolId -> {
                            holder.itemView.recycler_tool_value.text =
                                holder.itemView.recycler_seekbar.progress.toString() + holder.itemView.context.getString(
                                    R.string.rpm
                                )
                        }
                        Tools.Pto.Rear.toolId -> {
                            holder.itemView.recycler_tool_value.text =
                                holder.itemView.recycler_seekbar.progress.toString() + holder.itemView.context.getString(
                                    R.string.rpm
                                )
                        }
                        else -> {

                            holder.itemView.recycler_tool_value.text =
                                holder.itemView.recycler_seekbar.progress.toString() + holder.itemView.context.getString(
                                    R.string.percent
                                )
                        }
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                itemSliderValue = seekBar.progress
                onItemParametrsChange(
                    item.switchNumber,
                    itemSliderValue,
                    isItemActive
                )
            }
        })

        holder.itemView.recycler_switch.setOnCheckedChangeListener { _, isChecked ->
            if (!item.isNewToolControl) {
                onItemParametrsChange(
                    item.switchNumber,
                    itemSliderValue,
                    isItemActive
                )
            }
            isItemActive = isChecked
        }
    }

    private fun onItemParametrsChange(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        onItemCharacteristicChange(sliderNumber, sliderValue, isActivated)
    }
}
