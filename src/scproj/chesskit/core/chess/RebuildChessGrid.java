package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.GameStatus;
import scproj.chesskit.core.data.Movement;
import scproj.chesskit.core.data.PlayerSide;

import java.util.Arrays;
import java.util.List;

public class RebuildChessGrid {
    public static ChessGridElement[][] rebuildChessGrid(GameStatus gameStatus, ChessGridElement[][] initialGrid) {
        ChessGridElement[][] defaultChessplate = initialGrid;
        ChessGridElement[][] grid = new ChessGridElement[10][9];
        for (int i = 0; i < defaultChessplate.length; i++) {
            grid[i] = Arrays.copyOf(defaultChessplate[i], 9);
        }
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
                PlayerSide side=moveList.get(i).getPlayer();
                ChessGridElement piece=grid[starti][startj];
                if(Rule.movePieceMove(grid,piece,side,starti,startj,endi,endj)){
                    grid[endi][endj]=grid[starti][startj];
                    grid[starti][startj]=ChessGridElement.EMPTY;
                    i++;
                }
                else{
                    return null;
                }
            }
        }
        return grid;
    }
}
