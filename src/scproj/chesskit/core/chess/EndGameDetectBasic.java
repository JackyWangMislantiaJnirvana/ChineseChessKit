package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.PlayerSide;

public class EndGameDetectBasic {
    protected static PlayerSide getWinner(ChessGrid chessGrid) {
        // 根据棋盘状态判断哪一方玩家胜利，返回玩家
        ChessGridElement[][] grid=chessGrid.getGrid();
        int CheckBlack=0;
        for(int i=1;i<=5;i++){
            for(int j=1;j<=9;j++){
                if(grid[i][j]==ChessGridElement.BLACK_GENERAL){
                    CheckBlack=1;
                    break;
                }
            }
        }
        int CheckRed=0;
        for(int i=6;i<=10;i++){
            for(int j=1;j<=9;j++){
                if(grid[i][j]==ChessGridElement.RED_GENERAL){
                    CheckRed=1;
                    break;
                }
            }
        }
        if(CheckBlack==1&&CheckRed==0) return PlayerSide.BLACK;         //黑胜
        if(CheckBlack==0&&CheckRed==1) return PlayerSide.RED;           //红胜
        if(CheckBlack==0&&CheckRed==0) return PlayerSide.Balance;       //和棋
        return PlayerSide.Continue;                                     //继续
    }
}
