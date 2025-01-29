package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.common.QueryBuildingUtils
import io.kotgres.dsl.queries.insert.base.InsertQuery

open class ValuesInsertQuery internal constructor(query: InsertQuery) : InsertQuery(query) {

    fun valueList(values: List<List<Any>>): OnConflictInsertQuery {
        values.forEach {
            toInsertValues.add(QueryBuildingUtils.valuesToStringList("Insert value", *it.toTypedArray()))
        }
        return OnConflictInsertQuery(this)
    }

    fun value(values: List<Any>): ValuesInnerInsertQuery {
        toInsertValues.add(QueryBuildingUtils.valuesToStringList("Insert value", *values.toTypedArray()))
        return ValuesInnerInsertQuery(this)
    }

    fun valueRaw(raw: Raw): ValuesInnerInsertQuery {
        toInsertValues.add(listOf(raw.value))
        return ValuesInnerInsertQuery(this)
    }

    fun valuesRawList(rawList: List<Raw>): ValuesInnerInsertQuery {
        toInsertValues.addAll(rawList.map { listOf(it.value) })
        return ValuesInnerInsertQuery(this)
    }

}