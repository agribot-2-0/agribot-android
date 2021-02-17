package eu.agribot.agribotapp.ui.fragment.settings.recycler.settingslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemSettingsBinding
import kotlinx.android.synthetic.main.list_item_settings.view.settingsCategory
import kotlinx.android.synthetic.main.list_item_settings.view.settingsCategoryIcon

class SettingsListAdapter(val onItemSelected: (itemID: Int) -> Unit) :
    RecyclerView.Adapter<SettingsListViewHolder>() {
    var selectedItemPosition = -1

    var items = listOf<SettingsListViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                SettingsListDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsListViewHolder =
        SettingsListViewHolder(
            ListItemSettingsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SettingsListViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        @DrawableRes
        val imageSrc: Int = when (item.settingID) {
            ItemID.CONNECTION.ID -> {
                R.drawable.ic_settings_bluetooth
            }
            ItemID.ACCOUNT.ID -> {
                R.drawable.ic_settings_user
            }
            ItemID.OTHER.ID -> {
                R.drawable.ic_settings_other
            }

            else -> -1
        }

        holder.itemView.settingsCategoryIcon.setImageResource(imageSrc)

        holder.itemView.settingsCategory.setBackgroundResource(R.color.white)
        if (selectedItemPosition == position) {
            holder.itemView.settingsCategory.setBackgroundResource(R.color.base_green_lighter)
        }

        holder.itemView.settingsCategory.setOnClickListener {
            onItemSelected(item.settingID)
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
}
