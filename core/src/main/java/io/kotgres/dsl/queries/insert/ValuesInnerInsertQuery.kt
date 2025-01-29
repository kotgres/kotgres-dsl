package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.queries.common.QueryBuildingUtils
import io.kotgres.dsl.queries.insert.base.InsertQuery

class ValuesInnerInsertQuery internal constructor(query: InsertQuery) : OnConflictInsertQuery(query) {

    fun andValue(values: List<Any>): ValuesInnerInsertQuery {
        toInsertValues.add(QueryBuildingUtils.valuesToStringList("Insert value", *values.toTypedArray()))
        return ValuesInnerInsertQuery(this)
    }

    fun andValue(vararg values: Any): ValuesInnerInsertQuery {
        toInsertValues.add(QueryBuildingUtils.valuesToStringList("Insert value", values))
        return ValuesInnerInsertQuery(this)
    }
}