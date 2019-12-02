package scproj.chesskit.core.data

import com.squareup.moshi.Json

data class Movement(
    val player: PlayerSide,
    @Json(name = "moving_from") val movingFrom: Coordinate,
    @Json(name = "moving_to") val movingTo: Coordinate,
    @Json(name = "is_undo") val isUndo: Boolean
) {
    constructor(player: PlayerSide, fromX: Int, fromY: Int, toX: Int, toY: Int, isUndo: Boolean):
            this(player = player,
                movingFrom = Coordinate(fromX, fromY),
                movingTo = Coordinate(toX, toY),
                isUndo = isUndo)
}

data class GameStatus(
    @Json(name = "movement_sequence") val movementSequence: List<Movement>,
    @Json(name = "serial_number") val serialNumber: Long
) {
    operator fun minus(gameStatus: GameStatus): List<Movement> = gameStatus.movementSequence - this.movementSequence
}

enum class PlayerSide {
    RED, BLACK
}

data class Coordinate(
    @Json(name = "position_x") val positionX: Int,
    @Json(name = "position_y") val positionY: Int
)

data class RegisterStatus(
    @Json(name = "red_occupied") val redOccupied: Boolean,
    @Json(name = "black_occupied") val blackOccupied: Boolean
)
