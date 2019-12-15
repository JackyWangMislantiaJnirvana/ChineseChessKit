import kotlin.concurrent.thread

fun main() {
    var a = true
    thread(start = true) {
        while (true) {
            println("wow, ${a}")
            Thread.sleep(3000)
        }
    }
    readLine()
    println("Stopping")
    a = false
    readLine()
    println("Starting")
    a = true
}

class A {
    var a = true
}