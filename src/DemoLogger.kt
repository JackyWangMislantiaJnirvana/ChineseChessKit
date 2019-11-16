import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
class FooWithLogging {
    val message = "world"
    fun bar() {
        logger.trace { "wow" }
        logger.debug { "wow" }
        logger.info { "wow" }
        logger.warn { "wow" }
        logger.error { "wow" }
    }
}

fun main() {
    FooWithLogging().bar()
}
