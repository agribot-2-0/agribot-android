package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.swiperecyclerutils

class SwipeRecyclerClickListener(val itemPosition: Int, val onButtonClicked: (itemPosition: Int) -> Unit) :
    SwipeHelper.UnderlayButtonClickListener {
    override fun onClick() {
        onButtonClicked.invoke(itemPosition)
    }
}
