package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.Movement;

import java.util.List;

public class Adapter2Result {
    public List<Movement> moveList;
    public List<Adapter2Mistake> mistake;
    public List<Movement> wrongList;
    public List<Integer> wrongLine;

    public Adapter2Result(List<Movement> moveList, List<Adapter2Mistake> mistake, List<Movement> wrongList, List<Integer> wrongLine) {
        this.moveList = moveList;
        this.mistake = mistake;
        this.wrongList = wrongList;
        this.wrongLine = wrongLine;
    }
}
