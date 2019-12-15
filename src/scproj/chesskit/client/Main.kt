package scproj.chesskit.client

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ToggleGroup
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import scproj.chesskit.client.view.GameUI
import scproj.chesskit.core.chess.Adapter
import scproj.chesskit.core.chess.Adapter1Mistake
import scproj.chesskit.core.chess.AdapterMoveList
import scproj.chesskit.core.chess.ChessGrid
import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.server.ServerModel
import scproj.chesskit.server.ServerStatus
import scproj.chesskit.server.logger
import tornadofx.*
import java.io.File
import java.net.InetAddress
import kotlin.math.min
import kotlin.system.exitProcess

class Main : App(EntranceUI::class)

class EntranceUI : View() {
    val botThread = BotClientThreadController()
    val serverThreadController = ServerThreadController()
    override val root = vbox(spacing = 10, alignment = Pos.CENTER) {
        minHeight = 400.0
        minWidth = 300.0
        label("Chess Kit")
        button("Offline Game") {
            action {
                serverThreadController.changeModel(
                    ServerModel(
                        serverStatus = ServerStatus.RED_IN_ACTION,
                        isRedOccupied = true,
                        isBlackOccupied = true
                    )
                )
                botThread.isRunning = true
                primaryStage.hide()
                find<GameUI>().openWindow()!!.setOnCloseRequest {
                    primaryStage.show()
                }
            }
            minWidth = 200.0
        }
        button("Create Online Game") {
            action {
                primaryStage.hide()
                find<CreateOnlineGameForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
            minWidth = 200.0
        }
        button("Join Online Game") {
            action {
                primaryStage.hide()
                find<JoinOnlineServerForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
            minWidth = 200.0
        }
        button("Load From Standard Format") {
            action {
                primaryStage.hide()
                find<LoadProvidedGameForm>().openModal()!!
                    .setOnCloseRequest { primaryStage.show() }
            }
            minWidth = 200.0
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
                controller.playerSide = selectedPlayer
            }
        }
        radiobutton("BLACK", playerSideToggleGroup) {
            isSelected = false
            action {
                selectedPlayer = PlayerSide.BLACK
                controller.playerSide = selectedPlayer
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
        button("Register") {
            action {
                controller.register()
            }
        }

        hbox(spacing = 5, alignment = Pos.BOTTOM_RIGHT) {
            button("OK") {
                action {
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
    var loaded = false
    val errorList = ArrayList<String>().asObservable()
    val serverThread: ServerThreadController by inject()
    val gameController: GameController by inject()
    var loadedGameStatus: GameStatus? = null
    var loadedChessGrid: ChessGrid? = null
    override val root = form {
        fieldset("Choose file to load") {
            field("Choose a chess board file:") {
                button("Browse") {
                    action {
                        chessbord =
                            chooseFile(
                                "Choose a chessboard",
                                arrayOf(FileChooser.ExtensionFilter("Chessboard", "*.*"))
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
                                arrayOf(FileChooser.ExtensionFilter("Moveseq", "*.*"))
                            ).firstOrNull()
                    }
                }
            }
            button("Load") {
                action {
                    if (chessbord != null /*&& moveseq != null*/) {
                        // Read file -> adapter resolve -> injection -> gameUI
                        val chessboardContent = chessbord!!.readLines()
                        val initialPlayerSide = when (getLastMover(chessboardContent)) {
                            PlayerSide.RED -> PlayerSide.BLACK
                            PlayerSide.BLACK -> PlayerSide.RED
                            else -> PlayerSide.RED
                        }
                        val adapter1Result = Adapter.adapter(chessboardContent.toTypedArray())
                        if (adapter1Result.mistake == Adapter1Mistake.Valid) {
                            val grid = adapter1Result.grid
                            if (moveseq != null) {
                                val moveseqContent = moveseq!!.readLines()
                                val adapter2Result =
                                    AdapterMoveList.AdapterMoveList(
                                        moveseqContent.toTypedArray(), grid, initialPlayerSide
                                    )
                                for (i in 0 until min(adapter2Result.mistake.size, adapter2Result.wrongLine.size)) {
                                    errorList.add(
                                        "${moveseq!!.name}@Line${adapter2Result.wrongLine[i]}: Error: ${adapter2Result.mistake[i]}"
                                    )
                                }
                                println(adapter1Result.grid)
                                println(adapter2Result.moveList)
                                loadedChessGrid = ChessGrid(adapter1Result.grid)
                                loadedGameStatus = GameStatus(
                                    adapter2Result.moveList,
                                    serialNumber = adapter2Result.moveList.size.toLong()
                                )
                            } else {
                                loadedChessGrid = ChessGrid(adapter1Result.grid)
                                loadedGameStatus = GameStatus(
                                    emptyList(),
                                    serialNumber = 0
                                )
                            }
                        } else {
                            alert(
                                type = Alert.AlertType.ERROR,
                                header = "Error loading chessboard!",
                                content = "At file ${chessbord!!.name}: ${adapter1Result.mistake}: ${adapter1Result.wrongMessage}"
                            )
                        }
                    }
                }
            }
        }

        fieldset("Error list") {
            listview(errorList)
        }
        fieldset("Start a new game local server on top of this") {
            button("Start!") {
                action {
                    if (loadedGameStatus != null && loadedChessGrid != null)
                        serverThread.changeModel(
                            ServerModel(
                                gameStatus = loadedGameStatus!!
                            )
                        )
                    gameController.chessGrid = loadedChessGrid!!
                    find<ChooseOnlinePlayerSideForm>().openWindow()!!.setOnCloseRequest {
                        primaryStage.show()
                    }
                    this@form.hide()
//                    close()
                }
            }
        }
    }
}