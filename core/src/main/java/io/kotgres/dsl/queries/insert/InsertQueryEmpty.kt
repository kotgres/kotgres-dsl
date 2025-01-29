package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.queries.insert.base.InsertQuery

class InsertQueryEmpty internal constructor(query: InsertQuery) : InsertQuery(query) {

    fun insertInto(tableName: String): ColumnsInsertQuery {
        toInsertTable = tableName
        return ColumnsInsertQuery(this)
    }

}