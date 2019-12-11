package scproj.chesskit.core.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RuleTest {

    @Test
    void movePieceMove() {
        ChessGridElement[][] grid=RulesKt.getDEFAULT_CHESSPLATE();
        grid[5][0]=ChessGridElement.BLACK_SOLDIER;
        System.out.println(Rule.movePieceMove(grid,grid[6][4],6,4,5,4));
    }
}