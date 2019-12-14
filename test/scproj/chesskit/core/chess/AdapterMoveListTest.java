package scproj.chesskit.core.chess;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
class AdapterMoveListTest {
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        String[] s=new String[87];
        for(int i=0;i<87;i++){
            s[i]=scan.nextLine();
        }
        Adapter2Result result=AdapterMoveList.AdapterMoveList(s);
        System.out.println();
    }
}