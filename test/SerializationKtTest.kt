import org.junit.jupiter.api.Test
import scproj.chesskit.core.data.*

internal class SerializationKtTest {
    @Test
    fun testSerialize() {
        val movementTestData = Movement(
            player = PlayerSide.RED,
            movingFrom = Coordinate(1, 1),
            movingTo = Coordinate(2, 2),
            isUndo = false
        )

        val gameStatusTestingData = GameStatus(
            movementSequence = listOf(
                Movement(
                    player = PlayerSide.RED,
                    movingFrom = Coordinate(1, 1),
                    movingTo = Coordinate(2, 2),
                    isUndo = false
                ),
                Movement(
                    player = PlayerSide.BLACK,
                    movingFrom = Coordinate(1, 1),
                    movingTo = Coordinate(2, 2),
                    isUndo = false
                ),
                Movement(
                    player = PlayerSide.RED,
                    movingFrom = Coordinate(1, 1),
                    movingTo = Coordinate(2, 2),
                    isUndo = false
                )
            ),
            serialNumber = 3
        )

        println(
            serialize(
                movementTestData
            )
        )

        println(
            serialize(
                gameStatusTestingData
            )
        )
    }

    @org.junit.jupiter.api.Test
    fun gameStatusDeserialize() {
    }

    @org.junit.jupiter.api.Test
    fun movementDeserialize() {
    }
}