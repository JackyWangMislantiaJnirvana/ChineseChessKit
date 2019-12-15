package scproj.chesskit.server

import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.core.data.RegisterStatus
import scproj.chesskit.core.data.serialize
import scproj.chesskit.server.ServerStatus.WAITING_FOR_PLAYER
import java.security.InvalidParameterException

class ServerModel(
    var gameStatus: GameStatus = GameStatus(
        movementSequence = emptyList(),
        serialNumber = 0
    ),
    var serverStatus: ServerStatus = WAITING_FOR_PLAYER,
    private val initialReverts: Int = 3,
    isRedOccupied: Boolean = false,
    isBlackOccupied: Boolean = false
) {
    val revertChanceStatus = mutableMapOf(
        PlayerSide.BLACK to initialReverts,
        PlayerSide.RED to initialReverts
    )
    val gameRegistry = GameRegistry(isRedOccupied, isBlackOccupied)
}

class GameRegistry(isRedOccupied: Boolean = false, isBlackOccupied: Boolean = false) {
    private var registry = RegisterStatus(isRedOccupied, isBlackOccupied)

    val serializedRegistry
        get() = serialize(registry)

    fun getTheOther(playerSide: PlayerSide): Boolean =
        when (playerSide) {
            PlayerSide.RED -> registry.blackOccupied
            PlayerSide.BLACK -> registry.redOccupied
            else -> throw InvalidParameterException("TIE is not a valid player side")
        }

    fun checkOccupation(playerSide: PlayerSide): Boolean =
        when (playerSide) {
            PlayerSide.RED -> registry.redOccupied
            PlayerSide.BLACK -> registry.blackOccupied
            else -> throw InvalidParameterException("TIE is not a valid player side")
        }

    fun setOccupation(playerSide: PlayerSide, isOccupied: Boolean) {
        registry = when (playerSide) {
            PlayerSide.BLACK -> RegisterStatus(
                blackOccupied = isOccupied,
                redOccupied = registry.redOccupied
            )
            PlayerSide.RED -> RegisterStatus(
                redOccupied = isOccupied,
                blackOccupied = registry.blackOccupied
            )
            else -> throw InvalidParameterException("TIE is not a valid player side")
        }
    }
}