package dsl.operators

import io.kotgres.dsl.select
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NewOperatorTest {

    @Test
    fun `operator works`() {
        val query = select(randomOperator("name")).from("users").toSql()
        assertEquals("SELECT RANDOM(name) FROM users", query)
    }
}