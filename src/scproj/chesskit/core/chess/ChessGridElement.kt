package scproj.chesskit.core.chess

import javafx.scene.image.Image

enum class ChessGridElement() {
    EMPTY,      // 空位

    // Red
    RED_GENERAL,    // 将
    RED_MINISTER,   // 相
    RED_SERVANT,    // 士
    RED_VEHICLE,    // 车
    RED_CANNON,     // 炮
    RED_RIDER,      // 马
    RED_SOLDIER,     // 兵

    // Black
    BLACK_GENERAL,    // 将
    BLACK_MINISTER,   // 相
    BLACK_SERVANT,    // 士
    BLACK_VEHICLE,    // 车
    BLACK_CANNON,     // 炮
    BLACK_RIDER,      // 马
    BLACK_SOLDIER     // 兵
}