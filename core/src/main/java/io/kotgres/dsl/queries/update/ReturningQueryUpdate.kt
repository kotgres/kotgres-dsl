package io.kotgres.dsl.queries.update

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.update.base.UpdateQuery


open class ReturningQueryUpdate internal constructor(query: UpdateQuery) : CompleteQueryUpdate(query) {

    fun returning(expression: String): CompleteQueryUpdate {
        returning.add(expression)
        return CompleteQueryUpdate(this)
    }

    fun returning(raw: Raw): CompleteQueryUpdate {
        returning.add(raw.value)
        return CompleteQueryUpdate(this)
    }

    fun returning(vararg expression: String): CompleteQueryUpdate {
        returning.addAll(expression)
        return CompleteQueryUpdate(this)
    }

    fun returning(vararg raw: Raw): CompleteQueryUpdate {
        returning.addAll(raw.map { it.value })
        return CompleteQueryUpdate(this)
    }

}