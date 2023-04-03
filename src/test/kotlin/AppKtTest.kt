import org.http4k.core.Request
import org.http4k.core.Request
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AppKtTest {

    @Test
    internal fun `hello world`() {
       assertEquals("Hello World!", api.invoke(Request(Method.GET, "/")).bodyString())
    }
}