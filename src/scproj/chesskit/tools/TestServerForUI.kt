package scproj.chesskit.tools

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.Jetty
import org.http4k.server.asServer
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.vbox

class TestServerForUI : View() {
    val controller = AsyncTestServerUIController()
    val server: HttpHandler = {
        Response(Status.OK).body(controller.state.toString())
    }
    override val root = vbox {
        button("A") {
            action {
                controller.state = true
            }
        }
        button("B") {
            action {
                controller.state = false
            }
        }
        server.asServer(Jetty(9000)).start()
    }
}

class AsyncTestServerUIController {
    var state = false
}