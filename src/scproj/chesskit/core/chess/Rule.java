package scproj.chesskit.core.chess;
import scproj.chesskit.client.MainKt;
import sun.invoke.empty.Empty;

public class Rule {
    public static boolean movePieceMove(ChessGridElement[][] grid,ChessGridElement piece, int starti, int startj, int endi, int endj){
        int mini=Math.min(starti,endi);
        int minj=Math.min(startj,endj);
        int maxi=Math.max(starti,endi);
        int maxj=Math.max(startj,endj);
        boolean canMove=false;
        if(endi<=0||endj<=0||endi>10||endj>9) return false;
        if(piece==ChessGridElement.EMPTY) return false;
        if(starti==endi&&startj==endj) return false;
        //红方棋子
        if(piece==ChessGridElement.RED_VEHICLE){ //红方车
            if(starti==endi){
                int j=0;
                for(j=minj+1;j<=maxj-1;j++){
                    if(grid[starti][j]!= ChessGridElement.EMPTY){
                        canMove=false;
                        break;
                    }
                if(j==maxj) canMove=true;
                }
            }
            else if(startj==endj){
                int i=0;
                for(i=mini+1;i<=maxi-1;i++){
                    if(grid[i][startj]!= ChessGridElement.EMPTY) {
                        canMove = false;
                        break;
                    }
                }
                if(i==maxi) canMove=true;
            }
        }
    else if(piece==ChessGridElement.RED_RIDER){ //红方马
            int iMove=Math.abs(endi-starti);
            int jMove=Math.abs(endj-startj);
            if(iMove==2&&jMove==1){
                if(endi>starti){
                    if(grid[starti+1][startj]!=ChessGridElement.EMPTY){
                        canMove=false;
                    }
                    else{
                        canMove=true;
                    }
                }
                if(endi<starti){
                    if(grid[starti-1][startj]!=ChessGridElement.EMPTY){
                        canMove=false;
                    }
                    else{
                        canMove=true;
                    }
                }
            }
            else if(iMove==1&&jMove==2){
                if(endj>startj){
                    if(grid[starti][startj+1]!=ChessGridElement.EMPTY){
                        canMove=false;
                    }
                    else{
                        canMove=true;
                    }
                }
                if(endj<startj){
                    if(grid[starti][startj-1]!=ChessGridElement.EMPTY){
                        canMove=false;
                    }
                    else{
                        canMove=true;
                    }
                }
            }
            else canMove=false;
        }
    else if(piece==ChessGridElement.RED_MINISTER){//红方相
        int Centeri=(starti+endi)/2;
        int Centerj=(startj+endj)/2;
        int iMove=Math.abs(endi-starti);
        int jMove=Math.abs(endj-startj);
        if(iMove==2&&jMove==2&&endi>=6){
            if(grid[Centeri][Centerj]!=ChessGridElement.EMPTY){
                canMove=false;
            }
            else {
                canMove=true;
            }
        }
        else canMove=false;
        }
    else if(piece==ChessGridElement.RED_CANNON){//红方炮
        int num=0;
        if(starti==endi){
            int j=0;
            for(j=minj+1;j<=maxj-1;j++){
                if(grid[starti][j]!=ChessGridElement.EMPTY){
                    num++;
                }
            }
            if(num>1) {
                canMove=false;
            }
            else if(num==1){
                if(grid[endi][endj]!=ChessGridElement.EMPTY){
                    canMove=true;
                }
            }
            else if(num==0&&grid[endi][endj]==ChessGridElement.EMPTY){
                canMove=true;
            }
            else canMove=false;
        }
        else if(startj==endj){
            int i=0;
            for(i=mini+1;i<=maxi-1;i++){
                if(grid[i][startj]!=ChessGridElement.EMPTY){
                    num++;
                }
            }
            if(num>1) {
                canMove=false;
            }
            else if(num==1){
                if(grid[endi][endj]!=ChessGridElement.EMPTY){
                    canMove=true;
                }
            }
            else if(num==0&&grid[endi][endj]==ChessGridElement.EMPTY){
                canMove=true;
            }
            else canMove=false;
        }
        else canMove=false;
        }
    else if(piece==ChessGridElement.RED_SOLDIER){//红方兵
        int iMove=Math.abs(endi-starti);
        int jMove=Math.abs(endj-starti);
        if(starti>=6){
            if(starti-endi==1&&jMove==0){
                canMove=true;
            }
            else{
                canMove=false;
            }
        }
        else if(starti<=5){
            if(starti-endi==1&&jMove==0){
                canMove=true;
            }
            else if(endi-starti==0&&jMove==1){
                canMove=true;
            }
            else canMove=false;
        }
        else canMove=false;
        }
    else if(piece==ChessGridElement.RED_SERVANT){//红方仕
            int iMove=Math.abs(endi-starti);
            int jMove=Math.abs(endj-starti);
            if(endj>=4&&endj<=6&&endi>=8&&endi<=10&&iMove==1&&jMove==1){
                canMove=true;
            }
            else canMove=false;
        }
    else if(piece==ChessGridElement.RED_GENERAL){//红方帅
            int iMove=Math.abs(endi-starti);
            int jMove=Math.abs(endj-starti);
            if(endj>=4&&endj<=6&&endi>=8&&endi<=10) {
                if ((iMove == 1 && jMove == 0) || (iMove == 0 && jMove == 1)) {
                    canMove = true;
                } else canMove = false;
            }
            else canMove=false;
        }
    //黑方棋子
    else if(piece==ChessGridElement.BLACK_VEHICLE){ //黑方车
            if(starti==endi){
                int j=0;
                for(j=minj+1;j<=maxj-1;j++){
                    if(grid[starti][j]!= ChessGridElement.EMPTY){
                        canMove=false;
                        break;
                    }
                    if(j==maxj) canMove=true;
                }
            }
            else if(startj==endj){
                int i=0;
                for(i=mini+1;i<=maxi-1;i++){
                    if(grid[i][startj]!= ChessGridElement.EMPTY) {
                        canMove = false;
                        break;
                    }
                }
                if(i==maxi) canMove=true;
            }
        }
        else if(piece==ChessGridElement.BLACK_RIDER){ //黑方马
            int iMove=Math.abs(endi-starti);
            int jMove=Math.abs(endj-startj);
            if(iMove==2&&jMove==1){
                if(endi>starti){
                    if(grid[starti+1][startj]!=ChessGridElement.EMPTY){
                        canMove=false;
                    }
                    else{
                        canMove=true;
                    }
                }
                if(endi<starti){
                    if(grid[starti-1][startj]!=ChessGridElement.EMPTY){
                        canMove=false;
                    }
                    else{
                        canMove=true;
                    }
                }
            }
            else if(iMove==1&&jMove==2){
                if(endj>startj){
                    if(grid[starti][startj+1]!=ChessGridElement.EMPTY){
                        canMove=false;
                    }
                    else{
                        canMove=true;
                    }
                }
                if(endj<startj){
                    if(grid[starti][startj-1]!=ChessGridElement.EMPTY){
                        canMove=false;
                    }
                    else{
                        canMove=true;
                    }
                }
            }
            else canMove=false;
        }
        else if(piece==ChessGridElement.BLACK_MINISTER){//黑方相
            int Centeri=(starti+endi)/2;
            int Centerj=(startj+endj)/2;
            int iMove=Math.abs(endi-starti);
            int jMove=Math.abs(endj-startj);
            if(iMove==2&&jMove==2&&endi<=5){
                if(grid[Centeri][Centerj]!=ChessGridElement.EMPTY){
                    canMove=false;
                }
                else {
                    canMove=true;
                }
            }
            else canMove=false;
        }
        else if(piece==ChessGridElement.BLACK_CANNON){//黑方炮
            int num=0;
            if(starti==endi){
                int j=0;
                for(j=minj+1;j<=maxj-1;j++){
                    if(grid[starti][j]!=ChessGridElement.EMPTY){
                        num++;
                    }
                }
                if(num>1) {
                    canMove=false;
                }
                else if(num==1){
                    if(grid[endi][endj]!=ChessGridElement.EMPTY){
                        canMove=true;
                    }
                }
                else if(num==0&&grid[endi][endj]==ChessGridElement.EMPTY){
                    canMove=true;
                }
                else canMove=false;
            }
            else if(startj==endj){
                int i=0;
                for(i=mini+1;i<=maxi-1;i++){
                    if(grid[i][startj]!=ChessGridElement.EMPTY){
                        num++;
                    }
                }
                if(num>1) {
                    canMove=false;
                }
                else if(num==1){
                    if(grid[endi][endj]!=ChessGridElement.EMPTY){
                        canMove=true;
                    }
                }
                else if(num==0&&grid[endi][endj]==ChessGridElement.EMPTY){
                    canMove=true;
                }
                else canMove=false;
            }
            else canMove=false;
        }
        else if(piece==ChessGridElement.BLACK_SOLDIER){//黑方卒
            int iMove=Math.abs(endi-starti);
            int jMove=Math.abs(endj-starti);
            if(starti<=5){
                if(endi-starti==1&&jMove==0){
                    canMove=true;
                }
                else{
                    canMove=false;
                }
            }
            else if(starti>=6){
                if(endi-starti==1&&jMove==0){
                    canMove=true;
                }
                else if(endi-starti==0&&jMove==1){
                    canMove=true;
                }
                else canMove=false;
            }
            else canMove=false;
        }
        else if(piece==ChessGridElement.BLACK_SERVANT){//黑方侍
            int iMove=Math.abs(endi-starti);
            int jMove=Math.abs(endj-starti);
            if(endj>=4&&endj<=6&&endi>=1&&endi<=3&&iMove==1&&jMove==1){
                canMove=true;
            }
            else canMove=false;
        }
        else if(piece==ChessGridElement.BLACK_GENERAL){//黑方将
            int iMove=Math.abs(endi-starti);
            int jMove=Math.abs(endj-starti);
            if(endj>=4&&endj<=6&&endi>=1&&endi<=3) {
                if ((iMove == 1 && jMove == 0) || (iMove == 0 && jMove == 1)) {
                    canMove = true;
                } else canMove = false;
            }
            else canMove=false;
        }

    return canMove;
    }
}
