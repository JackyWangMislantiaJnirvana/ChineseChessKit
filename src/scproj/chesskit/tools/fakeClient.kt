package scproj.chesskit.tools

import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import scproj.chesskit.core.data.Coordinate
import scproj.chesskit.core.data.Movement
import scproj.chesskit.core.data.PlayerSide
import scproj.chesskit.core.data.serialize

fun main() {
    val client: HttpHandler = JavaHttpClient()

    println(
        client(
            Request(GET, "http://localhost:9000/observe")
        )
    )

    println(
        client(
            Request(POST, "http://localhost:9000/register/RED")
        )
    )
    println(
        client(
            Request(POST, "http://localhost:9000/register/BLACK")
        )
    )

    println(
        client(
            Request(POST, "http://localhost:9000/play/RED")
                .body(
                    serialize(
                        Movement(
                            PlayerSide.RED,
                            Coordinate(1, 2),
                            Coordinate(2, 2),
                            false
                        )
                    )
                )
        )
    )
    println(
        client(
            Request(POST, "http://localhost:9000/play/RED")
                .body(
                    serialize(
                        Movement(
                            PlayerSide.BLACK,
                            Coordinate(1, 2),
                            Coordinate(2, 2),
                            false
                        )
                    )
                )
        )
    )
    println(
        client(
            Request(POST, "http://localhost:9000/play/RED")
                .body(
                    serialize(
                        Movement(
                            PlayerSide.BLACK,
                            Coordinate(1, 2),
                            Coordinate(2, 2),
                            true
                        )
                    )
                )
        )
    )
}
