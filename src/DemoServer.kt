import mu.KotlinLogging
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.CachingFilters
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    // we can bind HttpHandlers (which are just functions from  Request -> Response) to paths/methods to create a Route,
    // then combine many Routes together to make another HttpHandler
    val logger = KotlinLogging.logger {  }
    var requestCounter = 0
    val app: HttpHandler = routes(
        "/ping" bind GET to { _: Request -> Response(OK).body("pong!") },
        "/greet/{name}" bind GET to { req: Request ->
            val name: String? = req.path("name")
            requestCounter++
            logger.info { "req #$requestCounter" }
            Response(OK).body("hello ${name ?: "anon!"}")
        }
    )

    // call the handler in-memory without spinning up a server
    val inMemoryResponse: Response = app(Request(GET, "/greet/Bob"))
    println(inMemoryResponse)

// Produces:
//    HTTP/1.1 200 OK
//
//
//    hello Bob

    // this is a Filter - it performs pre/post processing on a request or response
    val timingFilter = Filter {
            next: HttpHandler ->
        {
                request: Request ->
            val start = System.currentTimeMillis()
            val response = next(request)
            val latency = System.currentTimeMillis() - start
            println("Request to ${request.uri} took ${latency}ms")
            response
        }
    }

    // we can "stack" filters to create reusable units, and then apply them to an HttpHandler
    val compositeFilter = CachingFilters.Response.NoCache().then(timingFilter).then(
        Filter { next ->
            { request ->
                println("me!")
                next(request)
            }
        }
    )
    val filteredApp: HttpHandler = compositeFilter.then(app)

    // only 1 LOC to mount an app and start it in a container
    filteredApp.asServer(Jetty(9000)).start()

    // HTTP clients are also HttpHandlers!
    val client: HttpHandler = JavaHttpClient()

    val networkResponse: Response = client(Request(GET, "http://localhost:9000/greet/Bob"))
    println(networkResponse.bodyString())

// Produces:
//    Request to /api/greet/Bob took 1ms
//    HTTP/1.1 200
//    cache-control: private, must-revalidate
//    content-length: 9
//    date: Thu, 08 Jun 2017 13:01:13 GMT
//    expires: 0
//    server: Jetty(9.3.16.v20170120)
//
//    hello Bob
}