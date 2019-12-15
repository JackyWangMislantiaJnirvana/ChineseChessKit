package scproj.chesskit.core.chess

import scproj.chesskit.core.data.GameStatus

data class ChessGrid(val grid: Array<Array<ChessGridElement>>) {
    //    val grid: Array<Array<ChessGridElement>> = RebuildChessGrid.rebuildChessGrid(gameStatus)
    constructor(gameStatus: GameStatus) : this(RebuildChessGrid.rebuildChessGrid(gameStatus,null))//把null去了
//    fun update(newStatus: GameStatus) {
//        val delta = newStatus - this.gameStatus
//    }
//
//    private fun move(movement: Movement) {
//
//    }
}

