package scproj.chesskit.client

import scproj.chesskit.server.Server
import tornadofx.Controller
import kotlin.concurrent.thread

class ServerThreadController : Controller() {
    var serverThread: Thread? = null
    fun changeAndStart(newServer: Server) {
        serverThread?.stop()
        serverThread = thread(start = true, isDaemon = true, name = "local server") {
            newServer.asRealServer().start()
        }
    }
}