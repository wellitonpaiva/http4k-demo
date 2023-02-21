import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.string
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.SunHttp
import org.http4k.server.asServer

private const val SWAGGER_PATH = "/swagger.json"

val textLens = Body.string(ContentType.TEXT_PLAIN).toLens()

fun main() {
    routes(api,
        swaggerUi(SWAGGER_PATH)
    ).asServer(SunHttp()).start()
}

val api = contract {
    renderer = OpenApi3(ApiInfo("API Example", "1.0"))
    descriptionPath = SWAGGER_PATH
    routes += pingHandler
}

val pingHandler = "/ping" meta {
    summary = "Say Pong"
    returning(OK, textLens to "Sample Greeting")
} bindContract GET to { _: Request ->
    Response(OK).with(textLens of "Pong")
}


fun swaggerUi(descriptionPath: String): RoutingHttpHandler = routes(
    "docs" bind GET to {
        Response(Status.FOUND).header("Location", "/docs/index.html?url=$descriptionPath")
    },
    "/docs" bind static(Classpath("META-INF/resources/webjars/swagger-ui/3.25.2"))
)