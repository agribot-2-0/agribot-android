package eu.agribot.agribotapp.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import eu.agribot.agribotapp.R
import kotlinx.android.synthetic.main.view_bottom_item_details.view.*

class ViewBottomItemDetails @JvmOverloads constructor(
    context: Context,
    attributeSettings: AttributeSet? = null,
    defStyleValue: Int = 0
) : LinearLayout(context, attributeSettings, defStyleValue) {

    var firstButtonText: String = ""
        set(value) {
            field = value
            itemViewFirstButton.isVisible = value.isNotEmpty()
            itemViewFirstButton.text = value
        }

    var firstButtonClickListener: OnClickListener? = null
        set(value) {
            field = value
            itemViewFirstButton.setOnClickListener(value)
        }

    var secondButtonText: String = ""
        set(value) {
            field = value
            itemViewSecondButton.isVisible = value.isNotEmpty()
            itemViewSecondButton.text = value
        }

    var secondButtonClickListener: OnClickListener? = null
        set(value) {
            field = value
            itemViewSecondButton.setOnClickListener(value)
        }

    var thirdButtonText: String = ""
        set(value) {
            field = value
            itemViewThirdButton.isVisible = value.isNotEmpty()
            itemViewThirdButton.text = value
        }

    var thirdButtonClickListener: OnClickListener? = null
        set(value) {
            field = value
            itemViewThirdButton.setOnClickListener(value)
        }

    var fourthButtonText: String = ""
        set(value) {
            field = value
            itemViewFourthButton.isVisible = value.isNotEmpty()
            itemViewFourthButton.text = value
        }

    var fourthButtonClickListener: OnClickListener? = null
        set(value) {
            field = value
            itemViewFourthButton.setOnClickListener(value)
        }

    var itemNameText: String = ""
        set(value) {
            field = value
            itemViewName.isVisible = value.isNotEmpty()
            itemViewName.text = value
        }

    var progressBarState: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                itemViewProgressBar.isVisible = true
                itemViewProgressBarText.isVisible = true
            } else {
                itemViewProgressBar.isVisible = false
                itemViewProgressBarText.isVisible = false
            }
        }

    init {
        inflate(context, R.layout.view_bottom_item_details, this)
        context.withStyledAttributes(attributeSettings, R.styleable.ViewBottomItemDetails) {
            firstButtonText = getString(R.styleable.ViewBottomItemDetails_firstButtonText) ?: ""
            secondButtonText = getString(R.styleable.ViewBottomItemDetails_secondButtonText) ?: ""
            thirdButtonText = getString(R.styleable.ViewBottomItemDetails_thirdButtonText) ?: ""
            fourthButtonText = getString(R.styleable.ViewBottomItemDetails_fourthButtonText) ?: ""
            itemNameText = getString(R.styleable.ViewBottomItemDetails_itemName) ?: ""
            progressBarState = getInt(R.styleable.ViewBottomItemDetails_progressValue, -1)
        }
    }
}
