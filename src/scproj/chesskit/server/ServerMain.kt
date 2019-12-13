package scproj.chesskit.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import scproj.chesskit.core.data.GameStatus

fun main() {
    // TODO loading saves?
    val serverModel = ServerModel(
        GameStatus(
            movementSequence = emptyList(),
            serialNumber = 0
        )
    )

    val server = routes(
        "/play/{side}" bind POST to { request: Request ->
            checkPlayerSide()
                .then(checkIfServerStatusAllowPlay(serverModel))
                .then(checkMovement())
                .then(checkRevert(serverModel))
//                .then(checkEndgame(serverModel)) TODO
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

    server.asServer(Jetty(9000)).start()
}