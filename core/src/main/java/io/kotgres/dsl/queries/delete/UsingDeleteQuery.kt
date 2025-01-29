package io.kotgres.dsl.queries.delete

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.delete.base.DeleteQuery
import io.kotgres.dsl.queries.select.base.SelectQuery

class UsingDeleteQuery internal constructor(query: DeleteQuery) : WhereDeleteQuery(query) {

    fun using(tableName: String): WhereDeleteQuery {
        usingStatement = tableName
        return WhereDeleteQuery(this)
    }

    fun using(selectQuery: SelectQuery, tableName: String): WhereDeleteQuery {
        usingStatement = "( ${selectQuery.toSql()} ) AS $tableName"
        return WhereDeleteQuery(this)
    }

    fun using(selectExpression: Raw, tableName: String): WhereDeleteQuery {
        usingStatement = "( ${selectExpression.value} ) AS $tableName"
        return WhereDeleteQuery(this)
    }

    fun using(selectExpression: String, tableName: String): WhereDeleteQuery {
        usingStatement = "( $selectExpression ) AS $tableName"
        return WhereDeleteQuery(this)
    }


}