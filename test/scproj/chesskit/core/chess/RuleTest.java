package scproj.chesskit.core.chess;

import org.junit.jupiter.api.Test;
import scproj.chesskit.core.data.PlayerSide;

class RuleTest {

    @Test
    void movePieceMove() {
        ChessGridElement[][] grid=RulesKt.getDEFAULT_CHESSPLATE();
        grid[5][0]=ChessGridElement.BLACK_SOLDIER;
        System.out.println(Rule.movePieceMove(grid, grid[6][4], PlayerSide.RED, 6, 4, 5, 4));
    }
}