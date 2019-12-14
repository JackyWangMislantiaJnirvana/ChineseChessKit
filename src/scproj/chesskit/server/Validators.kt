package scproj.chesskit.server

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.routing.path
import scproj.chesskit.core.chess.ChessGrid
import scproj.chesskit.core.chess.EndGameDetectBasic
import scproj.chesskit.core.communication.*
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.core.data.movementDeserialize
import scproj.chesskit.core.data.playerSideDeserialize

// Play validators
// Compose them strictly in order!
// Pre-request validators
fun checkIfServerStatusAllowPlay(serverModel: ServerModel): Filter = Filter { handler: HttpHandler ->
    { request: Request ->
        if (serverModel.serverStatus == ServerStatus.RED_IN_ACTION
            || serverModel.serverStatus == ServerStatus.BLACK_IN_ACTION
        )
            handler(request)
        else
            Response(FORBIDDEN)
    }
}

fun checkPlayerSide(): Filter = Filter { handler: HttpHandler ->
    { request: Request ->
        val playerSide = playerSideDeserialize(request.path("side"))
        if (playerSide != null) {
            handler(request)
        } else {
            Response(INVALID_PLAYER_SIDE)
        }
    }
}

fun checkMovement(): Filter = Filter { handler: HttpHandler ->
    { request: Request ->
        val movement = movementDeserialize(request.bodyString())
        if (movement != null) {
            handler(request)
        } else {
            Response(UNRECOGNIZED_MOVEMENT)
        }
    }
}

fun checkRevert(serverModel: ServerModel): Filter = Filter { handler: HttpHandler ->
    { request: Request ->
        val movement = movementDeserialize(request.bodyString())!!
        if (!movement.isUndo || serverModel.gameStatus.movementSequence.size >= 2) {
            handler(request)
        } else {
            Response(INVALID_CHESS_MOVEMENT)
        }
    }
}

// Post-request validators
// This is the only IMPURE validator
fun checkEndgame(serverModel: ServerModel): Filter = Filter { handler: HttpHandler ->
    { requset: Request ->
        val response = handler(requset)

        val winner: PlayerSide? = EndGameDetectBasic.getWinner(
            ChessGrid(
                serverModel.gameStatus
            )
        )
        logger.info { "winner = $winner" }
        if (winner != null) {
            when (winner) {
                PlayerSide.RED -> {
                    serverModel.serverStatus = ServerStatus.RED_WON
                    response.status(RED_WON)
                }
                PlayerSide.BLACK -> {
                    serverModel.serverStatus = ServerStatus.BLACK_WON
                    response.status(BLACK_WON)
                }
                PlayerSide.TIE -> {
                    serverModel.serverStatus = ServerStatus.TIE
                    response.status(TIE)
                }
            }
        } else {
            response
        }
    }
}


// Register validators
fun checkIfServerStatusAllowRegister(serverModel: ServerModel): Filter = Filter { handler: HttpHandler ->
    { request: Request ->
        if (serverModel.serverStatus == ServerStatus.WAITING_FOR_PLAYER
            || serverModel.serverStatus == ServerStatus.RED_WON
            || serverModel.serverStatus == ServerStatus.BLACK_WON
            || serverModel.serverStatus == ServerStatus.TIE
        )
            handler(request)
        else
            Response(FORBIDDEN)
    }
}

// FIXME
fun checkOccupation(serverModel: ServerModel): Filter = Filter { handler: HttpHandler ->
    { request: Request ->
        val playerSide = playerSideDeserialize(request.path("side"))
        if (playerSide == null) {
            Response(INVALID_PLAYER_SIDE)
        } else if (!serverModel.gameRegistry.checkOccupation(playerSide)) {
            handler(request)
        } else {
            Response(SIDE_OCCUPIED)
        }
    }
}

// Special filter that append http status description to response headers
fun appendStatus(): Filter = Filter { handler: HttpHandler ->
    { request: Request ->
        val response = handler(request)
        response.header("status", response.status.toString())
    }
}
