package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.queries.insert.base.InsertQuery

open class OnConflictInsertQuery(query: InsertQuery) : ReturningInsertQuery(query) {

    fun onConflictColumn(columnName: String): OnConflictInnerInsertQuery {
        conflictTarget = "($columnName)"
        return OnConflictInnerInsertQuery(this)
    }

    fun onConflictColumn(vararg columnNameList: String): OnConflictInnerInsertQuery {
        conflictTarget = "(" + columnNameList.joinToString(",") + ")"
        return OnConflictInnerInsertQuery(this)
    }

    fun onConflictColumnList(columnNameList: List<String>): OnConflictInnerInsertQuery {
        conflictTarget = "(" + columnNameList.joinToString(",") + ")"
        return OnConflictInnerInsertQuery(this)
    }

    fun onConflictConstraint(constraintName: String): OnConflictInnerInsertQuery {
        conflictTarget = "ON CONSTRAINT $constraintName"
        return OnConflictInnerInsertQuery(this)
    }

    // TODO add to all queries
    /**
     * CLONE METHOD
     */
    fun clone(): OnConflictInsertQuery {
        return OnConflictInsertQuery(this)
    }
}