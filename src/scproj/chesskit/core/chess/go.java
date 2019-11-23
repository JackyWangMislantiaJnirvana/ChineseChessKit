package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.Movement;
import scproj.chesskit.core.data.PlayerSide;

public class go {
    public static Movement getNextStep(ChessGrid chessGrid, PlayerSide playerSide) {
        // 这样获取点阵
        ChessGridElement[][] grid = chessGrid.getGrid();
        // ......解决问题

        // 如何返回结果：new一个Movement对象出来，填入谁走的，从哪里移动到哪里，是否是悔棋
        // 关于如何调用构造函数：
        // Movement(
        //  PlayerSide 谁走的,
        //  Integer 从哪个x坐标拿棋子,
        //  Integer 从哪个y坐标拿棋子,
        //  Integer 把拿起来的棋子放到哪个x坐标,
        //  Integer 把拿起来的棋子放到哪个y坐标,
        //  Boolean 是否是悔棋
        // )
        //
        // 下面是一个例子：
        return new Movement(PlayerSide.BLACK, 1, 1, 2, 2, false);
    }

    public static void main(String[] args) {

    }
}