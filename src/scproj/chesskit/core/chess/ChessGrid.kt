package scproj.chesskit.core.chess

import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.Movement

class ChessGrid(private val gameStatus: GameStatus = GameStatus(emptyList(), 0)) {
    val grid: Array<Array<ChessGridElement>> = RebuildChessGrid.rebuildChessGrid(gameStatus)

    fun update(newStatus: GameStatus) {
        val delta = newStatus - this.gameStatus
    }

    private fun move(movement: Movement) {

    }
}

