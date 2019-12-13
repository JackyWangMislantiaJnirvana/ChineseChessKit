package scproj.chesskit.server

import mu.KotlinLogging
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

val logger = KotlinLogging.logger { }

fun handleObserves(serverModel: ServerModel): HttpHandler = { _: Request ->
    logger.debug { "inside observe handler" }
    when (serverModel.serverStatus) {
        ServerStatus.WAITING_FOR_PLAYER -> Response(NOT_STARTED).body(serverModel.gameRegistry.serializedRegistry)
        ServerStatus.RED_IN_ACTION -> Response(RED_IN_ACTION).body(serialize(serverModel.gameStatus))
        ServerStatus.BLACK_IN_ACTION -> Response(BLACK_IN_ACTION).body(serialize(serverModel.gameStatus))
        ServerStatus.RED_WON -> Response(RED_WON).body(serialize(serverModel.gameStatus))
        ServerStatus.BLACK_WON -> Response(BLACK_WON).body(serialize(serverModel.gameStatus))
        ServerStatus.TIE -> Response(TIE).body(serialize(serverModel.gameStatus))
    }
}

fun handleMove(serverModel: ServerModel): HttpHandler = { request: Request ->
    val movement = movementDeserialize(request.bodyString())!!
    serverModel.gameStatus = GameStatus(
        movementSequence = serverModel.gameStatus.movementSequence + movement,
        serialNumber = serverModel.gameStatus.serialNumber + 1
    )
    if (!movement.isUndo) {
        when (serverModel.serverStatus) {
            ServerStatus.BLACK_IN_ACTION -> serverModel.serverStatus = ServerStatus.RED_IN_ACTION
            ServerStatus.RED_IN_ACTION -> serverModel.serverStatus = ServerStatus.BLACK_IN_ACTION
        }
    }
    Response(MOVEMENT_SUCCESS).body(serialize(serverModel.gameStatus))
}

fun handleRegistration(serverModel: ServerModel): HttpHandler = { request: Request ->
    val playerSide = playerSideDeserialize(request.path("side"))!!
    serverModel.gameRegistry.setOccupation(playerSide, true)

    if (!serverModel.gameRegistry.getTheOther(playerSide)) {
        Response(WAITING_FOR_OPPONENT)
    } else {
        serverModel.serverStatus = ServerStatus.RED_IN_ACTION
        logger.info { "paring complete, starting game" }
        Response(PARING_COMPLETE)
    }
}