package io.kotgres.dsl.queries.update

import io.kotgres.dsl.queries.update.base.UpdateQuery

class EmptyUpdateQuery internal constructor(query: UpdateQuery) : UpdateQuery(query) {

    fun update(tableName: String): SetQueryUpdate {
        toUpdateTable = tableName
        return SetQueryUpdate(this)
    }

}