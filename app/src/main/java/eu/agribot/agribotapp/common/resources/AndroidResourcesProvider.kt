package eu.agribot.agribotapp.common.resources

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.BoolRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList

/**
 * Retreives all kinds of resources from
 * Androids system res and assets
 */
class AndroidResourcesProvider(private val context: Context) :
    ResourcesProvider {

    override fun getStringFormatted(@StringRes resId: Int, vararg args: Any): String =
        context.resources.getString(resId, *args)

    override fun getString(@StringRes resId: Int): String =
        context.resources.getString(resId)

    override fun getQuantityString(@PluralsRes resId: Int, count: Int, vararg args: Any): String =
        context.resources.getQuantityString(resId, count, *args)

    override fun getInteger(resId: Int): Int =
        context.resources.getInteger(resId)

    override fun getDimension(@DimenRes resId: Int): Float =
        context.resources.getDimension(resId)

    override fun getBoolean(@BoolRes resId: Int): Boolean =
        context.resources.getBoolean(resId)

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getDrawable(@DrawableRes resId: Int): Drawable =
        context.resources.getDrawable(resId, context.theme)

    override fun getAttrDrawable(@AttrRes attrId: Int): Drawable {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrId, typedValue, true)
        return getDrawable(typedValue.resourceId)
    }

    override fun getColor(resId: Int): Int =
        context.resources.getColor(resId, context.theme)

    override fun getAttrColor(@AttrRes attrId: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrId, typedValue, true)
        return getColor(typedValue.resourceId)
    }

    override fun getAssets(): AssetManager =
        context.resources.assets

    override fun parseToolIdToName(toolId: Int): String = when (toolId) {
        ToolsList.FRONT_UP_TPL -> context.getString(R.string.front_up_tpl_name)

        ToolsList.FRONT_DOWN_TPL -> context.getString(R.string.frony_down_tpl_name)

        ToolsList.BACK_UP_TPL -> context.getString(R.string.back_up_tpl_name)

        ToolsList.BACK_DOWN_TPL -> context.getString(R.string.back_rear_tpl_name)

        ToolsList.FRONT_PTO -> context.getString(R.string.front_pto_name)

        ToolsList.FRONT_BLUE -> context.getString(R.string.front_blue_name)

        ToolsList.FRONT_YELLOW -> context.getString(R.string.front_yellow_name)

        ToolsList.FRONT_GREEN -> context.getString(R.string.front_green_name)

        ToolsList.BACK_PTO -> context.getString(R.string.rear_pto_name)

        ToolsList.BACK_BLUE -> context.getString(R.string.rear_blue_name)

        ToolsList.BACK_YELLOW -> context.getString(R.string.rear_yellow_name)

        ToolsList.BACK_GREEN -> context.getString(R.string.rear_green_name)
        else -> context.getString(R.string.undefined_tool_name)
    }
}
