package scproj.chesskit.client.view

import scproj.chesskit.core.chess.ChessGridElement
import scproj.chesskit.core.chess.ChessGridElement.*

fun getIcon(piece: ChessGridElement) = when (piece) {
    EMPTY -> TODO("Actually no image")
    RED_GENERAL -> "red_general.png"
    RED_MINISTER -> "red_minister.png"
    RED_SERVANT -> "red_servant.png"
    RED_VEHICLE -> "red_vehicle.png"
    RED_CANNON -> "red_cannon.png"
    RED_RIDER -> "red_rider.png"
    RED_SOLDIER -> "red_soldier.png"
    BLACK_GENERAL -> "black_general.png"
    BLACK_MINISTER -> "black_minister.png"
    BLACK_SERVANT -> "black_servant.png"
    BLACK_VEHICLE -> "black_vehicle.png"
    BLACK_CANNON -> "black_cannon.png"
    BLACK_RIDER -> "black_rider.png"
    BLACK_SOLDIER -> "black_soldier.png"
}
