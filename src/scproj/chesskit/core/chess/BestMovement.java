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
            for(CanMove click:movesRed){
                ChessGridElement piece=grid[click.endi][click.endj];
                if(piece==ChessGridElement.BLACK_GENERAL){
                    return new Movement(PlayerSide.RED,click.starti,click.startj,click.endi,click.endj,false);
                }
                else if(piece==ChessGridElement.BLACK_VEHICLE){
                    return new Movement(PlayerSide.RED,click.starti,click.startj,click.endi,click.endj,false);
                }
                else if(piece==ChessGridElement.BLACK_RIDER){
                    return new Movement(PlayerSide.RED,click.starti,click.startj,click.endi,click.endj,false);
                }
                else if(piece==ChessGridElement.BLACK_CANNON){
                    return new Movement(PlayerSide.RED,click.starti,click.startj,click.endi,click.endj,false);
                }
                else if(piece==ChessGridElement.BLACK_MINISTER){
                    return new Movement(PlayerSide.RED,click.starti,click.startj,click.endi,click.endj,false);
                }
                else if(piece==ChessGridElement.BLACK_SERVANT){
                    return new Movement(PlayerSide.RED,click.starti,click.startj,click.endi,click.endj,false);
                }
                else if(piece==ChessGridElement.BLACK_SOLDIER){
                    return new Movement(PlayerSide.RED,click.starti,click.startj,click.endi,click.endj,false);
                }
            }
        } else if (playerSide == PlayerSide.BLACK) {
            for (CanMove click : movesBlack) {
                ChessGridElement piece = grid[click.endi][click.endj];
                if (piece == ChessGridElement.RED_GENERAL) {
                    return new Movement(PlayerSide.BLACK, click.starti, click.startj, click.endi, click.endj, false);
                } else if (piece == ChessGridElement.RED_VEHICLE) {
                    return new Movement(PlayerSide.BLACK, click.starti, click.startj, click.endi, click.endj, false);
                } else if (piece == ChessGridElement.RED_RIDER) {
                    return new Movement(PlayerSide.BLACK, click.starti, click.startj, click.endi, click.endj, false);
                } else if (piece == ChessGridElement.RED_CANNON) {
                    return new Movement(PlayerSide.BLACK, click.starti, click.startj, click.endi, click.endj, false);
                } else if (piece == ChessGridElement.RED_MINISTER) {
                    return new Movement(PlayerSide.BLACK, click.starti, click.startj, click.endi, click.endj, false);
                } else if (piece == ChessGridElement.RED_SERVANT) {
                    return new Movement(PlayerSide.BLACK, click.starti, click.startj, click.endi, click.endj, false);
                } else if (piece == ChessGridElement.RED_SOLDIER) {
                    return new Movement(PlayerSide.BLACK, click.starti, click.startj, click.endi, click.endj, false);
                }
            }
        }
        return null;
    }
}
