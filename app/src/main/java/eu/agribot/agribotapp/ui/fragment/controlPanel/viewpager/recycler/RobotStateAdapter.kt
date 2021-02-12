package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemRobotStateBinding

class RobotStateAdapter() :
    RecyclerView.Adapter<RobotStateViewHolder>() {

    var items = listOf<RobotStateViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                RobotStateDiffUtil(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RobotStateViewHolder =
        RobotStateViewHolder(
            ListItemRobotStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RobotStateViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)


    }
}
