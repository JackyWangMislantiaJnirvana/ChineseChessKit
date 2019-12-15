package scproj.chesskit.client.view

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.Font
import scproj.chesskit.client.GameController
import scproj.chesskit.client.Status
import scproj.chesskit.client.overrideFile
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.core.data.serialize
import tornadofx.*
import java.io.File
import kotlin.system.exitProcess

class GameUI : View("My View") {
    val chessGridUI: ChessGridUI by inject()
    val statusIndicator: StatusIndicator = StatusIndicator()
    val gameController: GameController by inject()
    val statusBar: StatusBar = StatusBar(gameController.statusBarText)
    override val root = borderpane {
        center = chessGridUI.root

        right = borderpane {
            top = statusIndicator.root
            center = form {
                label("Playing: ${gameController.gameMode}")
                label("Side: ${gameController.playerSide}")
            }
            bottom {
                vbox(spacing = 10, alignment = Pos.CENTER) {
                    button("Save Online Game") {
                        minWidth = 150.0
                        action {
                            find<SaveOnlineGameForm>().openModal()
                        }
                    }
                    button("Revert") {
                        minWidth = 150.0
                        action {
                            gameController.revert()
                        }
                    }
                    button("Exit") {
                        minWidth = 150.0
                        action {
                            exitProcess(0)
                        }
                    }
                    spacer {
                        minHeight = 20.0
                    }
                }
            }
        }

        bottom = statusBar.root
    }
}

class SaveOnlineGameForm : Fragment() {
    val gameController: GameController by inject()
    val filename = SimpleStringProperty()
    var chosenDir: File? = null
    override val root = form {
        fieldset("Save the online game locally") {
            label("You can load this file into your own server, later.")
            button("Browse directory...") {
                action {
                    chosenDir = chooseDirectory(
                        title = "Choose a path to save the game"
                    )
                }
            }
            field("Filename") {
                textfield(filename)
            }
            button("Save") {
                action {
                    if (chosenDir != null) {
                        overrideFile(
                            chosenDir!!, filename.value,
                            serialize(gameController.gameStatus)
                        )
                    }
                    close()
                }
            }
        }
    }
}

class StatusIndicator : Fragment() {
    val gameController: GameController by inject()
    override val root = pane {
        minWidth = 220.0
        minHeight = 70.0
        imageview("red.png") {
            layoutX = 25.0
            layoutY = 50.0
        }
        imageview("yellow.png") {
            layoutX = 85.0
            layoutY = 50.0
        }
        imageview("green.png") {
            layoutX = 145.0
            layoutY = 50.0
        }
        val redShadow = imageview("grey.png") {
            layoutX = 25.0
            layoutY = 50.0
        }
        val yellowShadow = imageview("grey.png") {
            layoutX = 85.0
            layoutY = 50.0
        }
        val greenShadow = imageview("grey.png") {
            layoutX = 145.0
            layoutY = 50.0
        }

        redShadow.isVisible = true
        yellowShadow.isVisible = gameController.playerSide == PlayerSide.RED
        greenShadow.isVisible = gameController.playerSide != PlayerSide.RED

        gameController.statusSubscribers.add { status ->
            when (status) {
                Status.ACTION -> {
                    redShadow.isVisible = true
                    yellowShadow.isVisible = true
                    greenShadow.isVisible = false
                }
                Status.WAITING -> {
                    redShadow.isVisible = true
                    yellowShadow.isVisible = false
                    greenShadow.isVisible = true
                }
                Status.GAME_OVER -> {
                    redShadow.isVisible = false
                    yellowShadow.isVisible = true
                    greenShadow.isVisible = true
                }
                else -> {
                    redShadow.isVisible = false
                    yellowShadow.isVisible = true
                    greenShadow.isVisible = true
                }
            }
        }
    }
}

class StatusBar(tip: SimpleStringProperty = SimpleStringProperty("Am I Cool?")) : Fragment() {
    val gameController: GameController by inject()
    override val root = hbox {
        style {
            backgroundColor += Color.DODGERBLUE
        }
        gameController.statusSubscribers.add { status ->
            Platform.runLater {
                when (status) {
                    Status.ACTION -> {
                        style {
                            backgroundColor += Color.DODGERBLUE
                        }
                        tip.value = "Your turn."
                    }
                    Status.WAITING -> {
                        style {
                            backgroundColor += Color.CORAL
                        }
                        tip.value = "Waiting for your opponent."
                    }
                    Status.GAME_OVER -> {
                        style {
                            backgroundColor += Color.BLUEVIOLET
                        }
                        tip.value = "" // TODO
                    }
                    else -> {
                    }
                }
            }
        }
        label(tip) {
            style {
                textFill = Color.WHITE
                font = Font(14.0)
            }
        }
    }
}