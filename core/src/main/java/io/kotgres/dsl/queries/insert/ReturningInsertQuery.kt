package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.insert.base.InsertQuery

open class ReturningInsertQuery internal constructor(query: InsertQuery) : InsertQuery(query) {

    fun returning(expression: String): CompleteQueryInsert {
        returning.add(expression)
        return CompleteQueryInsert(this)
    }

    fun returning(raw: Raw): CompleteQueryInsert {
        returning.add(raw.value)
        return CompleteQueryInsert(this)
    }

    fun returning(vararg expression: String): CompleteQueryInsert {
        returning.addAll(expression)
        return CompleteQueryInsert(this)
    }

    fun returning(vararg raw: Raw): CompleteQueryInsert {
        returning.addAll(raw.map { it.value })
        return CompleteQueryInsert(this)
    }

}