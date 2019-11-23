package scproj.chesskit.core.chess

import scproj.chesskit.core.chess.ChessGridElement.*
import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.Movement

class ChessGrid(private val gameStatus: GameStatus = GameStatus(emptyList(), 0)) {
    val grid:
//        arrayOf(BLACK_VEHICLE,  BLACK_RIDER,    BLACK_MINISTER, BLACK_SERVANT,  BLACK_GENERAL,  BLACK_SERVANT,  BLACK_MINISTER, BLACK_RIDER,    BLACK_VEHICLE ),
//        arrayOf(EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY         ),
//        arrayOf(EMPTY,          BLACK_CANNON,   EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          BLACK_CANNON,   EMPTY         ),
//        arrayOf(BLACK_SOLDIER,  EMPTY,          BLACK_SOLDIER,  EMPTY,          BLACK_SOLDIER,  EMPTY,          BLACK_SOLDIER,  EMPTY,          BLACK_SOLDIER ),
//        arrayOf(EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY         ),
//         BORDERLINE
//        arrayOf(EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY         ),
//        arrayOf(RED_SOLDIER,    EMPTY,          RED_SOLDIER,    EMPTY,          RED_SOLDIER,    EMPTY,          RED_SOLDIER,    EMPTY,          RED_SOLDIER   ),
//        arrayOf(EMPTY,          RED_CANNON,     EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          RED_CANNON,     EMPTY         ),
//        arrayOf(EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY,          EMPTY         ),
//        arrayOf(RED_VEHICLE,    RED_RIDER,      RED_MINISTER,   RED_SERVANT,    RED_GENERAL,    RED_SERVANT,    RED_MINISTER,   RED_RIDER,      RED_VEHICLE   )
//    )



    fun update(newStatus: GameStatus) {
        val delta = newStatus - this.gameStatus
    }

    private fun move(movement: Movement) {

    }
}

