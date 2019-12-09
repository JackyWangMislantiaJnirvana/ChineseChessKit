package scproj.chesskit.client

import mu.KotlinLogging
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.core.data.gameStatusDeserialize
import tornadofx.Controller

class GameController() : Controller() {
    private val logger = KotlinLogging.logger { }
    val httpClient = JavaHttpClient()
    val loggedHttpClient: HttpHandler = { request ->
        logger.debug { "Request:\n$request" }
        val response = httpClient(request)
        logger.debug { "Response:\n$response" }
        response
    }

    var playerSide = PlayerSide.RED
    var serverURL: String = ""
    var status = Status.IDLE

    fun observe() = gameStatusDeserialize(httpClient(Request(Method.GET, serverURL)).bodyString())
    //    fun move(movement: Movement): GameStatus? {
//        if (true) {
//            val response = loggedHttpClient(
//                Request(Method.POST, serverURL + "/play/$playerSide")
//                    .body(serialize(movement))
//            )
//
//        } else {
//            return null
//        }
//    }
    fun register() {

    }
}