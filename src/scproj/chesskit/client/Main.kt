package scproj.chesskit.client

import javafx.geometry.Pos
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
                find<ChooseOfflineGameSideForm>().openModal()!!
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

class GameUI : View() {
    override val root = borderpane {
        //        center = TODO("WHAT ON EARTH SHOULD WE USE FOR IMAGE GRAPHICS")
//        bottom =
//        right =
    }
}

class ChooseOfflineGameSideForm : Fragment() {
    override val root = form {
        spacing = 10.0
        label("Choose a side to play")
        combobox<String> {
            items = listOf("Red side", "Black side").asObservable()
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
    override val root = form {
        label("Join an online game")
        fieldset {
            field("Address") {
                textfield()
            }
            field("Port") {
                textfield()
            }
        }
        hbox(alignment = Pos.BOTTOM_RIGHT) {
            button("Check")
        }
        spacer {
            spacing = 5.0
        }
        hbox(spacing = 5.0, alignment = Pos.BOTTOM_RIGHT) {
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
