package scproj.chesskit.client

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup
import javafx.scene.paint.Color
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import scproj.chesskit.client.view.GameUI
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.server.logger
import tornadofx.*
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
                find<ChoosePlayerSideForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
        }
        button("Create Online Game") {
            action {
                primaryStage.hide()
                find<CreateOnlineGameForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
        }
        button("Join Online Game") {
            action {
                primaryStage.hide()
                find<JoinOnlineServerForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
        }
        button("Game Replay") {
        }
        button("Quit") {
            action {
                exitProcess(0)
            }
        }
    }
}

class ChoosePlayerSideForm : Fragment() {
    val controller: GameController by inject()
    val chosenPlayerSide = SimpleStringProperty()
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
                    hide()
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
    override val root = form {
        spacing = 10.0
        label("Set up a server locally for others to join")
        fieldset {
            field("Server address") {
                textfield()
            }
            field("Port") {
                textfield()
            }
        }
        hbox(spacing = 5, alignment = Pos.BOTTOM_RIGHT) {
            button("OK")
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
    val port = SimpleIntegerProperty()
    val choosenSide = SimpleStringProperty()
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
                        find<ChoosePlayerSideForm>().openModal()!!.setOnCloseRequest {
                            primaryStage.show()
                        }
                        hide()
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
