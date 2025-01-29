package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.select.base.SelectQuery


open class LimitQuerySelect(query: SelectQuery) : OffsetQuerySelect(query) {

    /**
     * LIMIT
     */

    fun limit(limit: Int): OffsetQuerySelect {
        this.rowLimit = limit
        return OffsetQuerySelect(this)
    }

}
