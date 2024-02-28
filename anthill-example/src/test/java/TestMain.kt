
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.test.Test

class TestMain {
    @Test
    fun testUTC2GMT(){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"))
        val now = ZonedDateTime.now(ZoneId.of("UTC"))
        println(now)
        val toLocalDateTime = now.toLocalDateTime()
        println(toLocalDateTime)
    }
}
