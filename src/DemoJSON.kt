import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import mu.KotlinLogging
import com.squareup.moshi.JsonDataException
import javax.smartcardio.Card
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

fun main() {
    val jsonString = """
        {
        "url": "https://api.github.com/repos/square/okio/issues/156",
        "id": 91393390,
        "number": 156,
        "title": "ByteString CharSequence idea",
        "state": "OPEN",
        "created_at": "2015-06-27T00:49:40.000Z",
        "body": "Let's make CharSequence that's backed by bytes.\n"
        }
    """
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
            // Ahhhh, reflect magic!!!!
        .add(StateAdapter())
        .build()
    val issueAdapter = moshi.adapter(Issue::class.java)

    val logger = KotlinLogging.logger {}
    logger.debug { System.currentTimeMillis() }
    val issue = issueAdapter.fromJson(jsonString)
    logger.debug { System.currentTimeMillis() }
    println(issue)
}

enum class State {
    OPEN, CLOSED
}

data class Issue(
    val url: String,
    val id: Long,
    val number: Long,
    val title: String,
    val state: State,
    @Json(name = "created_at") val createdAt: String,
    val body: String
)

class StateAdapter {
    @ToJson
    fun toJson(state: State): String = state.toString()

    @FromJson
    fun fromJson(state: String): State = when (state) {
        "OPEN" -> State.OPEN
        "CLOSED" -> State.CLOSED
        else -> throw JsonDataException("unknown state: $state")
    }
}
