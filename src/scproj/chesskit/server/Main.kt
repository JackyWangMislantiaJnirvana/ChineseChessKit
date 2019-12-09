package scproj.chesskit.server

/*
import mu.KotlinLogging
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.core.Method.GET
import org.http4k.core.*
import org.http4k.routing.path
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Jetty
import org.http4k.server.asServer
import scproj.chesskit.core.chess.ChessGrid
import scproj.chesskit.core.chess.EndGameDetect
import scproj.chesskit.core.chess.isMovementValid
import scproj.chesskit.core.communication.*
import scproj.chesskit.core.data.*

fun observeHandlerPure(gameStatus: GameStatus, serverStatus: ServerStatus, request: Request): Response =
    when (serverStatus) {
        ServerStatus.WAITING_FOR_PLAYER -> Response(NOT_STARTED)
        ServerStatus.RED_IN_ACTION -> Response(RED_IN_ACTION).body(serialize(gameStatus))
        ServerStatus.BLACK_IN_ACTION -> Response(BLACK_IN_ACTION).body(serialize(gameStatus))
        ServerStatus.GAME_OVER -> Response(GAME_OVER).body(serialize(gameStatus))
    }

fun main() {
    val logger = KotlinLogging.logger {  }
    var gameStatus = GameStatus(listOf<Movement>(), 0)
    var serverStatus: ServerStatus = ServerStatus.WAITING_FOR_PLAYER
    var registrationStatus = mutableMapOf(
        PlayerSide.BLACK to false,
        PlayerSide.RED to true
    )

//    val observeHandler: HttpHandler = {
//        when (serverStatus) {
//            ServerStatus.WAITING_FOR_PLAYER -> Response(NOT_STARTED)
//            ServerStatus.RED_IN_ACTION -> Response(RED_IN_ACTION).body(serialize(gameStatus))
//            ServerStatus.BLACK_IN_ACTION -> Response(BLACK_IN_ACTION).body(serialize(gameStatus))
//            ServerStatus.GAME_OVER -> Response(GAME_OVER).body(serialize(gameStatus))
//        }
//    }


    val observeHandler: HttpHandler = {request ->
        observeHandlerPure(gameStatus, serverStatus, request)
    }

    val playHandler: HttpHandler = { request: Request ->
        val side = playerSideDeserialize(request.path("side"))
        if (side != null) {
            val movement = movementDeserialize(request.bodyString())
            if (movement != null) {
                // Handling revert moves
                if (movement.isUndo && gameStatus.movementSequence.size >= 2) {
                    gameStatus = GameStatus(
                        movementSequence = gameStatus.movementSequence.subList(0, gameStatus.movementSequence.size - 2),
                        serialNumber = gameStatus.serialNumber + 1
                    )
                    Response(MOVEMENT_SUCCESS)
                        .body(serialize(gameStatus))
                // Handling regular moves
                } else if (isMovementValid(movement, gameStatus)) {
                    gameStatus = GameStatus(
                        movementSequence = gameStatus.movementSequence + movement,
                        serialNumber = gameStatus.serialNumber + 1
                    )
                    // TODO EndGameQ
                    val winner = EndGameDetect.getWinner(ChessGrid(gameStatus));

                    Response(MOVEMENT_SUCCESS)
                        .body(serialize(gameStatus))
                } else
                    Response(INVALID_CHESS_MOVEMENT)
            } else
                Response(UNRECOGNIZED_MOVEMENT)
        } else
            Response(INVALID_PLAYER_SIDE)
    }

    val registerHandler: HttpHandler = {request: Request ->
        if (serverStatus != ServerStatus.GAME_OVER || serverStatus != ServerStatus.WAITING_FOR_PLAYER) {
            val player = playerSideDeserialize(request.path("side"))
            if (player != null) {
                if (player == PlayerSide.RED && registrationStatus[PlayerSide.RED] == false) {
                    registrationStatus[PlayerSide.RED] = true
                    if (registrationStatus[PlayerSide.BLACK] == false)
                        Response(WAITING_FOR_OPPONENT)
                    else {
                        serverStatus = ServerStatus.RED_IN_ACTION
                        Response(PARING_COMPLETE)
                    }
                } else if (player == PlayerSide.BLACK && registrationStatus[PlayerSide.BLACK] == false) {
                    registrationStatus[PlayerSide.BLACK] = true
                    if (registrationStatus[PlayerSide.BLACK] == false)
                        Response(WAITING_FOR_OPPONENT)
                    else {
                        serverStatus = ServerStatus.RED_IN_ACTION
                        Response(PARING_COMPLETE)
                    }
                } else {
                    Response(SIDE_OCCUPIED)
                }
            } else {
                Response(INVALID_PLAYER_SIDE)
            }
        } else {
            Response(FORBIDDEN)
        }
    }

    val server: HttpHandler = routes(
        "/play/{side}" bind POST to playHandler,
        "/observe" bind observeHandler,
        "/reset" bind GET to {
            serverStatus = ServerStatus.WAITING_FOR_PLAYER
            gameStatus = GameStatus(movementSequence = emptyList(), serialNumber = 0)
            Response(OK)
        },
        "/register/{side}" bind POST to registerHandler
    )
    server.asServer(Jetty(9000)).start()
}
*/