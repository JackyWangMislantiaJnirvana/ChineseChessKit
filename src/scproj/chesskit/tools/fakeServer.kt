package scproj.chesskit.tools

import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes

class DEMOServerModel() {
    var status = 1
}

fun main() {
    val serverModel = DEMOServerModel()

    val app: HttpHandler = routes(
        "/" bind Method.POST to { request: Request ->
            checkRequirementOne(serverModel).then(checkRequirementTwo(serverModel)).then(handleItSimply(serverModel))
                .invoke(request)
        }
    )

    println(app(Request(Method.POST, "/").body("1")))
}

fun checkRequirementOne(serverModel: DEMOServerModel): Filter =
    Filter { handler: HttpHandler ->
        { request: Request ->
            println("valid filter 1, at your service")
            println("Changing status to 2")
            if (serverModel.status.toString() == request.bodyString())
                handler(request)
            else
                Response(Status.NOT_FOUND)
        }
    }

fun checkRequirementTwo(serverModel: DEMOServerModel): Filter =
    Filter { handler: HttpHandler ->
        { request: Request ->
            println("valid filter 2, at your service")
//            println("Changing serverModel.status to 2!")
//            serverModel.status = 2
            if (serverModel.status.toString() == request.bodyString())
                handler(request)
            else
                Response(Status.FORBIDDEN)
        }
    }

fun handleItSimply(serverModel: DEMOServerModel): HttpHandler = { request: Request ->
    Response(Status.OK).body(serverModel.status.toString() + ", " + request.bodyString())
}