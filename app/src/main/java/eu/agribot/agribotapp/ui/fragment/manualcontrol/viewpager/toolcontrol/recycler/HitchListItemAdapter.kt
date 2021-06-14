package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder.Tools
import eu.agribot.agribotapp.databinding.ListItemHitchBinding
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewHolder
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
                        holder.itemView.recycler_seekbar.progress.toString() + " RPM"

                }
                Tools.Pto.Rear.toolId -> {
                    holder.itemView.recycler_seekbar.min = 0
                    holder.itemView.recycler_seekbar.max = 1000
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + " RPM"
                }
                else -> {
                    holder.itemView.recycler_seekbar.min = 0
                    holder.itemView.recycler_seekbar.max = 100
                    holder.itemView.recycler_tool_value.text =
                        holder.itemView.recycler_seekbar.progress.toString() + " %"
                }
            }

        } else {
            holder.itemView.recycler_tool_value.text = item.progressValue
            holder.itemView.recycler_seekbar.min = 0
            holder.itemView.recycler_seekbar.max = 100
            holder.itemView.recycler_seekbar.progress = item.hitchListItemDetail.toolStartValue
        }


        holder.itemView.recycler_seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                if (!item.isNewToolControl) {
                    holder.itemView.recycler_tool_value.text =
                        "$progress ${item.hitchListItemDetail.toolUnit}"
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
            isItemActive = isChecked
            onItemParametrsChange(
                item.switchNumber,
                itemSliderValue,
                isItemActive
            )
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
