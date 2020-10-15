package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils

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