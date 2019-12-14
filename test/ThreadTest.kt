import scproj.chesskit.server.Server
import java.util.*
import kotlin.concurrent.thread

fun main() {
    val t = thread(start = true, name = "test") {
        Server().asRealServer().start()
    }
    val s = Scanner(System.`in`)
    s.next()
    println("stopping")
    t.interrupt()
}
