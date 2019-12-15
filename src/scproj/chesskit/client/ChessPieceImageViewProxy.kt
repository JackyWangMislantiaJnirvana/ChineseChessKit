package scproj.chesskit.client

import javafx.geometry.Point2D
import javafx.scene.image.ImageView
import javafx.util.Duration
import scproj.chesskit.core.chess.ChessGridElement
import scproj.chesskit.core.data.Coordinate
import tornadofx.move

val PIECE_HEIGHT: Double = 73.0
val PIECE_WIDTH: Double = 73.0

val rowPixelAnchors = listOf(
    27.0, 112.0, 200.0, 288.0, 387.0, 465.0, 555.0, 642.0, 731.0, 817.0
)
val columnPixelAnchors = listOf(
    22.0, 109.0, 200.0, 291.0, 383.0, 474.0, 565.0, 656.0, 747.0
)

fun gridToPixelCoordinate(coordinate: Coordinate): Point2D =
    Point2D(columnPixelAnchors[coordinate.positionY], rowPixelAnchors[coordinate.positionX])

fun pixelToGridCoordinate(point: Point2D): Coordinate {
    var coordinateX: Int = -1
    var coordinateY: Int = -1
    if (point.y <= rowPixelAnchors.first()) {
        coordinateX = 0
    } else if (point.y >= rowPixelAnchors.last()) {
        coordinateX = rowPixelAnchors.size - 1
    } else for (i in 0 until rowPixelAnchors.size - 1) {
        if (point.y >= rowPixelAnchors[i] && point.y <= rowPixelAnchors[i + 1]) {
            val intervalMiddlePoint = (rowPixelAnchors[i] + rowPixelAnchors[i + 1]) / 2.0
            if (point.y <= intervalMiddlePoint) {
                coordinateX = i
                break
            } else {
                coordinateX = i + 1
                break
            }
        }
    }

    if (point.x <= columnPixelAnchors.first()) {
        coordinateY = 0
    } else if (point.x >= columnPixelAnchors.last()) {
        coordinateY = columnPixelAnchors.size - 1
    } else for (i in 0 until columnPixelAnchors.size - 1) {
        if (point.x >= columnPixelAnchors[i] && point.x <= columnPixelAnchors[i + 1]) {
            val intervalMiddlePoint = (columnPixelAnchors[i] + columnPixelAnchors[i + 1]) / 2.0
            if (point.x <= intervalMiddlePoint) {
                coordinateY = i
                break
            } else {
                coordinateY = i + 1
                break
            }
        }
    }
    return Coordinate(coordinateX, coordinateY)
}

class ChessPieceImageViewProxy(val imageView: ImageView, val pieceType: ChessGridElement) {
    // Position (-1, -1) is where these image rest when game is not started
    val REST_POSITION = Coordinate(-1, -1)
    val DURATION = Duration(1000.0)
    var gridCoordinate = REST_POSITION
        get() = field
        private set(value) {
            field = value
        }

    fun move(dest: Coordinate): ChessPieceImageViewProxy {
        val clickedPixel = gridToPixelCoordinate(dest)
        val restOffsetX = PIECE_WIDTH
        val restOffsetY = PIECE_HEIGHT
        val bulkOffsetX = -PIECE_WIDTH / 2.0
        val bulkOffsetY = -PIECE_HEIGHT / 2.0
        val translateVector: Point2D =
            Point2D(clickedPixel.x + restOffsetX + bulkOffsetX, clickedPixel.y + restOffsetY + bulkOffsetY)

        gridCoordinate = dest
        imageView.move(DURATION, translateVector)

        return this
    }

    fun relocate(dest: Coordinate): ChessPieceImageViewProxy {
        val clickedPixel = gridToPixelCoordinate(dest)
        val restOffsetX = PIECE_WIDTH
        val restOffsetY = PIECE_HEIGHT
        val bulkOffsetX = -PIECE_WIDTH / 2.0
        val bulkOffsetY = -PIECE_HEIGHT / 2.0
        val translateVector: Point2D =
            Point2D(clickedPixel.x + restOffsetX + bulkOffsetX, clickedPixel.y + restOffsetY + bulkOffsetY)
        gridCoordinate = dest
        imageView.relocate(translateVector.x, translateVector.y)
        return this
    }
}

fun ImageView.getProxy(type: ChessGridElement): ChessPieceImageViewProxy {
    return ChessPieceImageViewProxy(this, type)
}