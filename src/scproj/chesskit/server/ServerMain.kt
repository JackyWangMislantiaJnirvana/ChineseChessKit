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
            checkPlayerSide(serverModel)
                .then(checkMovement(serverModel))
                .then(checkRevert(serverModel))
                .then(checkEndgame(serverModel))
                .then(handleMove(serverModel))
                .invoke(request)
        },
        "/observe" bind GET to { request: Request ->
            handleObserves(serverModel)
                .invoke(request)
        },
        "/register/{side}" bind POST to { request: Request ->
            checkServerStatus(serverModel)
                .then(checkOccupation(serverModel))
                .then(handleRegistration(serverModel))
                .invoke(request)
        }
    )

    server.asServer(Jetty(9000)).start()
}