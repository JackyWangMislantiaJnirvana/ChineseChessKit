package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.GameStatus;
import scproj.chesskit.core.data.PlayerSide;

import java.util.Scanner;

class AdapterMoveListTest {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String[] s = new String[14];
        for (int i = 0; i < 14; i++) {
            s[i] = scan.nextLine();
        }
        Adapter1Result result1 = Adapter.adapter(s);
        //Adapter2Result result2 = AdapterMoveList.AdapterMoveList(s, RulesKt.getDEFAULT_CHESSPLATE(), PlayerSide.RED);
        //GameStatus gameStatus=new GameStatus(result2.moveList,0);
        //ChessGridElement[][] grid=RebuildChessGrid.rebuildChessGrid(gameStatus,RulesKt.getDEFAULT_CHESSPLATE());
        System.out.println();
    }
}