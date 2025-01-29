package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.select.base.SelectQuery


open class OffsetQuerySelect(query: SelectQuery) : SelectQuery(query) {

    fun offset(offset: Int): CompleteQuerySelect {
        this.rowOffset = offset
        return CompleteQuerySelect(this)
    }

}
