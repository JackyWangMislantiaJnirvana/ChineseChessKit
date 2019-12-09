package scproj.chesskit.core.chess;

import java.util.ArrayList;
import java.util.List;

public class CanMoveList {
    public static List<CanMove> GetBlackCanMove(ChessGrid chessGrid){
        List<CanMove> list=new ArrayList<>();
        ChessGridElement[][] grid=chessGrid.getGrid();
        for(int i=0;i<=9;i++){
            for(int j=0;j<=8;j++){
                if(grid[i][j]!=ChessGridElement.EMPTY&&isBlack(grid[i][j])){
                    for(int x=0;x<=9;x++){
                        for(int y=0;y<=8;y++){
                            if(Rule.movePieceMove(grid,grid[i][j],i,j,x,y)){
                                list.add(new CanMove(grid[i][j],i,j,x,y));
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
    public static List<CanMove> GetRedCanMove(ChessGrid chessGrid){
        List<CanMove> list=new ArrayList<>();
        ChessGridElement[][] grid=chessGrid.getGrid();
        for(int i=0;i<=9;i++){
            for(int j=0;j<=8;j++){
                if(grid[i][j]!=ChessGridElement.EMPTY&&isRed(grid[i][j])){
                    for(int x=0;x<=9;x++){
                        for(int y=0;y<=8;y++){
                            if(Rule.movePieceMove(grid,grid[i][j],i,j,x,y)){
                                list.add(new CanMove(grid[i][j],i,j,x,y));
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
    private static boolean isRed(ChessGridElement piece){
        if(piece==ChessGridElement.RED_GENERAL||
        piece==ChessGridElement.RED_RIDER||
        piece==ChessGridElement.RED_CANNON||
        piece==ChessGridElement.RED_MINISTER||
        piece==ChessGridElement.RED_SERVANT||
        piece==ChessGridElement.RED_SOLDIER||
        piece==ChessGridElement.RED_VEHICLE) return true;
        else return false;
    }
    private static boolean isBlack(ChessGridElement piece){
        if(piece==ChessGridElement.BLACK_CANNON||
        piece==ChessGridElement.BLACK_GENERAL||
        piece==ChessGridElement.BLACK_MINISTER||
        piece==ChessGridElement.BLACK_RIDER||
        piece==ChessGridElement.BLACK_SERVANT||
        piece==ChessGridElement.BLACK_SOLDIER||
        piece==ChessGridElement.BLACK_VEHICLE)return true;
        else return false;
    }
}
