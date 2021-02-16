package eu.agribot.agribotapp.ui.fragment.swiperecyclerutils

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import eu.agribot.agribotapp.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class SwipeRecyclerUtil(
    val context: Context,
    val onEditSwiped: (position: Int) -> Unit,
    val onRemoveSwiped: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> {
                onRemoveSwiped(viewHolder.adapterPosition)
            }

            ItemTouchHelper.RIGHT -> {
                onEditSwiped(viewHolder.adapterPosition)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (dX > 0) {
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            ).addBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.recycler_swipe_edit
                )
            )
                .addActionIcon(R.drawable.ic_swipe_recycler_edit)
                .create()
                .decorate()
        } else {
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            ).addBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.recycler_swipe_remove
                )
            )
                .addActionIcon(R.drawable.ic_swipe_recycler_remove)
                .create()
                .decorate()
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
