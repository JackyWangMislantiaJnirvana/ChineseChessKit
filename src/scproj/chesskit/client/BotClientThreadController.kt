package scproj.chesskit.client

import mu.KotlinLogging
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method
import org.http4k.core.Request
import scproj.chesskit.core.chess.BestMovement
import scproj.chesskit.core.chess.ChessGrid
import scproj.chesskit.core.communication.BLACK_IN_ACTION
import scproj.chesskit.core.communication.BLACK_WON
import scproj.chesskit.core.communication.RED_WON
import scproj.chesskit.core.communication.means
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.core.data.gameStatusDeserialize
import scproj.chesskit.core.data.serialize
import kotlin.concurrent.thread

class BotClientThreadController {
    var serverURL = "http://localhost:9000"
    val httpHandler = JavaHttpClient()
    var isRunning = false
    val logger = KotlinLogging.logger { }

    init {
        thread(start = true, isDaemon = true, name = "bot client") {
            while (true) {
                if (isRunning) {
                    val observedResponse = httpHandler(Request(Method.GET, "$serverURL/observe"))
                    if (observedResponse.status means BLACK_IN_ACTION) {
                        val gameStatus = gameStatusDeserialize(observedResponse.bodyString())
                        val chessGrid = ChessGrid(gameStatus!!)
                        val bestMovement = BestMovement.BestMovement(chessGrid, PlayerSide.BLACK)
                        logger.debug { "Bot moved: $bestMovement" }
                        httpHandler(
                            Request(Method.POST, "$serverURL/play/BLACK").body(serialize(bestMovement))
                        )
                    }
                    if (observedResponse.status means RED_WON || observedResponse.status means BLACK_WON) {
                        isRunning = false
                        logger.info { "Game over, bot stopped." }
                    }
                }
                Thread.sleep(10000)
            }
        }
    }
}