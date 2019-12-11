package scproj.chesskit.core.chess;

public class Adapter {
    public static ChessGridElement[][] adapter(String[] picture){
        ChessGridElement[][] grid=new ChessGridElement[10][9];
        int[] num=new int[14];                                          //收集棋子个数
        boolean t=false;                                                //判断是否有楚河汉界
        boolean LengthOk=false;                                          //判断长宽
        if(picture.length!=10) LengthOk=true;
        else for(int i=0;i<picture.length;i++){
            char[] element=picture[i].toCharArray();
            if(element.length!=9) {LengthOk=true;break;}
            for(int j=0;j<element.length;i++){
                if(element[j]=='.') grid[i][j]=ChessGridElement.EMPTY;
                else if(element[j]=='C') {num[0]++;grid[i][j]=ChessGridElement.BLACK_VEHICLE;}
                else if(element[j]=='H') {num[1]++;grid[i][j]=ChessGridElement.BLACK_RIDER;}
                else if(element[j]=='E') {num[2]++;grid[i][j]=ChessGridElement.BLACK_MINISTER;}
                else if(element[j]=='A') {num[3]++;grid[i][j]=ChessGridElement.BLACK_SERVANT;}
                else if(element[j]=='G') {num[4]++;grid[i][j]=ChessGridElement.BLACK_GENERAL;}
                else if(element[j]=='N') {num[5]++;grid[i][j]=ChessGridElement.BLACK_CANNON;}
                else if(element[j]=='S') {num[6]++;grid[i][j]=ChessGridElement.BLACK_SOLDIER;}
                else if(element[j]=='c') {num[7]++;grid[i][j]=ChessGridElement.RED_VEHICLE;}
                else if(element[j]=='h') {num[8]++;grid[i][j]=ChessGridElement.RED_RIDER;}
                else if(element[j]=='e') {num[9]++;grid[i][j]=ChessGridElement.RED_MINISTER;}
                else if(element[j]=='a') {num[10]++;grid[i][j]=ChessGridElement.RED_SERVANT;}
                else if(element[j]=='g') {num[11]++;grid[i][j]=ChessGridElement.RED_GENERAL;}
                else if(element[j]=='n') {num[12]++;grid[i][j]=ChessGridElement.RED_CANNON;}
                else if(element[j]=='s') {num[13]++;grid[i][j]=ChessGridElement.RED_SOLDIER;}
                else if(element[j]=='-'){
                    t=true;
                    break;
                }
            }
        }
        if(Invalid(num,grid)&&t&&LengthOk) {
            //抛出错误
            return null;
        }
        else return grid;
    }
    private static boolean Invalid(int[] num,ChessGridElement[][] grid){
        if(num[0]>2) return true;
        if(num[1]>2) return true;
        if(num[2]>2) return true;
        if(num[3]>2) return true;
        if(num[4]>1&&num[4]<=0) return true;
        if(num[5]>2) return true;
        if(num[6]>5) return true;
        if(num[7]>2) return true;
        if(num[8]>2) return true;
        if(num[9]>2) return true;
        if(num[10]>2) return true;
        if(num[11]>1&&num[11]<=0) return true;
        if(num[12]>2) return true;
        if(num[13]>5) return true;
        for(int i=0;i<=9;i++){
            for(int j=0;j<=8;j++){
                if(grid[i][j]==ChessGridElement.BLACK_SOLDIER&&
                        (i<=2||((i==3||i==4)&&(j==1||j==3||j==5||j==7)))) return true;
                if(grid[i][j]==ChessGridElement.BLACK_MINISTER&&
                        !((i==0&&j==2)||(i==0&&j==6)||(i==2&&j==0)||(i==2&&j==4)||(i==2&&j==8)||(i==4&&j==2)||(i==4&&j==6)))
                    return true;
                if(grid[i][j]==ChessGridElement.BLACK_SERVANT&&
                        !((i==0&&j==3)||(i==0&&j==5)||(i==1&&j==4)||(i==2&&j==3)||(i==2&&j==5))) return true;
                if(grid[i][j]==ChessGridElement.BLACK_GENERAL&&
                        !(i>=0&&i<=2&&j>=3&&j<=5))return true;
                if(grid[i][j]==ChessGridElement.RED_SOLDIER&&
                        (i>=7||((i==6||i==5)&&(j==1||j==3||j==5||j==7)))) return true;
                if(grid[i][j]==ChessGridElement.RED_MINISTER&&
                        !((i==9&&j==2)||(i==9&&j==6)||(i==7&&j==0)||(i==7&&j==4)||(i==7&&j==8)||(i==5&&j==2)||(i==5&&j==6)))
                    return true;
                if(grid[i][j]==ChessGridElement.RED_SERVANT&&
                        !((i==9&&j==3)||(i==9&&j==5)||(i==8&&j==4)||(i==7&&j==3)||(i==7&&j==5))) return true;
                if(grid[i][j]==ChessGridElement.RED_GENERAL&&
                        !(i<=9&&i>=7&&j>=3&&j<=5))return true;
            }
        }
    return false;
    }
}
