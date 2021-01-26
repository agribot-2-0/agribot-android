package eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.diagnosticdatabar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemManualDiagnosticDataBinding
import kotlinx.android.synthetic.main.list_item_manual_diagnostic_data.view.*

class DiagnosticDataAdapter : RecyclerView.Adapter<DiagnosticDataViewHolder>() {

    var items = listOf<DiagnosticDataViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                DiagnosticDataDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosticDataViewHolder =
        DiagnosticDataViewHolder(
            ListItemManualDiagnosticDataBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DiagnosticDataViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        @DrawableRes
        val imageSrc: Int = when (item.diagnosticDataID) {
            DiagnosticDataID.VOLTAGE.ID -> {
                R.drawable.ic_car_battery
            }
            DiagnosticDataID.ENGINE_ROTATIONS.ID -> {
                R.drawable.ic_rpm
            }
            DiagnosticDataID.ENGINE_TORQUE.ID -> {
                R.drawable.ic_engine
            }
            DiagnosticDataID.FUEL_LEVEL.ID -> {
                R.drawable.ic_fuel
            }
            DiagnosticDataID.FUEL_RATE.ID -> {
                R.drawable.ic_fuelhour
            }
            DiagnosticDataID.FUEL_TEMPERATURE.ID -> {
                R.drawable.ic_thermometer
            }

            else -> -1
        }

        holder.itemView.diagnosticDataIcon.setImageResource(imageSrc)
    }
}
