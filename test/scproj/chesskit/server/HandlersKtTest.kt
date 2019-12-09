package scproj.chesskit.server

import org.http4k.core.Method
import org.http4k.core.Request
import org.junit.jupiter.api.Test
import scproj.chesskit.core.communication.BLACK_IN_ACTION
import scproj.chesskit.core.communication.means
import scproj.chesskit.core.data.*
import kotlin.test.assertEquals

internal class HandlersKtTest {
    val sm = ServerModel(
        gameStatus = GameStatus(
            movementSequence = listOf(
                Movement(
                    player = PlayerSide.RED,
                    movingFrom = Coordinate(1, 1),
                    movingTo = Coordinate(2, 2),
                    isUndo = false
                )
            ),
            serialNumber = 1
        ),
        serverStatus = ServerStatus.BLACK_IN_ACTION,
        isBlackOccupied = true,
        isRedOccupied = false
    )

    @Test
    fun handleObservesTest() {
        val response = handleObserves(sm).invoke(Request(Method.GET, ""))
        println(response)
        assert(response.status means BLACK_IN_ACTION)
        assertEquals(sm.gameStatus, gameStatusDeserialize(response.bodyString()))
    }

    @Test
    fun handleMoveTest() {
    }

    @Test
    fun handleRegistrationTest() {
    }
}