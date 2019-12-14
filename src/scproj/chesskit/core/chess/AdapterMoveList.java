package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.Movement;
import scproj.chesskit.core.data.PlayerSide;

import java.util.LinkedList;
import java.util.List;

public class AdapterMoveList {
    public static Adapter2Result AdapterMoveList(String[] args, ChessGridElement[][] grid) {
        List<Movement> moveList = new LinkedList<>();
        List<Adapter2Mistake> mistake = new LinkedList<>();
        List<Movement> wrongList = new LinkedList<>();
        List<Integer> wrongLine = new LinkedList<>();
        int i = 0;
        while (args[i].isEmpty() || args[i].charAt(0) < '1' || args[i].charAt(0) > '9') {
            i++;
        }
        int side = 0;
        while (i < args.length) {
            side = (side + 1) % 2;
            char[] c = args[i].toCharArray();
            int[] posi = new int[4];
            for (int j = 0; j < c.length; j++) {
                if (c[j] == ' ') {
                    posi[0]++;
                    posi[posi[0]] = j;
                }
            }
            int startj = Integer.parseInt(args[i].substring(0, posi[1]));
            int starti = Integer.parseInt(args[i].substring(posi[1] + 1, posi[2]));
            int endj = Integer.parseInt(args[i].substring(posi[2] + 1, posi[3]));
            int endi = Integer.parseInt(args[i].substring(posi[3] + 1, args[i].length()));
            if (side == 0) {
                starti--;
                endi--;
                startj--;
                endj--;
                if (Rule.movePieceMove(grid, grid[starti][startj], starti, startj, endi, endj)) {
                    grid[endi][endj] = grid[starti][startj];
                    grid[starti][startj] = ChessGridElement.EMPTY;
                    moveList.add(new Movement(PlayerSide.BLACK, starti, startj, endi, endj, false));
                } else {
                    mistake.add(Adapter2Rule.AdapterMoveListRule(grid, starti, startj, endi, endj, side));
                    wrongList.add(new Movement(PlayerSide.BLACK, starti, startj, endi, endj, false));
                    wrongLine.add(i);
                    side = (side + 1) % 2;
                }
            } else if (side == 1) {
                startj = 9 - startj;
                endj = 9 - endj;
                starti = 10 - starti;
                endi = 10 - endi;
                if (Rule.movePieceMove(grid, grid[starti][startj], starti, startj, endi, endj)) {
                    grid[endi][endj] = grid[starti][startj];
                    grid[starti][startj] = ChessGridElement.EMPTY;
                    moveList.add(new Movement(PlayerSide.RED, starti, startj, endi, endj, false));
                } else {
                    mistake.add(Adapter2Rule.AdapterMoveListRule(grid, starti, startj, endi, endj, side));
                    wrongList.add(new Movement(PlayerSide.RED, starti, startj, endi, endj, false));
                    wrongLine.add(i);
                    side = (side + 1) % 2;
                }
            }
            i++;
        }
        return new Adapter2Result(moveList, mistake, wrongList, wrongLine);
    }
}
