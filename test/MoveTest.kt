import scproj.chesskit.client.GameController
import scproj.chesskit.core.data.Coordinate
import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.Movement
import scproj.chesskit.core.data.PlayerSide
import tornadofx.View
import tornadofx.pane

fun main() {
    MoveTest().fuck()
}

class MoveTest : View() {
    val controller: GameController by inject()
    override val root = pane()
    fun fuck() {
        controller.updateGameStatus(
            GameStatus(
                movementSequence = listOf(
                    Movement(
                        player = PlayerSide.RED,
                        movingFrom = Coordinate(positionX = 6, positionY = 6),
                        movingTo = Coordinate(positionX = 5, positionY = 6),
                        isUndo = false
                    )
                ),
                serialNumber = 1
            )
        )
    }
}
