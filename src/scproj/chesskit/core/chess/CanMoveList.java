package scproj.chesskit.core.chess;

import java.util.ArrayList;
import java.util.List;

public class CanMoveList {
    public static List<CanMove> GetBlackCanMove(ChessGrid chessGrid){
        List<CanMove> list=new ArrayList<>();
        ChessGridElement[][] grid=chessGrid.getGrid();
        for(int i=1;i<=5;i++){
            for(int j=1;j<=9;j++){
                if(grid[i][j]!=ChessGridElement.EMPTY){
                    for(int x=1;x<=10;x++){
                        for(int y=1;y<=9;y++){
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
        for(int i=6;i<=10;i++){
            for(int j=1;j<=9;j++){
                if(grid[i][j]!=ChessGridElement.EMPTY){
                    for(int x=1;x<=10;x++){
                        for(int y=1;y<=9;y++){
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
}
