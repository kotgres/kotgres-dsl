package io.kotgres.dsl.queries.delete

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.delete.base.DeleteQuery

open class ReturningDeleteQuery internal constructor(query: DeleteQuery) : DeleteQuery(query) {

    fun returning(expression: String): CompletedDeleteQuery {
        returning.add(expression)
        return CompletedDeleteQuery(this)
    }

    fun returning(raw: Raw): CompletedDeleteQuery {
        returning.add(raw.value)
        return CompletedDeleteQuery(this)
    }

    fun returning(vararg expression: String): CompletedDeleteQuery {
        returning.addAll(expression)
        return CompletedDeleteQuery(this)
    }

    fun returning(vararg raw: Raw): CompletedDeleteQuery {
        returning.addAll(raw.map { it.value })
        return CompletedDeleteQuery(this)
    }

}