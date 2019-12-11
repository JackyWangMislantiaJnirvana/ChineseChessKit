package scproj.chesskit.client

import mu.KotlinLogging
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import scproj.chesskit.core.chess.ChessGrid
import scproj.chesskit.core.chess.DEFAULT_CHESSPLATE
import scproj.chesskit.core.chess.Rule
import scproj.chesskit.core.communication.MOVEMENT_SUCCESS
import scproj.chesskit.core.communication.PARING_COMPLETE
import scproj.chesskit.core.communication.WAITING_FOR_OPPONENT
import scproj.chesskit.core.communication.means
import scproj.chesskit.core.data.*
import tornadofx.Controller

class GameController : Controller() {
    private val logger = KotlinLogging.logger { }
    val httpClient = JavaHttpClient()
    val loggedHttpClient: HttpHandler = { request ->
        logger.debug { "Request:\n$request" }
        val response = httpClient(request)
        logger.debug { "Response:\n$response" }
        response
    }

    var playerSide = PlayerSide.RED
    var serverURL: String = "http://localhost:9000"  // default value for testing
    var status = Status.IDLE
    var gameStatus: GameStatus = GameStatus(
        emptyList(), 0
    )
    var chessGrid: ChessGrid = ChessGrid(DEFAULT_CHESSPLATE)

    fun observe(): Pair<org.http4k.core.Status, GameStatus?> {
        val response = httpClient(Request(Method.GET, "$serverURL/observe"))
        val responseStatus = response.status
        val gameStatus = gameStatusDeserialize(response.bodyString())
        return responseStatus to gameStatus
    }

    fun updateGameStatus(newStatus: GameStatus) {
        gameStatus = newStatus
    }

    fun spin() {

    }

    fun move(movement: Movement): Boolean {
//        val lastMovement = gameStatus.movementSequence.last()
        logger.info { "trying $movement" }
        val chessPiece = chessGrid.grid[movement.movingFrom.positionX][movement.movingFrom.positionY]
        logger.debug {
            """
            chessGrid.grid = ${chessGrid.grid.contentDeepToString()}
            chessPiece = $chessPiece
        """.trimIndent()
        }
        if (!Rule.movePieceMove(
                chessGrid.grid, chessPiece,
                movement.movingFrom.positionX, movement.movingFrom.positionY,
                movement.movingTo.positionX, movement.movingTo.positionY
            )
        ) {
            logger.info { "Movement invalid" }
            return false
        } else {
            logger.info { "Movement valid, uploading" }
            val response = loggedHttpClient(
                Request(Method.POST, "$serverURL/play/$playerSide")
                    .body(serialize(movement))
            )
            if (response.status means MOVEMENT_SUCCESS) {
                logger.info { "Uploaded successfully" }
                return true
            } else {
                logger.info { "Uploading unsuccessful" }
                return false
            }
        }
    }

    fun register(): Boolean {
        val response = httpClient(Request(Method.POST, "$serverURL/register/$playerSide"))
        if (response.status means PARING_COMPLETE || response.status means WAITING_FOR_OPPONENT) {
            logger.info { "Register succeded" }
            return true
        } else {
            logger.info { "Register failed" }
            return false
        }
    }
}