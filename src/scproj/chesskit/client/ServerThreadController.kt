package scproj.chesskit.client

import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.server.Server
import scproj.chesskit.server.ServerModel
import scproj.chesskit.server.ServerStatus
import tornadofx.Controller
import kotlin.concurrent.thread

class ServerThreadController : Controller() {
    var server = Server()
    fun changeModel(newServerModel: ServerModel) {
        server.serverModel = newServerModel
        if (newServerModel.gameStatus.movementSequence.lastOrNull() != null) {
            server.serverModel.serverStatus =
                when (newServerModel.gameStatus.movementSequence.lastOrNull()?.player) {
                    PlayerSide.RED -> ServerStatus.BLACK_IN_ACTION
                    PlayerSide.BLACK -> ServerStatus.RED_IN_ACTION
                    else -> ServerStatus.RED_IN_ACTION
                }
        }
    }

    init {
        thread(start = true, isDaemon = true, name = "local server") {
            server.asRealServer().start()
        }
    }
}