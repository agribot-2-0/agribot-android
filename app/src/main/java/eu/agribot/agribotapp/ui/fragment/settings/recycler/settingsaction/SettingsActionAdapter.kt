package eu.agribot.agribotapp.ui.fragment.settings.recycler.settingsaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemSettingsActionBinding
import kotlinx.android.synthetic.main.list_item_settings_action.view.*

class SettingsActionAdapter(val onItemSelected: (itemID: Int) -> Unit) :
    RecyclerView.Adapter<SettingsActionViewHolder>() {

    var items = listOf<SettingsActionViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                SettingsActionDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsActionViewHolder =
        SettingsActionViewHolder(
            ListItemSettingsActionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SettingsActionViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.settingAction.setOnClickListener {
            onItemSelected(item.actionID)
        }
    }
}
