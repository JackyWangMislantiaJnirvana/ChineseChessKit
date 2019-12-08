package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.GameStatus;
import scproj.chesskit.core.data.Movement;

import java.util.List;

public class RebuildChessGrid {
    public static ChessGridElement[][] rebuildChessGrid(GameStatus gameStatus) {
        ChessGridElement[][] grid=RulesKt.DEFAULT_CHESSPLATE;
        List<Movement> moveList=gameStatus.getMovementSequence();
        for(int i=0;i<moveList.size();i++){
            if(moveList.get(i+2).isUndo()){
                moveList.remove(i);
                moveList.remove(i+1);
                moveList.remove(i+2);
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
                }
                else{
                    System.out.println("The movement is invalid");
                }
            }
        }
        return new ChessGridElement[10][9];
    }
}
