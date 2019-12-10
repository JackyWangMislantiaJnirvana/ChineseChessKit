package scproj.chesskit.client.view

import tornadofx.View
import tornadofx.borderpane
import tornadofx.center

class GameUI : View("My View") {
    val chessGridUI: ChessGridUI by inject()
    override val root = borderpane {
        center {
            chessGridUI
        }

    }
}
