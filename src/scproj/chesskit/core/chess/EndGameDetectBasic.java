package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.PlayerSide;

public class EndGameDetectBasic {
    public static PlayerSide getWinner(ChessGrid chessGrid) {
        // 根据棋盘状态判断哪一方玩家胜利，返回玩家
        ChessGridElement[][] grid=chessGrid.getGrid();
        int CheckBlack=0;
        kkk:
        for(int i=0;i<=4;i++){
            for(int j=0;j<=8;j++){
                if(grid[i][j]==ChessGridElement.BLACK_GENERAL){
                    CheckBlack=1;
                    break kkk;
                }
            }
        }
        int CheckRed=0;
        kkk:
        for(int i=5;i<=9;i++){
            for(int j=0;j<=8;j++){
                if(grid[i][j]==ChessGridElement.RED_GENERAL){
                    CheckRed=1;
                    break kkk;
                }
            }
        }
        if(CheckBlack==1&&CheckRed==0) return PlayerSide.BLACK;         //黑胜
        if(CheckBlack==0&&CheckRed==1) return PlayerSide.RED;           //红胜
        if (CheckBlack == 0 && CheckRed == 0) return PlayerSide.TIE;       //和棋
//        return PlayerSide.Continue;                                     //继续
        return null;
    }
}
