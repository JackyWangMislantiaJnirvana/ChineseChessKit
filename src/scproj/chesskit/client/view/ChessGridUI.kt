package scproj.chesskit.client.view

import javafx.geometry.Point2D
import javafx.scene.effect.Bloom
import javafx.scene.input.MouseButton
import mu.KotlinLogging
import scproj.chesskit.client.*
import scproj.chesskit.core.chess.ChessGridElement
import scproj.chesskit.core.data.Coordinate
import scproj.chesskit.core.data.Movement
import tornadofx.View
import tornadofx.imageview
import tornadofx.pane

class ChessGridUI : View("My View") {
    val controller: GameController by inject()
    val logger = KotlinLogging.logger { }

    val chessPieces = ArrayList<ChessPieceImageViewProxy>()
    // size of chessboard_smaller.jpg
    val PANE_MIN_HEIGHT = 840.0
    val PANE_MIN_WIDTH = 770.0
    var selected: ChessPieceImageViewProxy? = null
    override val root = pane {
        minHeight = PANE_MIN_HEIGHT
        minWidth = PANE_MIN_WIDTH
        imageview("chessboard_smaller.jpg") {
            setOnMouseClicked {
                if (it.button == MouseButton.PRIMARY) {
                    if (selected != null) {
                        controller.move(
                            Movement(
                                player = controller.playerSide,
                                movingFrom = selected!!.gridCoordinate,
                                movingTo = pixelToGridCoordinate(Point2D(it.sceneX, it.sceneY)),
                                isUndo = false
                            )
                        )
                        logger.info { "Selection released by movement" }
                        selected!!.imageView.effect = null
                        selected = null
                    } else {
                        logger.info { "Not selected" }
                    }
                } else {
                    logger.info { "Selection released by right-clicking" }
                    selected!!.imageView.effect = null
                    selected = null
                }
            }
        }

        // Generals
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_GENERAL)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 4))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_GENERAL)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 4))
        )

        // Servants
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SERVANT)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 3))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SERVANT)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 5))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SERVANT)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 3))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SERVANT)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 5))
        )

        // Ministers
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_MINISTER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 2))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_MINISTER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 6))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_MINISTER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 2))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_MINISTER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 6))
        )

        // Riders
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_RIDER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 1))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_RIDER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 7))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_RIDER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 1))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_RIDER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 7))
        )

        // Vehicles
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_VEHICLE)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_VEHICLE)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(9, 8))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_VEHICLE)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_VEHICLE)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(0, 8))
        )

        // Cannons
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_CANNON)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(7, 1))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_CANNON)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(7, 7))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_CANNON)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(2, 1))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_CANNON)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(2, 7))
        )

        // Soldiers
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(6, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(6, 2))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(6, 4))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(6, 6))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(6, 8))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(3, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(3, 2))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(3, 4))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(3, 6))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy().move(Coordinate(3, 8))
        )

        chessPieces.forEach { piece ->
            piece.imageView.setOnMouseClicked {
                if (selected == null) {
                    selected = piece
                    piece.imageView.effect = Bloom(0.4)
                } else {
                    controller.move(
                        Movement(
                            player = controller.playerSide,
                            movingFrom = selected!!.gridCoordinate,
                            movingTo = piece.gridCoordinate,
                            isUndo = false
                        )
                    )
                }
            }
        }

        runAsync {
            while (true) {
                val observed = controller.observe()
                val gameStatus = observed.second
                if (gameStatus != null) {
                    val newMovements = gameStatus - controller.gameStatus
                    for (mov in newMovements) {
                        val target =
                            chessPieces.filter { it.gridCoordinate == mov.movingFrom }.getOrNull(0)
                        target?.move(mov.movingTo)
                    }
                    controller.gameStatus = gameStatus
                }
            }
        }
    }
}
