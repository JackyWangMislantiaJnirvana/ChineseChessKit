package scproj.chesskit.core.chess;

public class Adapter2Rule extends Rule{
    public static Adapter2Mistake AdapterMoveListRule(ChessGridElement[][] grid,int starti,int startj,int endi,int endj,int side){
        if(starti < 0 ||startj<0||starti>=10||startj>=9 ||endi < 0 || endj < 0 || endi >= 10 || endj >= 9)return Adapter2Mistake.Position_Out_Of_Range;
        else if(grid[starti][startj] == ChessGridElement.EMPTY) return Adapter2Mistake.Invalid_From_Position;
        else if(side==0&&!isBlack(grid[starti][startj])) return Adapter2Mistake.Invalid_From_Position;
        else if(side==1&&!isRed(grid[starti][startj])) return  Adapter2Mistake.Invalid_From_Position;
        else if (isRed(grid[starti][startj])){
            if(isRed(grid[endi][endj])){
                return Adapter2Mistake.Invalid_To_Position;
            }
        }
        else if(isBlack(grid[starti][startj])) {
            if (isBlack(grid[endi][endj])) {
                return Adapter2Mistake.Invalid_To_Position;
            }
        }
        else return Adapter2Mistake.Invalid_Move_Pattern;
        return Adapter2Mistake.Invalid_Move_Pattern;
    }
}
