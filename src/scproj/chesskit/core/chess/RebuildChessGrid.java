package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.GameStatus;
import scproj.chesskit.core.data.Movement;

import java.util.List;

//import java.util.List;

public class RebuildChessGrid {
    public static ChessGridElement[][] rebuildChessGrid(GameStatus gameStatus) {
        System.out.println(RulesKt.getDEFAULT_CHESSPLATE()[6][4]);
        ChessGridElement[][] grid=RulesKt.getDEFAULT_CHESSPLATE();
        List<Movement> moveList=gameStatus.getMovementSequence();
        int i=0;
        while(i<moveList.size()){
            if ( i + 2 <= moveList.size()-1 && moveList.get(i + 2).isUndo()) {
                i=i+3;
            }
            else{
                int starti=moveList.get(i).getMovingFrom().getPositionX();
                int startj=moveList.get(i).getMovingFrom().getPositionY();
                int endi=moveList.get(i).getMovingTo().getPositionX();
                int endj=moveList.get(i).getMovingTo().getPositionY();
                ChessGridElement piece=grid[starti][startj];
                if(Rule.movePieceMove(grid,piece,starti,startj,endi,endj)){
                    grid[endi][endj]=grid[starti][startj];
                    grid[starti][startj]=ChessGridElement.EMPTY;
                    i++;
                }
                else{
                    return null;  //错误抛出
                }
            }
        }
        return grid;
    }
}
