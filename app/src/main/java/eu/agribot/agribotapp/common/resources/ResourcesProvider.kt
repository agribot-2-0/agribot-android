package eu.agribot.agribotapp.common.resources

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import androidx.annotation.*

/**
 * Interface used to retreive resources and assets
 * @see AndroidResourcesProvider
 */
interface ResourcesProvider {

    fun getString(@StringRes resId: Int): String

    fun getStringFormatted(@StringRes resId: Int, vararg args: Any): String

    fun getQuantityString(@PluralsRes resId: Int, count: Int, vararg args: Any): String

    fun getInteger(@IntegerRes resId: Int): Int

    fun getDimension(@DimenRes resId: Int): Float

    fun getBoolean(@BoolRes resId: Int): Boolean

    fun getDrawable(@DrawableRes resId: Int): Drawable

    fun getAttrDrawable(@AttrRes attrId: Int): Drawable

    @ColorInt
    fun getColor(@ColorRes resId: Int): Int

    @ColorInt
    fun getAttrColor(@AttrRes attrId: Int): Int

    fun getAssets(): AssetManager
}
