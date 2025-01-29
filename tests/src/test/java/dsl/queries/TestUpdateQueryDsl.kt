package dsl.queries

import io.kotgres.dsl.extensions.raw
import io.kotgres.dsl.operators.isNull
import io.kotgres.dsl.select
import io.kotgres.dsl.update
import io.kotgres.dsl.withAs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TestUpdateQueryDsl {

    @Nested
    inner class withAs {
        @Test
        fun `withAs as works as expected`() {
            val query = select("a").from("users").limit(10)
            val queryFinal = withAs("limited_users", query)
                .update("users")
                .set("asd", "a")
                .andSet("b", "c")
                .where("a > b".raw)
                .orWhere("hello")
                .andWhere(isNull("b"))
                .toSql(false)


            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ) UPDATE users SET asd='a', b='c' WHERE a > b OR hello AND b IS NULL""",
                queryFinal
            )
        }

        @Test
        fun `withAs accepts multiple queries`() {
            val query = select("a").from("users").limit(10)
            val query2 = select("b").from("countries").limit(10)
            val queryFinal = withAs("limited_users", query)
                .andWithAs("limited_countries", query2)
                .update("users")
                .set("asd", "a")
                .andSet("b", "c")
                .toSql()

            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ), limited_countries AS ( SELECT b FROM countries LIMIT 10 ) UPDATE users SET asd='a', b='c'""",
                queryFinal
            )
        }

        @Test
        fun `withAs as works raw query`() {
            val query = "SELECT a FROM users LIMIT 10".raw
            val queryFinal = withAs("limited_users", query)
                .update("users")
                .set("asd", "a")
                .andSet("b", "c")
                .where("a > b".raw)
                .orWhere("hello")
                .andWhere(isNull("b"))
                .toSql(false)


            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ) UPDATE users SET asd='a', b='c' WHERE a > b OR hello AND b IS NULL""",
                queryFinal
            )
        }

        @Test
        fun `withAs line breaks as expected`() {
            val query = "SELECT a FROM users LIMIT 10".raw
            val query2 = select("b").from("countries").limit(10)
            val queryFinal = withAs("limited_users", query)
                .andWithAs("limited_countries", query2)
                .update("users")
                .set("asd", "a")
                .andSet("b", "c")
                .where("a > b".raw)
                .orWhere("hello")
                .andWhere(isNull("b"))
                .toSql(true)


            assertEquals(
                """
WITH
  limited_users AS ( SELECT a FROM users LIMIT 10 ),
  limited_countries AS ( SELECT b FROM countries LIMIT 10 )
UPDATE users
SET asd='a',
  b='c'
WHERE
  a > b
  OR hello
  AND b IS NULL
                """.trimMargin(),
                queryFinal
            )
        }
    }

    @Test
    fun `set works`() {
        val string = update("users")
            .set("asd", "a")
            .andSet("b", "c")
            .toSql()

        assertEquals("UPDATE users SET asd='a', b='c'", string)
    }

    @Test
    fun `set works for Raw`() {
        val string = update("users")
            .set("asd", "a".raw)
            .andSet("b", "c")
            .toSql()

        assertEquals("UPDATE users SET asd=a, b='c'", string)
    }

    @Test
    fun `set works for subquery`() {
        val string = update("users")
            .set("a", select("age").from("users").limit(1))
            .toSql()

        assertEquals("UPDATE users SET a=( SELECT age FROM users LIMIT 1 )", string)
    }
    @Test
    fun `andSet works for subquery`() {
        val string = update("users")
            .set("asd", "a".raw)
            .andSet("b", select("age").from("users").limit(1))
            .toSql()

        assertEquals("UPDATE users SET asd=a, b=( SELECT age FROM users LIMIT 1 )", string)
    }

    @Test
    fun `where works as expected or one where`() {
        val string = update("users")
            .set("asd", "a")
            .andSet("b", "c")
            .where("a > b")
            .toSql(false)

        assertEquals("UPDATE users SET asd='a', b='c' WHERE a > b", string)
    }

    @Test
    fun `where with line breaks works as expected for one where`() {
        val string = update("users")
            .set("asd", "a")
            .andSet("b", "c")
            .where("a > b")
            .toSql(true)

        assertEquals(
            """
UPDATE users
SET asd='a',
  b='c'
WHERE
  a > b
""".trim(), string
        )
    }

    @Test
    fun `where works as expected for multiple where`() {
        val string = update("users")
            .set("asd", "a")
            .andSet("b", "c")
            .where("a > b".raw)
            .orWhere("hello")
            .andWhere(isNull("b"))
            .toSql(false)

        assertEquals(
            """
UPDATE users SET asd='a', b='c' WHERE a > b OR hello AND b IS NULL
""".trim(), string
        )
    }

    @Test
    fun `where with line breaks works as expected for multiple where`() {
        val string = update("users")
            .set("asd", "a")
            .andSet("b", "c")
            .where("a > b".raw)
            .orWhere("hello")
            .andWhere(isNull("b"))
            .toSql(true)

        assertEquals(
            """
UPDATE users
SET asd='a',
  b='c'
WHERE
  a > b
  OR hello
  AND b IS NULL
""".trim(), string
        )
    }

    @Test
    fun `returning works`() {
        val string = update("users")
            .set("a", "b")
            .returning("hello")
            .toSql(false)

        assertEquals("UPDATE users SET a='b' RETURNING hello", string)
    }

    @Test
    fun `returning works for multiple fields in the string`() {
        val string = update("users")
            .set("a", "b")
            .returning("hello, bye, asd")
            .toSql(false)

        assertEquals("UPDATE users SET a='b' RETURNING hello, bye, asd", string)
    }

    @Test
    fun `returning works for multiple fields in vararg`() {
        val string = update("users")
            .set("a", "b")
            .returning("hello", "bye", "asd")
            .toSql(false)

        assertEquals("UPDATE users SET a='b' RETURNING hello, bye, asd", string)
    }


}