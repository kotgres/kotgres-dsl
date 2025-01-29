package io.kotgres.dsl.queries.delete

import io.kotgres.dsl.queries.delete.base.DeleteQuery

class EmptyDeleteQuery internal constructor(query: DeleteQuery) : DeleteQuery(query) {

    fun deleteFrom(tableName: String): UsingDeleteQuery {
        toDeleteFromTable = tableName
        return UsingDeleteQuery(this)
    }

}