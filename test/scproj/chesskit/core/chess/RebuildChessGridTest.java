package scproj.chesskit.core.chess;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static scproj.chesskit.core.data.SerializationKt.gameStatusDeserialize;

class RebuildChessGridTest {

    @Test
    void rebuildChessGrid() {
        System.out.println(Arrays.deepToString(RebuildChessGrid.rebuildChessGrid(gameStatusDeserialize("{\"movement_sequence\":[{\"player\":\"RED\",\"moving_from\":{\"position_x\":6,\"position_y\":4},\"moving_to\":{\"position_x\":5,\"position_y\":4},\"is_undo\":false}],\"serial_number\":1}"))));
    }
}