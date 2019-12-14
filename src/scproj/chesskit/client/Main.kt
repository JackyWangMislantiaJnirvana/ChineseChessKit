package scproj.chesskit.client

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import scproj.chesskit.client.view.GameUI
import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.server.ServerModel
import scproj.chesskit.server.logger
import tornadofx.*
import java.io.File
import java.net.InetAddress
import kotlin.system.exitProcess

class Main : App(EntranceUI::class)

class EntranceUI : View() {
    override val root = vbox(spacing = 10, alignment = Pos.CENTER) {
        minHeight = 400.0
        minWidth = 300.0
        label("Chess Kit")
        button("Offline Game") {
            action {
                primaryStage.hide()
                find<ChooseOnlinePlayerSideForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
            minWidth = 150.0
        }
        button("Create Online Game") {
            action {
                primaryStage.hide()
                find<CreateOnlineGameForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
            minWidth = 150.0
        }
        button("Join Online Game") {
            action {
                primaryStage.hide()
                find<JoinOnlineServerForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
            minWidth = 150.0
        }
        button("Game Replay") {
            action {
                // TODO
            }
            minWidth = 150.0
        }
        button("Quit") {
            action {
                exitProcess(0)
            }
            minWidth = 150.0
        }
    }
}

class ChooseOnlinePlayerSideForm : Fragment() {
    val controller: GameController by inject()
    //    val chosenPlayerSide = SimpleStringProperty()
    val redStatusString = SimpleStringProperty()
    val blackStatusString = SimpleStringProperty()
    override val root = form {
        spacing = 10.0
        label("Choose a side to play")
        label(redStatusString) {
            textFill = Color.RED
        }
        label(blackStatusString) {
            textFill = Color.BLUE
        }

        val playerSideToggleGroup = ToggleGroup()
        var selectedPlayer = PlayerSide.RED
        radiobutton("RED", playerSideToggleGroup) {
            isSelected = true
            action {
                selectedPlayer = PlayerSide.RED
            }
        }
        radiobutton("BLACK", playerSideToggleGroup) {
            isSelected = false
            action {
                selectedPlayer = PlayerSide.BLACK
            }
        }

        button("Refresh") {
            action {
                val registerStatus = controller.observeRegistration()
                if (registerStatus != null) {
                    if (registerStatus.redOccupied) {
                        redStatusString.value = "Red side is occupied."
                    } else {
                        redStatusString.value = "Red side is vacant!"
                    }
                    if (registerStatus.blackOccupied) {
                        blackStatusString.value = "Black side is occupied."
                    } else {
                        blackStatusString.value = "Black side is vacant!"
                    }
                }
            }
        }

        hbox(spacing = 5, alignment = Pos.BOTTOM_RIGHT) {
            button("OK") {
                action {
                    // TODO check if register is successful
                    controller.playerSide = selectedPlayer
                    controller.register()
                    find<GameUI>().openWindow()!!.setOnCloseRequest {
                        primaryStage.show()
                    }
                    this@form.hide()
                }
            }
            button("Cancel") {
                action {
                    close()
                    primaryStage.show()
                }
            }
        }
    }
}

class CreateOnlineGameForm : Fragment() {
    val gameController: GameController by inject()
    val port = SimpleIntegerProperty(9000)
    var fileToLoad: File? = null
    val serverThreadController: ServerThreadController by inject()
    override val root = form {
        spacing = 10.0
        label("Set up a server locally for others to join")
        fieldset {
            field("Server address") {
                textfield(InetAddress.getLocalHost().hostAddress)
            }
//            field("Port") {
//                textfield(9000)
//            }
            field("Load from a save (if you want)") {
                button("Load") {
                    action {
                        fileToLoad = chooseFile(
                            title = "Choosing Game Saves to Load",
                            filters = arrayOf(FileChooser.ExtensionFilter("JSON Serialized Game Status", "*.json"))
                        ).firstOrNull()
                    }
                }
            }
        }
        hbox(spacing = 5, alignment = Pos.BOTTOM_RIGHT) {
            button("OK") {
                action {
                    if (fileToLoad != null) {
                        val loaded = loadGameStatusFromFile(fileToLoad!!)
                        if (loaded != null) {
                            serverThreadController.changeModel(
                                ServerModel(
                                    gameStatus = loaded
                                )
                            )
                        }
                    } else {
                        serverThreadController.changeModel(
                            ServerModel(
                                gameStatus = GameStatus(
                                    emptyList(), 0
                                )
                            )
                        )
                    }

                    gameController.serverURL = "http://localhost:9000"

                    find<ChooseOnlinePlayerSideForm>().openWindow()!!.setOnCloseRequest {
                        primaryStage.show()
                    }
                    this@form.hide()
                }
            }
            button("Cancel") {
                action {
                    close()
                    primaryStage.show()
                }
            }
        }
    }
}

class JoinOnlineServerForm : Fragment() {
    val address = SimpleStringProperty()
    val port = SimpleIntegerProperty(9000)
    val chosenSide = SimpleStringProperty()
    val controller: GameController by inject()
    var addressValid = false
    override val root = form {
        label("Join an online game")
        fieldset {
            field("Address") {
                textfield(address)
            }
            field("Port") {
                textfield(port)
            }
//            field("Choose a side") {
//                combobox<String>(choosenSide) {
//                    items = listOf("Red side", "Black side").asObservable()
//                }
//            }
        }
        hbox(alignment = Pos.BOTTOM_RIGHT) {
            button("Test") {
                action {
                    var res: Response? = null
                    try {
                        res =
                            controller.httpClient(Request(Method.GET, "http://${address.get()}:${port.get()}/observe"))
                    } catch (e: Exception) {
                        logger.debug { "Failed to reach server\n$e" }
                    }
                    if (res?.status?.code == 200) {
                        text = "Test OK"
                        addressValid = true
                    } else {
                        text = "Test FAIL"
                        addressValid = false
                    }
                }
            }
        }
        spacer {
            spacing = 5.0
        }
        hbox(spacing = 5.0, alignment = Pos.BOTTOM_RIGHT) {
            button("OK") {
                action {
                    if (addressValid) {
                        controller.serverURL = "http://${address.get()}:${port.get()}"
                        find<ChooseOnlinePlayerSideForm>().openModal()!!.setOnCloseRequest {
                            primaryStage.show()
                        }
                        this@form.hide()
                    }
                }
            }
            button("Cancel") {
                action {
                    close()
                    primaryStage.show()
                }
            }
        }
        label("Please press \"Test\" first to check connectivity")
    }
}

class LoadProvidedGameForm : Fragment() {
    var chessbord: File? = null
    var moveseq: File? = null
    override val root = form {
        field("Choose a chess board file:") {
            button("Browse") {
                action {
                    chessbord =
                        chooseFile(
                            "Choose a chessboard",
                            arrayOf(FileChooser.ExtensionFilter("Chess", "*"))
                        ).firstOrNull()
                }
            }
        }
        field("Choose a moveseq file:") {
            button("Browse") {
                action {
                    moveseq =
                        chooseFile(
                            "Choose a chessboard",
                            arrayOf(FileChooser.ExtensionFilter("Chess", "*"))
                        ).firstOrNull()
                }
            }
        }
    }
}