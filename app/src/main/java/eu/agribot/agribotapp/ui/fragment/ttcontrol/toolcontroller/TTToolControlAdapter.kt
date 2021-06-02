package eu.agribot.agribotapp.ui.fragment.ttcontrol.toolcontroller

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.base.BaseViewHolder
import eu.agribot.agribotapp.databinding.ListItemHitchBinding
import eu.agribot.agribotapp.databinding.ListItemTttoolControlLabelBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import kotlinx.android.synthetic.main.list_item_hitch.view.*

class TTToolControlAdapter(val onItemCharacteristicChange: (position: Int, sliderValue: Int, isActivated: Boolean) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType: Int = 0

    var mainItems = listOf<HitchListItemViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                MainHolderDiffUtill(
                    mainItems,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    var labelItems = listOf<ToolControlLabelViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                LabelHolderDiffUtill(
                    labelItems,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    class TTToolControlMainViewHolder(viewBinding: ListItemHitchBinding) :
        BaseViewHolder<HitchListItemViewModel, ListItemHitchBinding>(
            viewBinding
        ) {

        override fun bind(
            viewModel: HitchListItemViewModel
        ) {
            viewBinding.viewModel = viewModel
            viewBinding.executePendingBindings()
        }
    }

    class TTToolControlLabelViewHolder(viewBinding: ListItemTttoolControlLabelBinding) :
        BaseViewHolder<ToolControlLabelViewModel, ListItemTttoolControlLabelBinding>(
            viewBinding
        ) {

        override fun bind(viewModel: ToolControlLabelViewModel) {
            viewBinding.viewModel = viewModel
            viewBinding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        viewType = position
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LABEL_HOLDER_NUMBER -> TTToolControlLabelViewHolder(
                ListItemTttoolControlLabelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            MAIN_HOLDER_NUMBER -> TTToolControlMainViewHolder(
                ListItemHitchBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> TTToolControlLabelViewHolder(
                ListItemTttoolControlLabelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType %2 == 0) {
            true -> {
                Log.e("xd", " label" + labelItems)
                val item = labelItems[position]
                (holder as TTToolControlLabelViewHolder).bind(item)
            }

            false -> {
                Log.e("xd", " tools" + mainItems)
                val item = mainItems[position]
                (holder as TTToolControlMainViewHolder).bind(item)


                var isItemActive = false
                var itemSliderValue = 0

                holder.itemView.recycler_tool_value.text =
                    "${item.hitchListItemDetail.toolStartValue} ${item.hitchListItemDetail.toolUnit}"
                holder.itemView.recycler_seekbar.progress =
                    item.hitchListItemDetail.toolStartValue
                holder.itemView.recycler_seekbar.max = item.hitchListItemDetail.maxValue

                holder.itemView.recycler_seekbar.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {

                    @SuppressLint("SetTextI18n")
                    override fun onProgressChanged(
                        seekBar: SeekBar,
                        progress: Int,
                        b: Boolean
                    ) {
                        holder.itemView.recycler_tool_value.text =
                            "$progress ${item.hitchListItemDetail.toolUnit}"
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
        }
    }

    private fun onItemParametrsChange(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        onItemCharacteristicChange(sliderNumber, sliderValue, isActivated)
    }

    override fun getItemCount(): Int {
        return 16
    }

    class MainHolderDiffUtill(
        private val oldList: List<HitchListItemViewModel>,
        private val newList: List<HitchListItemViewModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].toolName == newList[newItemPosition].toolName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    class LabelHolderDiffUtill(
        private val oldList: List<ToolControlLabelViewModel>,
        private val newList: List<ToolControlLabelViewModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].labelText == newList[newItemPosition].labelText
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    companion object {
        const val LABEL_HOLDER_NUMBER = 0
        const val MAIN_HOLDER_NUMBER = 2
    }

}