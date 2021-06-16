package eu.agribot.agribotapp.communication.bluetooth.basic.message.toolcontrolmessage

sealed class Tools(val toolId: Int) {

    sealed class TplAngle(id: Int) : Tools(id) {
        object Front : TplAngle(5)
        object Rear : TplAngle(11)
    }

    sealed class TplHeight(id: Int) : Tools(id) {
        object Front : TplHeight(6)
        object Rear : TplHeight(12)
    }

    sealed class Blue(id: Int) : Tools(id) {
        object Front : Blue(4)
        object Rear : Blue(10)
    }

    sealed class Green(id: Int) : Tools(id) {
        object Front : Green(2)
        object Rear : Green(8)
    }

    sealed class Yellow(id: Int) : Tools(id) {
        object Front : Yellow(3)
        object Rear : Yellow(9)
    }

    sealed class Pto(id: Int) : Tools(id) {
        object Front : Pto(1)
        object Rear : Pto(7)
    }
}
