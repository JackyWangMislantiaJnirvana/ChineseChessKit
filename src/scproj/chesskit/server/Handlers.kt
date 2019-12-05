package scproj.chesskit.server

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.path
import scproj.chesskit.core.communication.*
import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.movementDeserialize
import scproj.chesskit.core.data.playerSideDeserialize
import scproj.chesskit.core.data.serialize

/* Handlers --- closures, (which are impure), that modifies serverModel according to requests.
 * The functions below are FACTORIES of these handlers, injecting serverModel as
 * mutable status information when generating a handler.
 */

fun handleObserves(serverModel: ServerModel): HttpHandler = { _: Request ->
    when (serverModel.serverStatus) {
        ServerStatus.WAITING_FOR_PLAYER -> Response(NOT_STARTED)
        ServerStatus.RED_IN_ACTION -> Response(RED_IN_ACTION).body(serialize(serverModel.gameStatus))
        ServerStatus.BLACK_IN_ACTION -> Response(BLACK_IN_ACTION).body(serialize(serverModel.gameStatus))
        ServerStatus.RED_WON -> Response(RED_WON).body(serialize(serverModel.gameStatus))
        ServerStatus.BLACK_WON -> Response(BLACK_WON).body(serialize(serverModel.gameStatus))
    }
}

fun handleMove(serverModel: ServerModel): HttpHandler = { request: Request ->
    val movement = movementDeserialize(request.bodyString())!!
    serverModel.gameStatus = GameStatus(
        movementSequence = serverModel.gameStatus.movementSequence + movement,
        serialNumber = serverModel.gameStatus.serialNumber + 1
    )
    Response(MOVEMENT_SUCCESS).body(serialize(serverModel.gameStatus))
}

fun handleRegistration(serverModel: ServerModel): HttpHandler = { request: Request ->
    val playerSide = playerSideDeserialize(request.path("side"))!!
    serverModel.gameRegistry.setOccupation(playerSide)

    if (!serverModel.gameRegistry.getTheOther(playerSide)) {
        Response(WAITING_FOR_OPPONENT)
    } else {
        Response(PARING_COMPLETE)
    }
}