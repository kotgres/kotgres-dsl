package io.kotgres.dsl.queries.base

import io.kotgres.dsl.Raw
import io.kotgres.dsl.internal.model.condition.And
import io.kotgres.dsl.internal.model.condition.Or
import io.kotgres.dsl.internal.model.condition.base.Condition
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.common.QueryBuildingUtils

/**
 * For queries that have a where statement (aka: select, update, delete)
 */
open class ConditionalQuery {

    /**
     * FIELDS
     */
    internal val whereCondition by lazy {
        arrayListOf<Condition>()
    }

    /**
     * AND
     */

    protected fun whereAnd(vararg blocks: IQueryBlock) {
        blocks.toList().forEach {
            whereCondition.add(And(it.toString()))
        }
    }

    protected fun whereAnd(vararg blocks: Raw) {
        blocks.toList().forEach {
            whereCondition.add(And(it.value))
        }
    }

    protected fun whereAnd(vararg blocks: Any) {
        blocks.toList().forEach {
            whereCondition.add(And(it.toString()))
        }
    }

    /**
     * OR
     */

    protected fun whereOr(vararg blocks: IQueryBlock) {
        blocks.toList().forEach {
            whereCondition.add(Or(it.toString()))
        }
    }

    protected fun whereOr(vararg blocks: Raw) {
        blocks.toList().forEach {
            whereCondition.add(Or(it.value))
        }
    }

    protected fun whereOr(vararg blocks: Any) {
        blocks.toList().forEach {
            whereCondition.add(Or(it.toString()))
        }
    }

    // WARNING: very similar code in QueryParts.buildWherePart, if changing here also change there
    protected fun buildWhere(shouldLineBreak: Boolean): String? {
        return QueryBuildingUtils.buildWherePart(whereCondition, shouldLineBreak)
    }

}