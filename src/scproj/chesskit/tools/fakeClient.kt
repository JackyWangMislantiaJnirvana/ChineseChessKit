package scproj.chesskit.tools

import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import scproj.chesskit.core.communication.MOVEMENT_SUCCESS
import scproj.chesskit.core.communication.means
import scproj.chesskit.core.data.Coordinate
import scproj.chesskit.core.data.Movement
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.core.data.serialize

fun main() {
    val client: HttpHandler = JavaHttpClient()
    val networkResponse: Response = client(
        Request(POST, "http://localhost:9000/play/RED")
            .body(
                serialize(
                    Movement(
                        PlayerSide.RED,
                        Coordinate(1, 2),
                        Coordinate(2, 2),
                        false)
                )
            )
    )
    println(networkResponse)
}
