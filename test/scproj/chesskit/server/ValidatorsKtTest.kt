package scproj.chesskit.server

import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.junit.jupiter.api.Test
import scproj.chesskit.core.data.GameStatus

internal class ValidatorsKtTest {

    @Test
    fun checkPlayerSideTest() {
    }

    @Test
    fun checkMovementTest() {
    }

    @Test
    fun checkRevertTest() {
    }

    @Test
    fun checkEndgameTest() {
    }

    fun `handler used to test validators`(serverModel: ServerModel): HttpHandler = { request: Request ->
        Response(Status.OK).body("Validation passed")
    }

    @Test
    fun checkServerStatusTest() {
        val sm1 = ServerModel(
            gameStatus = GameStatus(
                movementSequence = emptyList(),
                serialNumber = 0
            ),
            serverStatus = ServerStatus.WAITING_FOR_PLAYER
        )
        val sm2 = ServerModel(
            gameStatus = GameStatus(
                movementSequence = emptyList(),
                serialNumber = 0
            ),
            serverStatus = ServerStatus.BLACK_IN_ACTION
        )
        val sm3 = ServerModel(
            gameStatus = GameStatus(
                movementSequence = emptyList(),
                serialNumber = 0
            ),
            serverStatus = ServerStatus.RED_WON
        )
        val res1 = checkServerStatus(sm1).then(`handler used to test validators`(sm1)).invoke(Request(Method.POST, ""))
        val res2 = checkServerStatus(sm2).then(`handler used to test validators`(sm2)).invoke(Request(Method.POST, ""))
        val res3 = checkServerStatus(sm3).then(`handler used to test validators`(sm3)).invoke(Request(Method.POST, ""))
        println("res1 = $res1")
        println("res2 = $res2")
        println("res3 = $res3")
    }

    @Test
    fun checkOccupationTest() {
        val sm = ServerModel(
            gameStatus = GameStatus(
                movementSequence = emptyList(),
                serialNumber = 0
            ),
            serverStatus = ServerStatus.WAITING_FOR_PLAYER,
            isRedOccupied = true,
            isBlackOccupied = false
        )
        val routedTestApp =
            routes(
                "/register/{side}" bind Method.POST to
                        checkOccupation(sm).then(`handler used to test validators`(sm))
            )
        val invalidPlayerSideRes = routedTestApp(Request(Method.POST, "/register/FUCK"))
        val occupiedRes = routedTestApp(Request(Method.POST, "/register/RED"))
        val nonOccupiedRes = routedTestApp(Request(Method.POST, "/register/BLACK"))
        println("invalidPlayerSideRes = $invalidPlayerSideRes")
        println("occupiedRes = $occupiedRes")
        println("nonOccupiedRes = $nonOccupiedRes")
    }
}