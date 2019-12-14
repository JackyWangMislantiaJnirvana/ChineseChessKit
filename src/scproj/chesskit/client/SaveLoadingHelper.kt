package scproj.chesskit.client

import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.gameStatusDeserialize
import java.io.File

fun loadGameStatusFromFile(file: File): GameStatus? = gameStatusDeserialize(file.readText())
