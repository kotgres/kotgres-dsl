package dsl.operators

import io.kotgres.dsl.operators.greaterEq
import io.kotgres.dsl.select
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class DatesFormatTest {

    @Test
    fun `LocalDateTime is formated correctly`() {
        val specificDateTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0)
        val query = select("*")
            .from("users")
            .where("date_created" greaterEq specificDateTime)
            .toSql()

        assertEquals("SELECT * FROM users WHERE date_created >= '2025-01-01T12:00'", query)
    }

    @Test
    fun `Date is formated correctly`() {
        val specificDate = Date(125, 0, 1, 12, 0, 0)
        val query = select("*")
            .from("users")
            .where("date_created" greaterEq specificDate)
            .toSql()

        assertEquals("SELECT * FROM users WHERE date_created >= '2025-01-01T12:00:00Z'", query)
    }
}