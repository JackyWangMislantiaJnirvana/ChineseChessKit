import javafx.geometry.Point2D
import scproj.chesskit.client.ChessPieceImageViewProxy
import scproj.chesskit.client.pixelToGridCoordinate
import scproj.chesskit.core.chess.ChessGridElement
import tornadofx.View
import tornadofx.imageview
import tornadofx.pane

class DemoChessPiece : View("My View") {
    var chessPiece: ChessPieceImageViewProxy? = null
    override val root = pane {
        setMinSize(770.0, 840.0)
        imageview("chessboard_smaller.jpg") {
            setOnMouseClicked {
                //                chessPiece!!.move(chessPiece!!.pixelToGridCoordinate(Point2D(it.sceneX, it.sceneY)))
                println("x = ${it.sceneX}, y = ${it.sceneY}")
                val coordinate = pixelToGridCoordinate(Point2D(it.sceneX, it.sceneY))
                println("row = ${coordinate.positionX}, col = ${coordinate.positionY}")
                chessPiece!!.move(coordinate)
                println("chess at ${chessPiece!!.gridCoordinate}")
            }
        }
        chessPiece = ChessPieceImageViewProxy(imageview("red_general.png") {
            layoutX = -73.0
            layoutY = -73.0
        }, ChessGridElement.RED_GENERAL)
    }
}
