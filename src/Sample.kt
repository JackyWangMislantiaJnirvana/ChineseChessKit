class Sample(var number: Int) {
    fun sayHello() = println("hello")
    fun callingLambda(l: () -> Unit) = l()

    val isEven: Boolean
        get() = number % 2 == 0
}

fun main() {
    val s = Sample(2);
    println(s.isEven)
}
