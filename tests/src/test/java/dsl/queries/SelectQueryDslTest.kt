package dsl.queries

import io.kotgres.dsl.*
import io.kotgres.dsl.exceptions.DslException
import io.kotgres.dsl.extensions.raw
import io.kotgres.dsl.operators.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class SelectQueryDslTest {

    @Nested
    inner class withAs {
        @Test
        fun `withAs as works as expected`() {
            val query = select("a").from("users").limit(10)
            val queryFinal = withAs("limited_users", query)
                .select("*")
                .from("limited_users")
                .orderBy("a")
                .toSql()


            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ) SELECT * FROM limited_users ORDER BY a ASC""",
                queryFinal
            )
        }

        @Test
        fun `withAs accepts multiple queries`() {
            val query = select("a").from("users").limit(10)
            val query2 = select("b").from("countries").limit(10)
            val queryFinal = withAs("limited_users", query)
                .andWithAs("limited_countries", query2)
                .select("*")
                .from("limited_users")
                .orderBy("a")
                .toSql()

            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ), limited_countries AS ( SELECT b FROM countries LIMIT 10 ) SELECT * FROM limited_users ORDER BY a ASC""",
                queryFinal
            )
        }
    }

    @Nested
    inner class select {
        @Test
        fun `select works for a single field`() {
            val string = select("name")
                .from("users")
                .toSql()

            assertEquals("SELECT name FROM users", string)
        }

        @Test
        fun `select works for a multiple fields`() {
            val string = select("name", "age")
                .from("users")
                .toSql()

            assertEquals("SELECT name, age FROM users", string)
        }

        @Test
        fun `select works with query block aggregates`() {
            val string = select(avg("age"))
                .from("users")
                .toSql()

            assertEquals("SELECT avg(age) FROM users", string)
        }

        @Test
        fun `select works with query block and fields combination`() {
            val string = select(avg("age"), "name")
                .from("users")
                .toSql()

            assertEquals("SELECT avg(age), name FROM users", string)
        }

        @Test
        fun `select throws if we pass random types`() {
            val error = assertThrows<DslException> {
                select(1, Date())
            }
            assertEquals("Select statement only accepts IQueryBlock, String or Raw queries", error.message)
        }

        @Test
        fun `select works for single raw`() {
            val string = select("name".raw)
                .from("users")
                .toSql()

            assertEquals("SELECT name FROM users", string)
        }

        @Test
        fun `select works for multiple raw`() {
            val string = select("name".raw, "age".raw)
                .from("users")
                .toSql()

            assertEquals("SELECT name, age FROM users", string)
        }

        @Test
        fun `select works for raw combined with others`() {
            val string = select("name as hello".raw, "email")
                .from("users")
                .toSql()

            assertEquals("SELECT name as hello, email FROM users", string)
        }
    }

    @Nested
    inner class where {

        @Test
        fun `whereRaw works as expected`() {
            val string = select("*")
                .from("users")
                .where("age = 18".raw)
                .toSql()

            assertEquals("SELECT * FROM users WHERE age = 18", string)
        }

        @Test
        fun `where works with eq and an int`() {
            val string = select("*")
                .from("users")
                .where("age" eq 18)
                .toSql()

            assertEquals("SELECT * FROM users WHERE age = 18", string)
        }

        @Test

        fun `where works with eq and a string`() {
            val string = select("*")
                .from("users")
                .where("name" eq "david")
                .toSql()

            assertEquals("SELECT * FROM users WHERE name = 'david'", string)
        }

        @Test
        fun `where works with eq and a raw binding`() {
            val string = select("*")
                .from("users")
                .where("name" eq "?".raw)
                .toSql()

            assertEquals("SELECT * FROM users WHERE name = ?", string)
        }

        @Test
        fun `where works with eq and a BINDING`() {
            val string = select("*")
                .from("users")
                .where("name" eq BINDING)
                .toSql()

            assertEquals("SELECT * FROM users WHERE name = ?", string)
        }

        @Test
        fun `where works with eq and a binding in a raw query`() {
            val string = select("*")
                .from("users")
                .where("name = ?".raw)
                .toSql()

            assertEquals("SELECT * FROM users WHERE name = ?", string)
        }

        @Test
        fun `orWhere works as expected`() {
            val query = select("a").from("b").where("c".raw).orWhere("d").toSql()

            assertEquals("SELECT a FROM b WHERE c OR d", query)
        }

        @Test
        fun `andWhere works as expected`() {
            val query = select("a").from("b").where("c").andWhere("d").toSql()

            assertEquals("SELECT a FROM b WHERE c AND d", query)
        }

        @Test
        fun `andWhere works as expected with line breaks`() {
            val query = select("a").from("b").where("c").andWhere("d").toSql(true)

            assertEquals(
                """
                SELECT a
                FROM b
                WHERE
                  c
                  AND d
                  """.trimIndent(),
                query
            )
        }

        @Test
        fun `where works as expected with or operator`() {
            val query = select("a").from("b").where(sub("c") or sub("d")).orderBy("e", Order.ASC).toSql()

            assertEquals("SELECT a FROM b WHERE ( c ) OR ( d ) ORDER BY e ASC", query)
        }

        @Test
        fun `can have queryBlock in eq right side`() {
            val query = select("*").from("users").where(
                "age" eq (
                        select("age").from("users").limit(1)
                        )
            ).toSql()

            assertEquals("SELECT * FROM users WHERE age = ( SELECT age FROM users LIMIT 1 )", query)
        }

    }

    @Nested
    inner class join {

        @Test
        fun `join works as expected`() {
            val sql = select("*")
                .from("user")
                .leftJoin("address").on("address.user_id = user.id")
                .toSql()
            assertEquals("SELECT * FROM user LEFT JOIN address ON ( address.user_id = user.id )", sql)
        }

        @Test
        fun `join with as works as expected`() {
            val sql = select("*")
                .from("user")
                .leftJoin("address", "a").on("a.user_id = user.id")
                .toSql()
            assertEquals("SELECT * FROM user LEFT JOIN address AS a ON ( a.user_id = user.id )", sql)
        }

        @Test
        fun `join with as works as expected with select from`() {
            val sql = select("*")
                .from("users")
                .leftJoin(select("*").from("address"), "a").on("user_id = u.id")
                .toSql()
            assertEquals("SELECT * FROM users LEFT JOIN ( SELECT * FROM address ) AS a ON ( user_id = u.id )", sql)
        }

        @Test
        fun `multiple join works as expected`() {
            val sql = select("*")
                .from("user")
                .leftJoin("address").on("address.user_id = user.id")
                .rightJoin("country").on("address.country_id = country.id")
                .toSql()
            assertEquals(
                "SELECT * FROM user LEFT JOIN address ON ( address.user_id = user.id ) RIGHT JOIN country ON ( address.country_id = country.id )",
                sql
            )
        }

        @Test
        fun `multiple joins and multiple on works as expected`() {
            val sql = select("*")
                .from("user")
                .leftJoin("address").on("address.user_id = user.id").andOn("address.age = 123")
                .rightJoin("country").on("address.country_id = country.id").orOn("country.is_island = false")
                .toSql(true)
            assertEquals(
                """
SELECT *
FROM user
LEFT JOIN address ON ( address.user_id = user.id AND address.age = 123 )
RIGHT JOIN country ON ( address.country_id = country.id AND country.is_island = false )
                """.trimIndent(),
                sql
            )
        }

        @Test
        fun `multiple joins and multiple on works as expected and alias`() {
            val sql = select("*")
                .from("user", "u")
                .leftJoin("address", "a").on("a.user_id = u.id").andOn("a.age = 123")
                .rightJoin("country", "c").on("a.country_id = c.id").orOn("c.is_island = false")
                .toSql(true)
            assertEquals(
                """
SELECT *
FROM user AS u
LEFT JOIN address AS a ON ( a.user_id = u.id AND a.age = 123 )
RIGHT JOIN country AS c ON ( a.country_id = c.id AND c.is_island = false )
                """.trimIndent(),
                sql
            )
        }

        @Test
        fun `multiple joins and multiple on works as expected and alias and subqueries`() {
            val subquery = select("*").from("address").limit(10)
            val subquery2 = select("*").from("country").where("population > 1000")
            val sql = select("*")
                .from("user", "u")
                .leftJoin(subquery, "a").on("a.user_id = u.id").andOn("a.age = 123")
                .rightJoin(subquery2, "c").on("a.country_id = c.id").orOn("c.is_island = false")
                .toSql(true)
            assertEquals(
                """
SELECT *
FROM user AS u
LEFT JOIN ( SELECT * FROM address LIMIT 10 ) AS a ON ( a.user_id = u.id AND a.age = 123 )
RIGHT JOIN ( SELECT * FROM country WHERE population > 1000 ) AS c ON ( a.country_id = c.id AND c.is_island = false )
                """.trimIndent(),
                sql
            )
        }
    }

    @Nested
    inner class groupBy {
        @Test
        fun `groupBy works as expected`() {
            val string = select("*")
                .from("asd")
                .groupBy("aasd")
                .toSql()

            assertEquals("SELECT * FROM asd GROUP BY aasd", string)
        }

        @Test
        fun `orderBy works as expected`() {
            val string = select("*")
                .from("asd")
                .orderBy("hello")
                .toSql()

            assertEquals("SELECT * FROM asd ORDER BY hello ASC", string)
        }
    }

    @Nested
    inner class having {
        @Test
        fun `complex having works as expected`() {
            val string = select("*")
                .from("country")
                .groupBy("region_id")
                .having("SUM(sales_amount) > 5000")
                .andHaving("AVG(sales_amount) > 500")
                .orHaving("1=1")
                .limit(10)
                .toSql(true)

            assertEquals(
                """
SELECT *
FROM country
GROUP BY region_id
HAVING
  SUM(sales_amount) > 5000
  AND AVG(sales_amount) > 500
  OR 1=1
LIMIT 10
            """.trimIndent(), string
            )
        }

        @Test
        fun `complex having works as expected (2)`() {
            val string = select("*")
                .from("country")
                .groupBy("region_id")
                .having("SUM(sales_amount) > 5000")
                .orHaving("AVG(sales_amount) > 500")
                .andHaving("1=1")
                .limit(10)
                .toSql(true)

            assertEquals(
                """
SELECT *
FROM country
GROUP BY region_id
HAVING
  SUM(sales_amount) > 5000
  OR AVG(sales_amount) > 500
  AND 1=1
LIMIT 10
            """.trimIndent(), string
            )
        }
    }

    @Nested
    inner class pagination {

        @Test
        fun `limit works as expected`() {
            val string = select("*")
                .from("asd")
                .limit(10)
                .toString()

            assertEquals("SELECT * FROM asd LIMIT 10", string)
        }

        @Test
        fun `offset works as expected`() {
            val string = select("*")
                .from("asd")
                .offset(10)
                .toString()

            assertEquals("SELECT * FROM asd OFFSET 10", string)
        }
    }

    @Nested
    inner class toQuery {
        @Test
        fun `does break lines as expected for simple query`() {
            val query = select("a")
                .from("b")
                .where(sub("c") or sub("d"))
                .orderBy("e", Order.ASC)
                .toSql(true)
            assertEquals(
                """
                SELECT a
                FROM b
                WHERE
                  ( c ) OR ( d )
                ORDER BY e ASC
                """.trimIndent(), query
            )
        }

        @Test
        fun `does break lines as expected for complex parenthesis`() {
            val query = select("*")
                .from("users")
                .where(
                    sub(("age" eq 20) and ("name" neq "David")) and isNull("email")
                ).toSql(true)

            assertEquals(
                """
                    SELECT *
                    FROM users
                    WHERE
                      ( age = 20 AND name != 'David' ) AND email IS NULL
                """.trimIndent(), query
            )
        }

        @Test
        fun `withAs breaks lines as expected`() {
            val query = select("a").from("users").limit(10)
            val query2 = select("b").from("countries").limit(10)
            val queryFinal = withAs("limited_users", query)
                .andWithAs("limited_countries", query2)
                .select("*")
                .from("limited_users")
                .orderBy("a")
                .toSql(true)

            assertEquals(
                """
WITH
  limited_users AS ( SELECT a FROM users LIMIT 10 ),
  limited_countries AS ( SELECT b FROM countries LIMIT 10 )
SELECT *
FROM limited_users
ORDER BY a ASC"""
                    .trimIndent(),
                queryFinal
            )
        }

        @Test
        fun `multiple join works as expected`() {
            val sql = select("*")
                .from("user")
                .leftJoin("address").on("address.user_id = user.id")
                .rightJoin("country").on("address.country_id = country.id")
                .toSql(true)
            assertEquals(
                """
SELECT *
FROM user
LEFT JOIN address ON ( address.user_id = user.id )
RIGHT JOIN country ON ( address.country_id = country.id )
            """.trimIndent(), sql
            )
        }
    }
}