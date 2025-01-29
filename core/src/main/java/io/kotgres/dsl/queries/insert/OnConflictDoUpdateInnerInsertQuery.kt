package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.insert.base.InsertQuery
import io.kotgres.dsl.internal.model.condition.And

open class OnConflictDoUpdateInnerInsertQuery internal constructor(query: InsertQuery) : ReturningInsertQuery(query) {

    fun where(vararg blocks: IQueryBlock): ReturningInsertQuery {
        blocks.toList().forEach {
            onConflictDoUpdateWhereConditions.add(And(it.toString()))
        }
        return ReturningInsertQuery(this)
    }

    fun where(vararg blocks: Raw): ReturningInsertQuery {
        blocks.toList().forEach {
            onConflictDoUpdateWhereConditions.add(And(it.value))
        }
        return ReturningInsertQuery(this)
    }

    fun where(vararg blocks: Any): ReturningInsertQuery {
        blocks.toList().forEach {
            onConflictDoUpdateWhereConditions.add(And(it.toString()))
        }
        return ReturningInsertQuery(this)
    }

}