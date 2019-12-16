package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.Movement;
import scproj.chesskit.core.data.PlayerSide;

import java.util.List;

public class BestMovement {
    public static Movement BestMovement(ChessGrid chessGrid, PlayerSide playerSide) {
        ChessGridElement[][] grid=chessGrid.getGrid();
        List<CanMove> movesRed=CanMoveList.GetRedCanMove(chessGrid);
        List<CanMove> movesBlack=CanMoveList.GetBlackCanMove(chessGrid);
        if(playerSide==PlayerSide.RED){
            CanMove moveRed=new CanMove(ChessGridElement.EMPTY,0,0,0,0);
            for(CanMove click:movesRed){
                ChessGridElement piece=grid[click.endi][click.endj];
                if(getScore(moveRed.entry)<=getScore(piece)){
                    moveRed=click;
                }
            }
            return new Movement(PlayerSide.RED, moveRed.starti, moveRed.startj, moveRed.endi, moveRed.endj, false);
        } else if (playerSide == PlayerSide.BLACK) {
            CanMove moveBlack=new CanMove(ChessGridElement.EMPTY,0,0,0,0);
            for (CanMove click : movesBlack) {
                ChessGridElement piece = grid[click.endi][click.endj];
                if (getScore(moveBlack.entry) <= getScore(piece)) {
                    moveBlack = click;
                }
            }
            return new Movement(PlayerSide.BLACK, moveBlack.starti, moveBlack.startj, moveBlack.endi, moveBlack.endj, false);
        }
        return null;
    }
    private static int getScore(ChessGridElement piece){
        if(piece==ChessGridElement.BLACK_GENERAL) return 7;
        else if(piece==ChessGridElement.BLACK_VEHICLE) return 6;
        else if(piece==ChessGridElement.BLACK_RIDER) return 5;
        else if(piece==ChessGridElement.BLACK_CANNON) return 4;
        else if(piece==ChessGridElement.BLACK_MINISTER) return 3;
        else if(piece==ChessGridElement.BLACK_SERVANT) return 2;
        else if(piece==ChessGridElement.BLACK_SOLDIER) return 1;
        else if(piece==ChessGridElement.RED_GENERAL) return 7;
        else if(piece==ChessGridElement.RED_VEHICLE) return 6;
        else if(piece==ChessGridElement.RED_RIDER) return 5;
        else if(piece==ChessGridElement.RED_CANNON) return 4;
        else if(piece==ChessGridElement.RED_MINISTER) return 3;
        else if(piece==ChessGridElement.RED_SERVANT) return 2;
        else if(piece==ChessGridElement.RED_SOLDIER) return 1;
        return 0;
    }
}
