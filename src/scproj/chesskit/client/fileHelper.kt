package scproj.chesskit.client

import scproj.chesskit.core.data.GameStatus
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.core.data.gameStatusDeserialize
import java.io.File

fun loadGameStatusFromFile(file: File): GameStatus? = gameStatusDeserialize(file.readText())
fun readAndSplitLines(file: File) = file.readLines()
fun getLastMover(lines: List<String>): PlayerSide? {
    val sideString = lines.filter { it.startsWith("@") }
        .filter { it.contains("LAST_MOVER=") }
        .map { it.substring(it.indexOf("=")..it.length - 1).trim() }
        .getOrNull(0)
    return when (sideString) {
        "RED" -> PlayerSide.RED
        "BLACK" -> PlayerSide.BLACK
        else -> null
    }
}

fun overrideFile(dir: File, filename: String, content: String) {
    dir.resolve(filename).writeText(content)
}