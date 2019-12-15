package scproj.chesskit.core.chess;

import org.junit.jupiter.api.Test;
import scproj.chesskit.core.data.GameStatus;
import scproj.chesskit.core.data.Movement;
import scproj.chesskit.core.data.PlayerSide;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class BestMovementTest {

    @Test
    void bestMovement() {
        Scanner scan = new Scanner(System.in);
        String[] s = new String[14];
        for (int i = 0; i < 14; i++) {
            s[i] = scan.nextLine();
        }
        Adapter1Result result1 = Adapter.adapter(s);
        ChessGrid chessGrid=new ChessGrid(result1.grid);
        Movement move=BestMovement.BestMovement(chessGrid, PlayerSide.BLACK);
        System.out.println();
    }
}