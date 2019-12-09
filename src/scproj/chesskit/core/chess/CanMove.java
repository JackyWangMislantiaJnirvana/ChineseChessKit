package scproj.chesskit.core.chess;

public class CanMove {
    public ChessGridElement entry;
    public int starti;
    public int startj;
    public int endi;
    public int endj;

    public CanMove(ChessGridElement entry, int starti, int startj, int endi, int endj) {
        this.entry = entry;
        this.starti = starti;
        this.startj = startj;
        this.endi = endi;
        this.endj = endj;
    }
}
