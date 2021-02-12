package eu.agribot.agribotapp.communication.database.firestorage

enum class DownloaderPath(val filePath: String) {

    TOOLS_PHOTO("/user_tools"),

    LAYER_PATH("/user_path"),

    MAP_PATH("/user_map"),

    PATH_SOURCE("/path/")
}
