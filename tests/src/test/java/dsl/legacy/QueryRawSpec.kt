package dsl.legacy

import io.kotgres.dsl.operators.sub
import io.kotgres.dsl.select
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class QueryRawSpec {


    @Test
    fun `should match sql with fields specified`() {
        val sql = select("id AS user_id", "name AS full_name", "age").from("user").toSql()
        assertEquals("SELECT id AS user_id, name AS full_name, age FROM user", sql)
    }
    @Test
    fun `should match sql with fields and where condition`() {
                val sql = select("id", "name", "age")
                        .from("user")
                        .where("name = 'Bat Man'")
                        .toSql()
                assertEquals("SELECT id, name, age FROM user WHERE name = 'Bat Man'", sql)
    }

    @Test
    fun `should match sql with fields and multiple where conditions`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where(
                "name = 'Bat Man'",
                "id > 1",
                sub("name LIKE '%bat' OR name LIKE 'man%'")
            )
            .toSql()
        assertEquals("SELECT id, name, age FROM user WHERE name = 'Bat Man' AND id > 1 AND ( name LIKE '%bat' OR name LIKE 'man%' )", sql)
    }

    @Test
    fun `should match sql with sub query`() {
        val sql = select("id", "name", "age")
            .from(
                select("*").from("user").where("age < 50").limit(10)
            )
            .where("name LIKE '%Bat Man'")
            .toSql()
        assertEquals("SELECT id, name, age FROM (SELECT * FROM user WHERE age < 50 LIMIT 10) WHERE name LIKE '%Bat Man'", sql)
    }

    @Test
    fun `should match sql with between where conditions of numbers`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where(
                "name = ?",
                "age BETWEEN 10 AND 50"
            )
            .toSql()
        assertEquals("SELECT id, name, age FROM user WHERE name = ? AND age BETWEEN 10 AND 50", sql)
    }

    @Test
    fun `should match sql with between where conditions of strings`() {
        val sql = select("id", "name", "age")
            .from("user")
            .where(
                "name BETWEEN ? AND ?"
            )
            .toSql()
        assertEquals("SELECT id, name, age FROM user WHERE name BETWEEN ? AND ?", sql)
    }

}
