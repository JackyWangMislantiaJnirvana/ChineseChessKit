package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.PlayerSide;

public class EndGameDetect {
    public static PlayerSide getWinner(ChessGrid chessGrid) {
        // 根据棋盘状态判断哪一方玩家胜利，返回玩家
        return PlayerSide.BLACK;
    }
}
