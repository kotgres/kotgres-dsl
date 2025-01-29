package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.ConflictSet
import io.kotgres.dsl.queries.insert.base.InsertQuery

open class OnConflictInnerInsertQuery internal constructor(query: InsertQuery) : InsertQuery(query) {

    fun doNothing(): ReturningInsertQuery {
        conflictResolutionSet = true
        return ReturningInsertQuery(this)
    }

    fun doUpdate(conflictSetList: List<ConflictSet>): ReturningInsertQuery {
        conflictResolutionSet = true
        this.conflictSetList = conflictSetList
        return ReturningInsertQuery(this)
    }

    fun doUpdate(vararg conflictSetList: ConflictSet): OnConflictDoUpdateInnerInsertQuery {
        conflictResolutionSet = true
        this.conflictSetList = conflictSetList.toList()
        return OnConflictDoUpdateInnerInsertQuery(this)
    }
}