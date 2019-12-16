package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.PlayerSide;

public class BeGeneral {
    public static boolean beGeneral(ChessGridElement[][] grid,ChessGridElement piece,int Gpx,int Gpy){
        PlayerSide side=null;
        PlayerSide enermy=null;
        if(piece==ChessGridElement.BLACK_GENERAL) {side=PlayerSide.BLACK;enermy=PlayerSide.RED;}
        if(piece==ChessGridElement.RED_GENERAL) {side=PlayerSide.RED;enermy=PlayerSide.BLACK;}
            for (int i = 0; i < 10; i++) {
                if (grid[i][Gpy] != piece&&grid[i][Gpy]!=ChessGridElement.EMPTY){
                    if(Rule.movePieceMove(grid,grid[i][Gpy],enermy,i,Gpy,Gpx,Gpy))
                        return true;
                }
            }
            for (int i=0;i<9;i++){
                if(grid[Gpx][i]!=piece&&grid[Gpx][i]!=ChessGridElement.EMPTY){
                    if(Rule.movePieceMove(grid,grid[Gpx][i],enermy,Gpx,i,Gpx,Gpy)) return true;
                }
            }
            return false;
    }
}
