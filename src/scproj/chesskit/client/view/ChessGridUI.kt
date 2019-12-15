package scproj.chesskit.client.view

import javafx.application.Platform
import javafx.geometry.Point2D
import javafx.scene.effect.Bloom
import javafx.scene.input.MouseButton
import mu.KotlinLogging
import scproj.chesskit.client.*
import scproj.chesskit.core.chess.ChessGridElement
import scproj.chesskit.core.communication.*
import scproj.chesskit.core.data.Coordinate
import scproj.chesskit.core.data.Movement
import scproj.chesskit.core.data.PlayerSide
import tornadofx.View
import tornadofx.imageview
import tornadofx.pane
import tornadofx.whenDocked

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
                        selected?.imageView?.effect = null
                        selected = null
                    } else {
                        logger.info { "Not selected" }
                    }
                } else {
                    logger.info { "Selection released by right-clicking" }
                    selected?.imageView?.effect = null
                    selected = null
                }
            }
        }

        // Generals
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_GENERAL)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_GENERAL)//.move(Coordinate(9, 4))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_GENERAL)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_GENERAL)//.move(Coordinate(0, 4))
        )

        // Servants
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SERVANT)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_SERVANT)//.move(Coordinate(9, 3))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SERVANT)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_SERVANT)//.move(Coordinate(9, 5))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SERVANT)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_SERVANT)//.move(Coordinate(0, 3))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SERVANT)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_SERVANT)//.move(Coordinate(0, 5))
        )

        // Ministers
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_MINISTER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_MINISTER)//.move(Coordinate(9, 2))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_MINISTER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_MINISTER)//.move(Coordinate(9, 6))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_MINISTER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_MINISTER)//.move(Coordinate(0, 2))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_MINISTER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_MINISTER)//.move(Coordinate(0, 6))
        )

        // Riders
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_RIDER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_RIDER)//.move(Coordinate(9, 1))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_RIDER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_RIDER)//.move(Coordinate(9, 7))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_RIDER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_RIDER)//.move(Coordinate(0, 1))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_RIDER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_RIDER)//.move(Coordinate(0, 7))
        )

        // Vehicles
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_VEHICLE)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_VEHICLE)//.move(Coordinate(9, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_VEHICLE)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_VEHICLE)//.move(Coordinate(9, 8))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_VEHICLE)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_VEHICLE)//.move(Coordinate(0, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_VEHICLE)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_VEHICLE)//.move(Coordinate(0, 8))
        )

        // Cannons
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_CANNON)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_CANNON)//.move(Coordinate(7, 1))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_CANNON)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_CANNON)//.move(Coordinate(7, 7))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_CANNON)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_CANNON)//.move(Coordinate(2, 1))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_CANNON)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_CANNON)//.move(Coordinate(2, 7))
        )

        // Soldiers
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_SOLDIER)//.move(Coordinate(6, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_SOLDIER)//.move(Coordinate(6, 2))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_SOLDIER)//.move(Coordinate(6, 4))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_SOLDIER)//.move(Coordinate(6, 6))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.RED_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.RED_SOLDIER)//.move(Coordinate(6, 8))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_SOLDIER)//.move(Coordinate(3, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_SOLDIER)//.move(Coordinate(3, 0))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_SOLDIER)//.move(Coordinate(3, 4))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_SOLDIER)//.move(Coordinate(3, 6))
        )
        chessPieces.add(
            imageview(getIcon(ChessGridElement.BLACK_SOLDIER)) {
                layoutX = -PIECE_WIDTH
                layoutY = -PIECE_HEIGHT
            }.getProxy(ChessGridElement.BLACK_SOLDIER)//.move(Coordinate(3, 8))
        )

        chessPieces.forEach { piece ->
            piece.imageView.setOnMouseClicked {
                if (selected == null) {
                    selected = piece
                    piece.imageView.toFront()
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
                    logger.info { "Selection released by attacking" }
                    selected!!.imageView.effect = null
                    selected = null
                }
            }
        }

        whenDocked {
            logger.debug { "ChessGridUI docked!" }
            val chessGridMatrix = controller.chessGrid.grid
            val tempChessList = chessPieces.toMutableList()
            for (i in chessGridMatrix.indices) {
                for (j in chessGridMatrix[i].indices) {
                    if (chessGridMatrix[i][j] != ChessGridElement.EMPTY) {
                        for (piece in tempChessList) {
                            if (chessGridMatrix[i][j] == piece.pieceType) {
                                piece.move(Coordinate(i, j))
                                tempChessList.remove(piece)
                                break
                            }
                        }
                    }
                }
            }
        }

        runAsync {
            while (true) {
                val observed = controller.observeGame()
                val status = observed.first
                val gameStatus = observed.second

                if (status means RED_WON
                    || status means BLACK_WON
                    || status means TIE
                ) {
                    controller.status = Status.GAME_OVER
                    Platform.runLater {
                        when {
                            status means RED_WON -> controller.statusBarText.value = "Game over, RED won."
                            status means BLACK_WON -> controller.statusBarText.value = "Game over, BLACK won."
                            status means TIE -> controller.statusBarText.value = "Game over, TIE."
                        }
                    }
                    break
                }
                when {
                    status means RED_IN_ACTION -> when (controller.playerSide) {
                        PlayerSide.RED -> controller.status = Status.ACTION
                        PlayerSide.BLACK -> controller.status = Status.WAITING
                    }
                    status means BLACK_IN_ACTION -> when (controller.playerSide) {
                        PlayerSide.RED -> controller.status = Status.WAITING
                        PlayerSide.BLACK -> controller.status = Status.ACTION
                    }
                }

                if (gameStatus != null) {
//                    val newMovements = gameStatus.movementSequence - controller.gameStatus.movementSequence
                    val newMovements = gameStatus.movementSequence.subList(
                        controller.gameStatus.movementSequence.size, gameStatus.movementSequence.size
                    )
//                    logger.debug {
//                        """gameStatus = $gameStatus
//                            |controller.gameStatus = ${controller.gameStatus}
//                        """.trimMargin()
//                    }
                    if (newMovements.isNotEmpty()) {
                        for (mov in newMovements) {
                            if (mov.isUndo) {
                                val maskedMovementSequence =
                                    gameStatus.movementSequence.subList(0, gameStatus.movementSequence.size - 3)
                                chessPieces.forEach {
                                    it.imageView.isVisible = true
                                }
                                for (m in maskedMovementSequence) {
                                    val target =
                                        chessPieces.filter { it.gridCoordinate == m.movingFrom && it.imageView.isVisible }
                                            .getOrNull(0)
                                    chessPieces.filter { it.gridCoordinate == m.movingFrom && it.imageView.isVisible }
                                        .forEach(::println)
                                    val enemy =
                                        chessPieces.filter { it.gridCoordinate == m.movingTo && it.imageView.isVisible }
                                            .getOrNull(0)
                                    chessPieces.filter { it.gridCoordinate == m.movingTo && it.imageView.isVisible }
                                        .forEach(::println)
                                    target?.move(m.movingTo)
                                    enemy?.imageView?.isVisible = false
                                }
                            } else {
                                val target =
                                    chessPieces.filter { it.gridCoordinate == mov.movingFrom && it.imageView.isVisible }
                                        .getOrNull(0)
                                chessPieces.filter { it.gridCoordinate == mov.movingFrom && it.imageView.isVisible }
                                    .forEach(::println)
                                val enemy =
                                    chessPieces.filter { it.gridCoordinate == mov.movingTo && it.imageView.isVisible }
                                        .getOrNull(0)
                                chessPieces.filter { it.gridCoordinate == mov.movingTo && it.imageView.isVisible }
                                    .forEach(::println)
                                target?.move(mov.movingTo)
                                enemy?.imageView?.isVisible = false
                                // just for fun
//                        Thread.sleep(1000)
                            }
                        }
                        controller.updateGameStatus(gameStatus)
                    }
                }
                // TODO Consider dialing this time smaller?
                Thread.sleep(500)
            }
        }
    }
}
