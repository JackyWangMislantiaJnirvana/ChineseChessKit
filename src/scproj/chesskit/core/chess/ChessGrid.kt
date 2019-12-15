package scproj.chesskit.core.chess

import scproj.chesskit.core.data.GameStatus

data class ChessGrid(val grid: Array<Array<ChessGridElement>>) {
    //    val grid: Array<Array<ChessGridElement>> = RebuildChessGrid.rebuildChessGrid(gameStatus)
    constructor(gameStatus: GameStatus, initialChessGrid: Array<Array<ChessGridElement>> = DEFAULT_CHESSPLATE) :
            this(RebuildChessGrid.rebuildChessGrid(gameStatus, initialChessGrid))
//    fun update(newStatus: GameStatus) {
//        val delta = newStatus - this.gameStatus
//    }
//
//    private fun move(movement: Movement) {
//
//    }
}

