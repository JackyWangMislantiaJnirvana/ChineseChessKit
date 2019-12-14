package scproj.chesskit.client

import scproj.chesskit.server.Server
import scproj.chesskit.server.ServerModel
import tornadofx.Controller
import kotlin.concurrent.thread

class ServerThreadController : Controller() {
    var server = Server()
    fun changeModel(newServerModel: ServerModel) {
        server.serverModel = newServerModel
    }

    init {
        thread(start = true, isDaemon = true, name = "local server") {
            server.asRealServer().start()
        }
    }
}