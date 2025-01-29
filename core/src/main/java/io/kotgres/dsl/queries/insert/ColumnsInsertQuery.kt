package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.common.QueryBuildingUtils
import io.kotgres.dsl.queries.insert.base.InsertQuery

class ColumnsInsertQuery internal constructor(query: InsertQuery) : InsertQuery(query) {

    // we skip columns and overriding if this is used
    fun defaultValues(): OnConflictInsertQuery {
        isDefaultValues = true
        return OnConflictInsertQuery(this)
    }

    // TODO does this even make sense ?
    // we skip columns and overriding if this is used
//    fun defaultValuesCustom(string: String): OnConflictInsertQuery {
//        isDefaultValues = true
//        return OnConflictInsertQuery(this)
//    }

    fun columns(columns: List<String>): OverridingValueInsertQuery {
        toInsertColumns.addAll(columns)
        return OverridingValueInsertQuery(this)
    }

    fun columns(vararg names: String): OverridingValueInsertQuery {
        toInsertColumns.addAll(names)
        return OverridingValueInsertQuery(this)
    }

    fun columns(vararg blocks: IQueryBlock): OverridingValueInsertQuery {
        toInsertColumns.addAll(blocks.map { it.getStatement() })
        return OverridingValueInsertQuery(this)
    }

    fun columns(vararg raw: Raw): OverridingValueInsertQuery {
        toInsertColumns.addAll(raw.map { it.value })
        return OverridingValueInsertQuery(this)
    }

    fun columns(vararg blocks: Any): ValuesInsertQuery {
        toInsertColumns.addAll(QueryBuildingUtils.columnsToStringList("Insert columns", blocks))
        return ValuesInsertQuery(this)
    }

}