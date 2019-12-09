package scproj.chesskit.client

import tornadofx.View
import tornadofx.pane

class ChessGridUI : View("My View") {
    val controller: GameController by inject()
    override val root = pane {
    }
}
