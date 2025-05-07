package dsl.legacy

import io.kotgres.dsl.BINDING
import io.kotgres.dsl.Order
import io.kotgres.dsl.exceptions.DslException
import io.kotgres.dsl.extensions.raw
import io.kotgres.dsl.operators.`@@`
import io.kotgres.dsl.operators.AS
import io.kotgres.dsl.operators.agg
import io.kotgres.dsl.operators.and
import io.kotgres.dsl.operators.avg
import io.kotgres.dsl.operators.between
import io.kotgres.dsl.operators.eq
import io.kotgres.dsl.operators.greater
import io.kotgres.dsl.operators.inList
import io.kotgres.dsl.operators.isNotNull
import io.kotgres.dsl.operators.less
import io.kotgres.dsl.operators.lessEq
import io.kotgres.dsl.operators.like
import io.kotgres.dsl.operators.match
import io.kotgres.dsl.operators.or
import io.kotgres.dsl.operators.sub
import io.kotgres.dsl.operators.toTsvector
import io.kotgres.dsl.select
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Date

class QuerySpecTest {

    @Nested
    inner class `given one table select statement` {
        @Test
        fun `should match sql with select asterisk`() {
            val sql = select("*").from("user").limit(10).offset(20).toSql()

            assertEquals("SELECT * FROM user LIMIT 10 OFFSET 20", sql)
        }

        @Test
        fun `should match sql with fields specified`() {
            val sql = select("id", "name", "age").from("user").toSql()

            assertEquals("SELECT id, name, age FROM user", sql)
        }

        @Test
        fun `should match sql with fields specified and order by`() {
            val sql = select("id", "name", "age").from("user")
                .orderBy("name")
                .toSql()

            assertEquals("SELECT id, name, age FROM user ORDER BY name ASC", sql)
        }

        @Test
        fun `should match sql with fields specified and order by two fields`() {
            val sql = select("id", "name", "age").from("user")
                .orderBy(listOf("id", "name"))
                .toSql()

            assertEquals("SELECT id, name, age FROM user ORDER BY id ASC, name ASC", sql)
        }

        @Test
        fun `should match sql with fields specified and order by two fields different directions`() {
            val sql = select("id", "name", "age").from("user")
                .orderBy("id", Order.DESC)
                .andOrderBy("name", Order.ASC)
                .toSql()

            assertEquals("SELECT id, name, age FROM user ORDER BY id DESC, name ASC", sql)
        }

        @Test
        fun `should match sql with fields and where condition`() {
            val sql = select("id", "name", "age")
                .from("user")
                .where("name" eq "?".raw)
                .toSql()

            assertEquals("SELECT id, name, age FROM user WHERE name = ?", sql)
        }

        @Test
        fun `should match sql with where condition field value IN a list`() {
            val userIds = listOf(1, 2, 3)
            val sql = select("id", "name", "age")
                .from("user")
                .where("id" inList userIds)
                .toSql()

            assertEquals("SELECT id, name, age FROM user WHERE id IN (1, 2, 3)", sql)
        }

        @Test
        fun `should match sql with where condition field value IN a string list`() {
            val names = listOf("Leng", "Bat's Man")
            val sql = select("id", "name", "age")
                .from("user")
                .where("name" inList names)
                .toSql()

            assertEquals("SELECT id, name, age FROM user WHERE name IN ('Leng', 'Bat''s Man')", sql)
        }

        @Test
        fun `should match sql with where condition field value IN a list(non-parameterized)`() {
                val sql = select("id", "name", "age")
                        .from("user")
                        .where("id" inList arrayOf(1, 12, 18, 25, 55))
                        .toSql()

                assertEquals("SELECT id, name, age FROM user WHERE id IN (1, 12, 18, 25, 55)", sql)
        }

        @Test
        fun `should match sql with where condition field value IN a string list (non-parameterized)`() {
            val sql = select("id", "name", "age")
                .from("user")
                .where("name" inList arrayOf("Leng", "Bat's Man"))
                .toSql()

            assertEquals("SELECT id, name, age FROM user WHERE name IN ('Leng', 'Bat''s Man')", sql)
        }

       @Test
        fun `should match sql with fields and multiple where conditions`() {
            val sql = select("id", "name", "age")
                .from("user")
                .where(
                    "name" eq BINDING and
                            ("id" greater 1) and
                            sub(("name" like BINDING) or ("name" like BINDING)) and
                            (isNotNull("nickname"))
                )
                .toSql()

            assertEquals(
                "SELECT id, name, age FROM user WHERE name = ? AND id > 1 AND ( name LIKE ? OR name LIKE ? ) AND nickname IS NOT NULL",
                sql
            )
        }

        @Test
        fun `should match sql with fields and multiple where conditions (2)`() {
            val sql = select("id", "name", "age")
                .from("user")
                .where(
                    "name" eq BINDING and
                            ("id" greater 1) and
                            sub(("name" like BINDING) or ("name" like BINDING))
                )
                .toSql()

            assertEquals(
                "SELECT id, name, age FROM user WHERE name = ? AND id > 1 AND ( name LIKE ? OR name LIKE ? )",
                sql
            )
        }

        @Test
        fun `should match sql multiple where conditions with raw string values`() {
            val sql = select("id", "name", "age", "nickname")
                .from("user")
                .where(
                    "name" eq "nickname".raw,
                    "id" greater 1,
                    sub(
                        ("name" like "%bat") or
                                ("name" like "man%")
                    )
                )
                .toSql()

            assertEquals(
                "SELECT id, name, age, nickname FROM user WHERE name = nickname AND id > 1 AND ( name LIKE '%bat' OR name LIKE 'man%' )",
                sql
            )
        }

        @Test
        fun `should match sql with sub query`() {
            val sql = select("id", "name", "age")
                .from(
                    select("*").from("user").where("age" less 50).limit(10)
                )
                .where("name" like BINDING)
                .toSql()

            assertEquals(
                "SELECT id, name, age FROM (SELECT * FROM user WHERE age < 50 LIMIT 10) WHERE name LIKE ?",
                sql
            )
        }

        @Test
        fun `lessEq works as expected`() {
            val sql = select("*").from("user").where("age" lessEq 50).limit(10).toSql()

            assertEquals(
                "SELECT * FROM user WHERE age <= 50 LIMIT 10",
                sql
            )
        }

        @Test
        fun `should match sql with between where conditions of numbers`() {
            val sql = select("id", "name", "age")
                .from("user")
                .where(
                    "name" eq "Bat Man" and
                            ("age" between (10 to 50))
                )
                .toSql()

            assertEquals("SELECT id, name, age FROM user WHERE name = 'Bat Man' AND age BETWEEN 10 AND 50", sql)
        }

        @Test
        fun `should match sql with between where conditions of strings`() {
            val sql = select("id", "name", "age")
                .from("user")
                .where(
                    ("name" between ("?".raw to "?".raw))
                )
                .toSql()

            assertEquals("SELECT id, name, age FROM user WHERE name BETWEEN ? AND ?", sql)
        }

        @Test
        fun `should match sql with fulltext search(MySQL)`() {
            val sql = select("*").from("user")
                .where("name" match "smit")
                .toSql()

            assertEquals("SELECT * FROM user WHERE name @@ 'smit'", sql)
        }

        @Test
        fun `should match sql with fulltext search (2)`() {
            val sql = select("*").from("user")
                .where("name" `@@` "smit")
                .toSql()

            assertEquals("SELECT * FROM user WHERE name @@ 'smit'", sql)
        }

        @Test
        fun to_tsvector() {
            val sql = select(toTsvector("xyz")).from("user")
                .toSql()

            assertEquals("SELECT to_tsvector('''xyz''') FROM user", sql)
        }

        @Test
        fun `codeblock in select`() {
            val sql = select(avg("asd")).from("user")
                .toSql()

            assertEquals("SELECT avg(asd) FROM user", sql)
        }

        @Test
        fun `aggregte raw`() {
            val sql = select(agg("sum", "quantity * price")).from("user")
                .toSql()

            assertEquals("SELECT sum(quantity * price) FROM user", sql)
        }

        @Test
        fun `mixed w column in select`() {
            val sql = select(avg("asd"), "hello").from("user")
                .toSql()

            assertEquals("SELECT avg(asd), hello FROM user", sql)
        }

        @Test
        fun `throws when non query block or string is passed to select`() {
            val error = assertThrows<DslException> {
                select(avg("asd"), "hello", Date())
            }
            assertEquals("Select statement only accepts IQueryBlock, String or Raw queries", error.message)
        }

        @Test
        fun `to_tsvector comp`() {
            val sql = select(toTsvector("asd", "english")).from("user")
                .toSql()

            assertEquals("SELECT to_tsvector('english', '''asd''') FROM user", sql)
        }

        @Test
        fun `and is added as expected`() {
            val sql = select("1").from("user")
                .where("1")
                .andWhere("2")
                .toSql()

            assertEquals("SELECT 1 FROM user WHERE 1 AND 2", sql)
        }


        @Test
        fun `and is added as expected 2`() {
            val sql = select("1").from("user")
                .where(toTsvector("1").and(toTsvector("2")))
                .toSql()

            assertEquals("SELECT 1 FROM user WHERE to_tsvector('''1''') AND to_tsvector('''2''')", sql)
        }

        @Test
        fun `as works`() {
            val sql = select("hello" AS "h").toSql()

            assertEquals("SELECT hello AS h", sql)
        }
    }
}