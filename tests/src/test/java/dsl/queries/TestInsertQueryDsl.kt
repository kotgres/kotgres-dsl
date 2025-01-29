package dsl.queries

import io.kotgres.dsl.ConflictSet
import io.kotgres.dsl.extensions.raw
import io.kotgres.dsl.insertInto
import io.kotgres.dsl.operators.isNull
import io.kotgres.dsl.select
import io.kotgres.dsl.withAs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TestInsertQueryDsl {

    @Nested
    inner class withAs {
        @Test
        fun `withAs as works as expected`() {
            val query = select("a").from("users").limit(10)
            val queryFinal = withAs("limited_users", query)
                .insertInto("user")
                .toSql(false)

            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ) INSERT INTO user""",
                queryFinal
            )
        }

        @Test
        fun `withAs accepts multiple queries`() {
            val query = select("a").from("users").limit(10)
            val query2 = select("b").from("countries").limit(10)
            val queryFinal = withAs("limited_users", query)
                .andWithAs("limited_countries", query2)
                .insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("is_duplicated", true))
                .toSql(false)

            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ), limited_countries AS ( SELECT b FROM countries LIMIT 10 ) INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET is_duplicated = true""",
                queryFinal
            )
        }

        @Test
        fun `withAs as works raw query`() {
            val query = "SELECT a FROM users LIMIT 10".raw
            val queryFinal = withAs("limited_users", query)
                .insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("is_duplicated", true))
                .toSql(false)

            assertEquals(
                """WITH limited_users AS ( SELECT a FROM users LIMIT 10 ) INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET is_duplicated = true""",
                queryFinal
            )
        }

        @Test
        fun `withAs line breaks as expected`() {
            val query = "SELECT a FROM users LIMIT 10".raw
            val query2 = select("b").from("countries").limit(10)
            val queryFinal = withAs("limited_users", query)
                .andWithAs("limited_countries", query2)
                .insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("is_duplicated", true))
                .toSql(true)

            assertEquals(
                """
WITH
  limited_users AS ( SELECT a FROM users LIMIT 10 ),
  limited_countries AS ( SELECT b FROM countries LIMIT 10 )
INSERT INTO user ( name, age )
VALUES ( 'david', 18 )
ON CONFLICT (name) DO UPDATE
  SET is_duplicated = true
                """.trimIndent(),
                queryFinal
            )
        }
    }

    @Nested
    inner class basic {
        @Test
        fun `insert into works`() {
            val query = insertInto("user")
                .toSql()

            assertEquals("INSERT INTO user", query)
        }

        @Test
        fun `columns works`() {
            val query = insertInto("user")
                .columns("name", "age")
                .toSql()

            assertEquals("INSERT INTO user ( name, age )", query)
        }

        @Test
        fun `calling defaultValues works`() {
            val query = insertInto("user")
                .defaultValues()
                .toSql()

            assertEquals("INSERT INTO user DEFAULT VALUES", query)
        }

        @Test
        fun `calling value single value works`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .toSql()

            assertEquals("INSERT INTO user ( name, age ) VALUES ( 'david', 18 )", query)
        }

        @Test
        fun `calling value and addValue with two values works`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .andValue(listOf("max", 52))
                .toSql()

            assertEquals("INSERT INTO user ( name, age ) VALUES ( 'david', 18 ), ( 'max', 52 )", query)
        }

        @Test
        fun `calling value and addValue with three values works`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .andValue(listOf("max", 52))
                .andValue(listOf("martha", 70))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ), ( 'max', 52 ), ( 'martha', 70 )",
                query
            )
        }

        @Test
        fun `calling valueList works with varargs`() {
            val query = insertInto("user")
                .columns("name", "age")
                .valueList(listOf(listOf("david", 18), listOf("max", 52), listOf("martha", 70)))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ), ( 'max', 52 ), ( 'martha', 70 )",
                query
            )
        }

        @Test
        fun `calling valueList works with list wrapper`() {
            val query = insertInto("user")
                .columns("name", "age")
                .valueList(listOf(listOf("david", 18), listOf("max", 52), listOf("martha", 70)))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ), ( 'max', 52 ), ( 'martha', 70 )",
                query
            )
        }
    }


    @Nested
    inner class overriding {
        @Test
        fun `overridingSystemValue works`() {
            val query = insertInto("user")
                .columns("name", "age")
                .overridingSystemValue()
                .value(listOf("david", 18))
                .toSql()

            assertEquals("INSERT INTO user ( name, age ) OVERRIDING SYSTEM VALUE VALUES ( 'david', 18 )", query)
        }

        @Test
        fun `overridingUserValue works`() {
            val query = insertInto("user")
                .columns("name", "age")
                .overridingUserValue()
                .value(listOf("david", 18))
                .toSql()

            assertEquals("INSERT INTO user ( name, age ) OVERRIDING USER VALUE VALUES ( 'david', 18 )", query)
        }
    }

    @Nested
    inner class onConflict {
        @Test
        fun `onConflict works after value and shows with no conflict resolution strategy if not set (incomplete query)`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .toSql()

            assertEquals("INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name)", query)
        }

        @Test
        fun `onConflict works after andValue (incomplete query)`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .andValue(listOf("max", 52))
                .onConflictColumn("name")
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ), ( 'max', 52 ) ON CONFLICT (name)",
                query
            )
        }

        @Test
        fun `onConflict works after valueList (incomplete query)`() {
            val query = insertInto("user")
                .columns("name", "age")
                .valueList(listOf(listOf("david", 18), listOf("max", 52)))
                .onConflictColumn("name")
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ), ( 'max', 52 ) ON CONFLICT (name)",
                query
            )
        }

        @Test
        fun `onConflict works with doNothing`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .toSql()

            assertEquals("INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name)", query)
        }

        @Test
        fun `onConflict works with merge and string`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("name", "duplicated"))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET name = 'duplicated'",
                query
            )
        }

        @Test

        fun `onConflict works with do update set and where`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("name", "duplicated"))
                .where("age IS NULL")
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET name = 'duplicated' WHERE age IS NULL",
                query
            )
        }

        @Test
        fun `onConflict works with do update set and where when using IQueryBlock`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("name", "duplicated"))
                .where(isNull("age"))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET name = 'duplicated' WHERE age IS NULL",
                query
            )
        }

        @Test
        fun `onConflict works with do update, where and returning`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("name", "duplicated"))
                .where("age IS NULL")
                .returning("*")
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET name = 'duplicated' WHERE age IS NULL RETURNING *",
                query
            )
        }

        @Test
        fun `onConflict works with merge and Boolean`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("is_duplicated", true))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET is_duplicated = true",
                query
            )
        }

        @Test
        fun `onConflict works with merge and raw`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("updated_at", "now()".raw))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET updated_at = now()",
                query
            )
        }

        @Test
        fun `onConflict works with merge and Int`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("age", 99))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET age = 99",
                query
            )
        }

        @Test
        fun `onConflict works with merge and null`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("age", null))
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET age = null",
                query
            )
        }

        @Test
        fun `onConflict works with merge and returning`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doUpdate(ConflictSet("age", null))
                .returning("id")
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO UPDATE SET age = null RETURNING id",
                query
            )
        }
    }

    @Nested
    inner class returning {
        @Test
        fun `returning works after andValue`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .andValue(listOf("max", 52))
                .returning("id", "created_at")
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ), ( 'max', 52 ) RETURNING id, created_at",
                query
            )
        }

        @Test
        fun `returning works after value`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .returning("id", "created_at")
                .toSql()

            assertEquals("INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) RETURNING id, created_at", query)
        }

        @Test
        fun `returning works with valueList`() {
            val query = insertInto("user")
                .columns("name", "age")
                .valueList(listOf( listOf("david", 18)))
                .returning("id, created_at".raw)
                .toSql()

            assertEquals("INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) RETURNING id, created_at", query)
        }

        @Test
        fun `returning works after onConflict if conflict resolution is set`() {
            val query = insertInto("user")
                .columns("name", "age")
                .value(listOf("david", 18))
                .onConflictColumn("name")
                .doNothing()
                .returning("id")
                .toSql()

            assertEquals(
                "INSERT INTO user ( name, age ) VALUES ( 'david', 18 ) ON CONFLICT (name) DO NOTHING RETURNING id",
                query
            )
        }
    }

}