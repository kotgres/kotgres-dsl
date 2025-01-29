package dsl.legacy


import io.kotgres.dsl.extensions.raw
import io.kotgres.dsl.operators.between
import io.kotgres.dsl.operators.eq
import io.kotgres.dsl.operators.greater
import io.kotgres.dsl.operators.inList
import io.kotgres.dsl.operators.isNotNull
import io.kotgres.dsl.operators.less
import io.kotgres.dsl.operators.like
import io.kotgres.dsl.operators.match
import io.kotgres.dsl.operators.or
import io.kotgres.dsl.operators.sub
import io.kotgres.dsl.select
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QueryFunCallSpec {

    @Test
    fun `should match sql with fields and where condition`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where("name" eq "Bat Man")
            .toSql()
        assertEquals("SELECT id, name, age FROM user WHERE name = 'Bat Man'", sql)
    }

    @Test
    fun `should match sql with where condition field value IN a list`() {
        val userIds = listOf(1, 3, 12)
        val sql = select("id", "name", "age")
            .from("user")
            .where("id" inList userIds)
            .toSql()
        assertEquals("SELECT id, name, age FROM user WHERE id IN (1, 3, 12)", sql)
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
    fun `should match sql with where condition field value IN is a raw value`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where("name" inList "?,?".raw)
            .toSql()
        assertEquals("SELECT id, name, age FROM user WHERE name IN (?,?)", sql)
    }

    @Test
    fun `should match sql with where condition field value IN a list (raw)`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where("id" inList "?".raw)
            .toSql()
        assertEquals("SELECT id, name, age FROM user WHERE id IN (?)", sql)
    }

    @Test
    fun `should match sql with fields and multiple where conditions`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where(
                "name" eq "Bat Man",
                "id" greater 1,
                sub(("name" like "%bat") or ("name" like "man%")),
                isNotNull("nickname")
            )
            .toSql()
        assertEquals(
            "SELECT id, name, age FROM user WHERE name = 'Bat Man' AND id > 1 AND ( name LIKE '%bat' OR name LIKE 'man%' ) AND nickname IS NOT NULL",
            sql
        )
    }

    @Test
    fun `should match sql multiple where conditions with raw string values`() {
        val sql = select("id", "name", "age", "nickname")
            .from("user")
            .where(
                ("name" eq "nickname"),
                ("id" greater 1),
                sub(
                    ("name" like "%bat") or
                            ("name" like "man%")
                )
            )
            .toSql()
        assertEquals(
            "SELECT id, name, age, nickname FROM user WHERE name = 'nickname' AND id > 1 AND ( name LIKE '%bat' OR name LIKE 'man%' )",
            sql
        )
    }

    // TODO is having eqRaw the best solution? Could we maybe do it in another way?
    // TODO Also make sure there is an easy way to add a raw IQueryBlock anywhere
    @Test
    fun `equal with another column instead of a raw value`() {
        val sql = select("id", "name", "age", "nickname")
            .from("user")
            .where(
                "name" eq "nickname".raw,
            )
            .toSql()

        assertEquals(
            "SELECT id, name, age, nickname FROM user WHERE name = nickname",
            sql
        )
    }

    @Test
    fun `should match sql with sub query`() {
        val sql = select("id", "name", "age")
            .from(
                select("*").from("user").where("age" less 50).limit(10)
            )
            .where("name" like "Bat Man")
            .toSql()
        assertEquals(
            "SELECT id, name, age FROM (SELECT * FROM user WHERE age < 50 LIMIT 10) WHERE name LIKE 'Bat Man'",
            sql
        )
    }

    @Test
    fun `should match sql with between where conditions of numbers`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where(
                "name" eq "Bat Man",
                "age" between (10 to 50)
            )
            .toSql()
        assertEquals("SELECT id, name, age FROM user WHERE name = 'Bat Man' AND age BETWEEN 10 AND 50", sql)
    }

    @Test
    fun `should match sql with between where conditions of strings`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where(
                "name" between ("a" to "z")
            )
            .toSql()
        assertEquals(
            "SELECT id, name, age FROM user WHERE name BETWEEN 'a' AND 'z'", sql
        )
    }

    @Test
    fun `should match sql with fulltext`() {
        val sql = select("*").from("user")
            .where("name" match "smit")
            .toSql()
        assertEquals("SELECT * FROM user WHERE name @@ 'smit'", sql)
    }

    @Test
    fun `should match sql with fulltext against another column`() {
        val sql = select("*").from("user")
            .where("name" match "document".raw)
            .toSql()
        assertEquals("SELECT * FROM user WHERE name @@ document", sql)
    }

}
