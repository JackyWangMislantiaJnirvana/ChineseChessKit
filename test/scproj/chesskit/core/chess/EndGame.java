package scproj.chesskit.core.chess;
import java.util.Scanner;
public class EndGame {
    public static void main(String[] args) {
        String[] Endgame=new String[11];
        Scanner scan=new Scanner(System.in);
        for(int i=0;i<=10;i++){
            Endgame[i]=scan.nextLine();
        }
        Adapter1Result result=Adapter.adapter(Endgame);
        ChessGrid chessGrid=new ChessGrid(result.grid);
        System.out.println(EndGameDetect.getWinner(chessGrid));
    }
}
