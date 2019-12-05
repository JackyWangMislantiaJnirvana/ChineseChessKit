package scproj.chesskit.server

import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.server.ServerStatus.WAITING_FOR_PLAYER

class ServerModel(
    var gameStatus: GameStatus,
    var serverStatus: ServerStatus = WAITING_FOR_PLAYER,
    private val initialReverts: Int = 3
) {
    val revertChanceStatus = mutableMapOf(
        PlayerSide.BLACK to initialReverts,
        PlayerSide.RED to initialReverts
    )
    val gameRegistry = GameRegistry()
}

class GameRegistry {
    private val registry = mutableMapOf(
        PlayerSide.BLACK to false,
        PlayerSide.RED to false
    )

    fun getTheOther(playerSide: PlayerSide): Boolean =
        when (playerSide) {
            PlayerSide.RED -> registry[PlayerSide.BLACK]
            PlayerSide.BLACK -> registry[PlayerSide.RED]
        }!!

    fun checkOccupation(playerSide: PlayerSide): Boolean =
        when (playerSide) {
            PlayerSide.RED -> registry[PlayerSide.BLACK]
            PlayerSide.BLACK -> registry[PlayerSide.RED]
        }!!

    fun setOccupation(playerSide: PlayerSide) {
        registry[playerSide] = true
    }
}