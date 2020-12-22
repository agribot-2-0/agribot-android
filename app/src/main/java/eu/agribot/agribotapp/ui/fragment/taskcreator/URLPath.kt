package eu.agribot.agribotapp.ui.fragment.taskcreator

enum class URLPath(val path: String) {
    FILE("file://"),

    MAP_PATH("/user_map/maps/{z}/{x}/{y}.png")
}
