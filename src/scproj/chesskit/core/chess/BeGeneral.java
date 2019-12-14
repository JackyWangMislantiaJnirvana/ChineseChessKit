package scproj.chesskit.core.chess;

public class BeGeneral {
    public static boolean beGeneral(ChessGridElement[][] grid,ChessGridElement piece,int Gpx,int Gpy){
            for (int i = 0; i < 10; i++) {
                if (grid[i][Gpy] != piece&&grid[i][Gpy]!=ChessGridElement.EMPTY){
                    if(Rule.movePieceMove(grid,grid[i][Gpy],i,Gpy,Gpx,Gpy))
                        return true;
                }
            }
            for (int i=0;i<9;i++){
                if(grid[Gpx][i]!=piece&&grid[Gpx][i]!=ChessGridElement.EMPTY){
                    if(Rule.movePieceMove(grid,grid[Gpx][i],Gpx,i,Gpx,Gpy)) return true;
                }
            }
            return false;
    }
}
