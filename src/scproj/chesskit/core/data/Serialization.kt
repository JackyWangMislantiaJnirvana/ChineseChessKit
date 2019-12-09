package scproj.chesskit.core.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

private class PlayerSideJsonAdapter {
    @FromJson
    fun fromJson(json: String): PlayerSide = when (json) {
        "RED" -> PlayerSide.RED
        "BLACK" -> PlayerSide.BLACK
        else -> throw JsonDataException("unknown player side: $json")
    }
    @ToJson
    fun toJson(playerSide: PlayerSide): String = playerSide.toString()
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(PlayerSideJsonAdapter())
    .build()

private val gameStatusAdapter = moshi.adapter(GameStatus::class.java)
private val movementAdapter = moshi.adapter(Movement::class.java)
private val registerStatusAdapter = moshi.adapter(RegisterStatus::class.java)

fun serialize(gameStatus: GameStatus): String = gameStatusAdapter.toJson(gameStatus)
fun serialize(movement: Movement): String = movementAdapter.toJson(movement)
fun serialize(registerStatus: RegisterStatus): String = registerStatusAdapter.toJson(registerStatus)

fun gameStatusDeserialize(json: String): GameStatus? = gameStatusAdapter.fromJson(json)
fun movementDeserialize(json: String): Movement? = movementAdapter.fromJson(json)
fun registerStatusDeserialize(json: String): RegisterStatus? = registerStatusAdapter.fromJson(json)

fun playerSideDeserialize(side: String?): PlayerSide? = when(side) {
    "RED" -> PlayerSide.RED
    "BLACK" -> PlayerSide.BLACK
    else -> null
}
