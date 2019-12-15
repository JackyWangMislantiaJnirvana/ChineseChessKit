package scproj.chesskit.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.then
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import scproj.chesskit.core.data.GameStatus

class Server(
    var serverModel: ServerModel =
        ServerModel(
            GameStatus(
                movementSequence = emptyList(),
                serialNumber = 0
            )
        ),
    val port: Int = 9000
) {
    val httpHandler = routes(
        "/play/{side}" bind POST to { request: Request ->
            checkPlayerSide()
                .then(checkIfServerStatusAllowPlay(serverModel))
                .then(checkMovement())
                .then(checkTurn(serverModel))
                .then(checkRevert(serverModel))
                .then(checkEndgame(serverModel))
                // this filter is used for debugging
                .then(appendStatus())
                .then(handleMove(serverModel))
                .invoke(request)
        },
        "/observe" bind GET to { request: Request ->
            appendStatus()
                .then(handleObserves(serverModel))
                .invoke(request)
        },
        "/register/{side}" bind POST to { request: Request ->
            checkIfServerStatusAllowRegister(serverModel)
                .then(checkOccupation(serverModel))
                // this filter is used for debugging
                .then(appendStatus())
                .then(handleRegistration(serverModel))
                .invoke(request)
        }
    )

    operator fun invoke(request: Request): Response = httpHandler(request)
    fun asRealServer() = httpHandler.asServer(Jetty(port))
}

//fun createServer(
//    serverModel: ServerModel =
//        ServerModel(
//            GameStatus(
//                movementSequence = emptyList(),
//                serialNumber = 0
//            )
//        )
//): HttpHandler =
//    routes(
//        "/play/{side}" bind POST to { request: Request ->
//            checkPlayerSide()
//                .then(checkIfServerStatusAllowPlay(serverModel))
//                .then(checkMovement())
//                .then(checkRevert(serverModel))
//                .then(checkEndgame(serverModel))
//                 this filter is used for debugging
//                .then(appendStatus())
//                .then(handleMove(serverModel))
//                .invoke(request)
//        },
//        "/observe" bind GET to { request: Request ->
//            appendStatus()
//                .then(handleObserves(serverModel))
//                .invoke(request)
//        },
//        "/register/{side}" bind POST to { request: Request ->
//            checkIfServerStatusAllowRegister(serverModel)
//                .then(checkOccupation(serverModel))
//                 this filter is used for debugging
//                .then(appendStatus())
//                .then(handleRegistration(serverModel))
//                .invoke(request)
//        }
//    )

fun main() {
    Server().asRealServer().start()
}