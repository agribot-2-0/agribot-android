package eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemHitchBinding
import kotlinx.android.synthetic.main.list_item_hitch.view.recycler_seekbar
import kotlinx.android.synthetic.main.list_item_hitch.view.recycler_switch
import kotlinx.android.synthetic.main.list_item_hitch.view.recycler_tool_value

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

    override fun onBindViewHolder(holder: HitchListItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        var isItemActive = false
        var itemSliderValue = 0

        holder.itemView.recycler_tool_value.text = "${item.toolStartValue} ${item.toolUnit}"
        holder.itemView.recycler_seekbar.progress = item.toolStartValue
        holder.itemView.recycler_seekbar.max = item.maxValue

        holder.itemView.recycler_seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                holder.itemView.recycler_tool_value.text = "$progress ${item.toolUnit}"
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
