import scproj.chesskit.core.chess.ChessGridElement
import scproj.chesskit.core.chess.Rule

fun main() {
    val res =
        Rule.movePieceMove(scproj.chesskit.core.chess.DEFAULT_CHESSPLATE, ChessGridElement.RED_SOLDIER, 6, 4, 5, 4)
    println(res)
}