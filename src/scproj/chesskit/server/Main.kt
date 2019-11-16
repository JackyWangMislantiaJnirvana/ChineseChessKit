package scproj.chesskit.server

import mu.KotlinLogging
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.util.*
import org.http4k.core.Method.GET
import org.http4k.core.*
import org.http4k.routing.path
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNSATISFIABLE_PARAMETERS
import org.http4k.server.Jetty
import org.http4k.server.asServer
import scproj.chesskit.core.chess.isMovementValid
import scproj.chesskit.core.communication.*
import scproj.chesskit.core.data.*

fun main() {
    val logger = KotlinLogging.logger {  }
    var gameStatus = GameStatus(listOf<Movement>(), 0)
    var serverStatus: Status = Status.WAITING_FOR_PLAYER

    val observeHandler: HttpHandler =
        {request: Request ->
        when (serverStatus) {
            Status.WAITING_FOR_PLAYER -> Response(NOT_STARTED)
            Status.RED_IN_ACTION -> Response(RED_IN_ACTION).body(serialize(gameStatus))
            Status.BLACK_IN_ACTION -> Response(BLACK_IN_ACTION).body(serialize(gameStatus))
//            Status.GAME_OVER -> Response(GAME_OVER).body(serialize) TODO
        }
        Response(OK).body(serialize(gameStatus))
    }
    val playHandler: HttpHandler = { request: Request ->
        val side = playerSideDeserialize(request.path("side"))
        if (side != null) {
            val movement = movementDeserialize(request.bodyString())
            if (movement != null) {
                if (isMovementValid(movement, gameStatus)) {
                    gameStatus = GameStatus(
                        movementSequence = gameStatus.movementSequence + movement,
                        serialNumber = gameStatus.serialNumber + 1
                    )
                    Response(MOVEMENT_SUCCESS)
                        .body(serialize(gameStatus))
                }
                else
                    Response(INVALID_CHESS_MOVEMENT)
            } else
                Response(UNRECOGNIZED_MOVEMENT)
        } else
            Response(INVALID_PLAYER_SIDE)
    }
    val registerHandler: HttpHandler

    val server: HttpHandler = routes(
        "/play/{side}" bind POST to playHandler,
        "/observe" bind observeHandler,
        "/reset" bind GET to {
            serverStatus = Status.WAITING_FOR_PLAYER
            gameStatus = GameStatus(movementSequence = emptyList(), serialNumber = 0)
            Response(OK)
        }//,
//        "/register/{side}" bind POST to registerHandler
    )
    server.asServer(Jetty(9000)).start()
}
