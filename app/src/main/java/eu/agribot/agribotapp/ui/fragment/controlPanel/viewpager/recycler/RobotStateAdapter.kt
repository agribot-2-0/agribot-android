package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemRobotStateBinding
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.robotdata.items.RobotStateItems
import kotlinx.android.synthetic.main.list_item_robot_state.view.stateImage

class RobotStateAdapter :
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

    override fun onBindViewHolder(holder: RobotStateViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        @DrawableRes
        val imageSrc: Int = when (item.stateItemId) {
            RobotStateItems.FUEL_LEVEL.ID -> {
                R.drawable.ic_dashboard_robot_speed
            }
            RobotStateItems.TASK_PROGRESS.ID -> {
                R.drawable.ic_dashboard_estimated_time
            }
            RobotStateItems.ESTIMATED_TASK_FINISH_TIME.ID -> {
                R.drawable.ic_dashboard_path_progress
            }
            RobotStateItems.ROBOT_SPEED.ID -> {
                R.drawable.ic_dashboard_fuel_level
            }

            else -> -1
        }


        holder.itemView.stateImage.setImageResource(imageSrc)
    }

}

