package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder

sealed class Tools(val toolId: Int) {
    sealed class TplAngle(val id: Int) : Tools(id) {
        object Front : TplAngle(5)
        object Rear : TplAngle(11)
    }

    sealed class TplHeight(val id: Int) : Tools(id) {
        object Front : TplHeight(6)
        object Rear : TplHeight(12)
    }

    sealed class Blue(val id: Int) : Tools(id) {
        object Front : Blue(4)
        object Rear : Blue(10)
    }

    sealed class Green(val id: Int) : Tools(id) {
        object Front : Green(2)
        object Rear : Green(8)
    }

    sealed class Yellow(val id: Int) : Tools(id) {
        object Front : Yellow(3)
        object Rear : Yellow(9)
    }

    sealed class Pto(val id: Int) : Tools(id) {
        object Front : Pto(1)
        object Rear : Pto(7)
    }

}