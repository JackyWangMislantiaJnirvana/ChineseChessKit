package scproj.chesskit.core.chess;

import java.util.Scanner;

class AdapterMoveListTest {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String[] s = new String[87];
        for (int i = 0; i < 87; i++) {
            s[i] = scan.nextLine();
        }
        Adapter1Result result1 = Adapter.adapter(new String[]);
        Adapter2Result result2 = AdapterMoveList.AdapterMoveList(s, result1.grid);
        System.out.println();
    }
}