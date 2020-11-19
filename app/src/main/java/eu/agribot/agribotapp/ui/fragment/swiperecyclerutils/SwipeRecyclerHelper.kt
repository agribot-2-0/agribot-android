package eu.agribot.agribotapp.ui.fragment.swiperecyclerutils

import androidx.recyclerview.widget.RecyclerView

class SwipeRecyclerHelper(
    val recyclerView: RecyclerView,
    val deleteButton: (itemPosition: Int) -> UnderlayButton,
    val editButton: (itemPosition: Int) -> UnderlayButton
) : SwipeHelper(recyclerView) {

    override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
        return listOf(deleteButton(position), editButton(position))
    }
}
