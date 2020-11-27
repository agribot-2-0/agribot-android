package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect.spinner

import android.util.Log
import android.view.View
import android.widget.AdapterView

class SpinnerSelectedItemAdapter(val onSelected: (position: Int) -> Unit) :
    AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onSelected(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.e("SpinnerSelectedItem", "No item selected")
    }
}
