package dsl.queries

import io.kotgres.dsl.deleteFrom
import io.kotgres.dsl.extensions.raw
import io.kotgres.dsl.operators.eq
import io.kotgres.dsl.operators.greater
import io.kotgres.dsl.select
import io.kotgres.dsl.withAs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DeleteQueryDslTest {

    @Nested
    inner class withAs {
        @Test
        fun `withAs as works as expected`() {
            val query = select("a").from("users").limit(10)
            val queryFinal = withAs("limited_users", query)
                .deleteFrom("users")
                .toSql()

            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ) DELETE FROM users""",
                queryFinal
            )
        }

        @Test
        fun `withAs accepts multiple queries`() {
            val query = select("a").from("users").limit(10)
            val query2 = select("b").from("countries").limit(10)
            val queryFinal = withAs("limited_users", query)
                .andWithAs("limited_countries", query2)
                .deleteFrom("users")
                .where("age > 100")
                .returning("id")
                .toSql()

            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ), limited_countries AS ( SELECT b FROM countries LIMIT 10 ) DELETE FROM users WHERE age > 100 RETURNING id""",
                queryFinal
            )
        }

        @Test
        fun `withAs as works raw query`() {
            val query = "SELECT a FROM users LIMIT 10".raw
            val queryFinal = withAs("limited_users", query)
                .deleteFrom("users")
                .where("age > 100")
                .returning("id")
                .toSql()

            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ) DELETE FROM users WHERE age > 100 RETURNING id""",
                queryFinal
            )
        }

        @Test
        fun `withAs line breaks as expected`() {
            val query = "SELECT a FROM users LIMIT 10".raw
            val query2 = select("b").from("countries").limit(10)
            val queryFinal = withAs("limited_users", query)
                .andWithAs("limited_countries", query2)
                .deleteFrom("users")
                .where("age > 100")
                .returning("id")
                .toSql(true)

            assertEquals(
                """
WITH
  limited_users AS ( SELECT a FROM users LIMIT 10 ),
  limited_countries AS ( SELECT b FROM countries LIMIT 10 )
DELETE FROM users
WHERE
  age > 100
RETURNING
  id
                """.trimIndent(),
                queryFinal
            )
        }
    }

    @Test
    fun `deleteFrom works`() {
        val query = deleteFrom("users")
            .toSql()
        assertEquals("DELETE FROM users", query)
    }

    @Test
    fun `using works`() {
        val query = deleteFrom("users")
            .using("countries AS c")
            .toSql()
        assertEquals("DELETE FROM users USING countries AS c", query)
    }

    @Test
    fun `where works straight from deleteFrom`() {
        val query = deleteFrom("users")
            .where("age > 100")
            .toSql()
        assertEquals("DELETE FROM users WHERE age > 100", query)
    }

    @Test
    fun `andWhere works`() {
        val query = deleteFrom("users")
            .where("age" greater 100)
            .andWhere("was_deleted" eq false)
            .toSql()
        assertEquals("DELETE FROM users WHERE age > 100 AND was_deleted = false", query)
    }

    @Test
    fun `orWhere works`() {
        val query = deleteFrom("users")
            .where("age > 100")
            .orWhere("was_deleted" eq false)
            .toSql(false)
        assertEquals("DELETE FROM users WHERE age > 100 OR was_deleted = false", query)
    }

    @Test
    fun `set works`() {
        val query = deleteFrom("users")
            .where("age > 100")
            .returning("id")
            .toSql()
        assertEquals("DELETE FROM users WHERE age > 100 RETURNING id", query)
    }

}