package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.databinding.ListItemRfidDataBinding

class ReceivedRfidDataAdapter() :
    RecyclerView.Adapter<ReceivedRfidDataViewHolder>() {

    var items = listOf<ReceivedRfidDataViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                ReceivedRfidDataDiffUtill(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedRfidDataViewHolder =
        ReceivedRfidDataViewHolder(
            ListItemRfidDataBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ReceivedRfidDataViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}
