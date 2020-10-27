package eu.agribot.agribotapp.ui.fragment.taskcreator

import com.google.android.gms.maps.model.UrlTileProvider
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.pow

class TileProvider(width: Int, height: Int, private val baseUrl: String) :
    UrlTileProvider(width, height) {
    override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
        try {

            return URL(
                baseUrl.replace("{z}", "" + zoom).replace("{x}", "" + x).replace(
                    "{y}", "" + (2.0.pow(zoom) - y - 1).toInt()
                )
            )

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }
}
