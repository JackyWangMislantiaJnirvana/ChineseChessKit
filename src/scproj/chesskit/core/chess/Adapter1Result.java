package scproj.chesskit.core.chess;

public class Adapter1Result {
    public ChessGridElement[][] grid;
    public Adapter1Mistake mistake;
    public String wrongMessage;

    public Adapter1Result(ChessGridElement[][] grid, Adapter1Mistake mistake,String wrongMessage) {
        this.grid = grid;
        this.mistake = mistake;
        this.wrongMessage = wrongMessage;
    }

}
