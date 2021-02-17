package eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemRobotStateBinding
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
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
                R.drawable.ic_dashboard_fuel_level
            }
            RobotStateItems.TASK_PROGRESS.ID -> {
                R.drawable.ic_dashboard_path_progress
            }
            RobotStateItems.ESTIMATED_TASK_FINISH_TIME.ID -> {
                R.drawable.ic_dashboard_estimated_time
            }
            RobotStateItems.ROBOT_SPEED.ID -> {
                R.drawable.ic_dashboard_robot_speed
            }
            RobotStateItems.START_TIME.ID -> {
                R.drawable.ic_dashboard_start_time
            }
            RobotStateItems.START_DATE.ID -> {
                R.drawable.ic_dashboard_start_date
            }
            RobotStateItems.WIND.ID -> {
                R.drawable.ic_dashboard_wind
            }
            RobotStateItems.TEMPERATURE.ID -> {
                R.drawable.ic_dashboard_temperature
            }
            RobotStateItems.PRESSURE.ID -> {
                R.drawable.ic_dashboard_pressure
            }
            RobotStateItems.HUMIDITY.ID -> {
                R.drawable.ic_dashboard_humidity
            }
            RobotStateItems.CURRENT_TASK.ID -> {
                R.drawable.ic_dashboard_current_task
            }
            RobotStateItems.CURRENT_PATH.ID -> {
                R.drawable.ic_dashboard_path_progress
            }
            RobotStateItems.MANUAL_CONTROL_VOLTAGE.ID -> {
                R.drawable.ic_manual_control_voltage
            }
            RobotStateItems.MANUAL_CONTROL_ENGINE_ROTATIONS.ID -> {
                R.drawable.ic_manual_control_rotations
            }
            RobotStateItems.MANUAL_CONTROL_ENGINE_TORQUE.ID -> {
                R.drawable.ic_manual_control_torque
            }
            RobotStateItems.MANUAL_CONTROL_FUEL_LEVEL.ID -> {
                R.drawable.ic_dashboard_fuel_level
            }
            RobotStateItems.MANUAL_CONTROL_FUEL_RATE.ID -> {
                R.drawable.ic_manual_control_fuel_rate
            }
            RobotStateItems.MANUAL_CONTROL_OIL_TEMPERATURE.ID -> {
                R.drawable.ic_manual_control_oil_temp
            }
            else -> -1
        }

        holder.itemView.stateImage.setImageResource(imageSrc)
    }
}
