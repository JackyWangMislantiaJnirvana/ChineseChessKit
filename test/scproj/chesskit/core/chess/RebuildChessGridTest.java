package scproj.chesskit.core.chess;

import netscape.javascript.JSUtil;
import org.junit.jupiter.api.Test;
import scproj.chesskit.core.data.GameStatus;
import scproj.chesskit.core.data.PlayerSide;

import java.util.Arrays;

import static scproj.chesskit.core.data.SerializationKt.gameStatusDeserialize;

import java.util.Scanner;

class RebuildChessGridTest {

    @Test
    void rebuildChessGrid() {
        Scanner scan=new Scanner(System.in);
        String[] board=new String[11];
        String[] list=new String[87];
        for(int i=0;i<=10;i++){
            board[i]=scan.nextLine();
        }
        for(int i=0;i<=87;i++){
            list[i]=scan.nextLine();
        }
        Adapter1Result result1=Adapter.adapter(board);

        Adapter2Result result2=AdapterMoveList.AdapterMoveList(list,result1.grid, PlayerSide.BLACK);//这里会有和前天晚上一样的错误
        GameStatus gameStatus=new GameStatus(result2.moveList,87);
        ChessGridElement[][] grid=RebuildChessGrid.rebuildChessGrid(gameStatus,result1.grid);//前3行对result1.grid做出了修改，故而这里会出bug，可以复制数组解决
        for(int i=0;i<10;i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid[i][j]+" ");
            }
            System.out.println();
        }
    }
}