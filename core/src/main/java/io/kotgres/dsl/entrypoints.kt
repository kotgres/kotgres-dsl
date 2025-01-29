package io.kotgres.dsl

import io.kotgres.dsl.queries.delete.EmptyDeleteQuery
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.delete.UsingDeleteQuery
import io.kotgres.dsl.queries.delete.base.DeleteQuery
import io.kotgres.dsl.queries.insert.ColumnsInsertQuery
import io.kotgres.dsl.queries.insert.InsertQueryEmpty
import io.kotgres.dsl.queries.insert.base.InsertQuery
import io.kotgres.dsl.queries.select.InnerSelectQuerySelect
import io.kotgres.dsl.queries.select.SelectQuerySelect
import io.kotgres.dsl.queries.select.base.SelectQuery
import io.kotgres.dsl.queries.update.SetQueryUpdate
import io.kotgres.dsl.queries.update.EmptyUpdateQuery
import io.kotgres.dsl.queries.update.base.UpdateQuery
import io.kotgres.dsl.queries.withAs.WithAsBlock

/**
 * WITH AS
 */

fun withAs(field: String, query: SelectQuery): WithAsBlock {
    return WithAsBlock(field, query.toString())
}

fun withAs(field: String, query: Raw): WithAsBlock {
    return WithAsBlock(field, query.value)
}

/**
 * DUPLICATED AND MODIFIED IN WithAsBlock.kt
 * If you make any changes to this file, replicate them there
 */

/**
 * SELECT
 */

fun select(vararg names: String): InnerSelectQuerySelect {
    return SelectQuerySelect(SelectQuery()).select(*names)
}

fun select(vararg block: IQueryBlock): InnerSelectQuerySelect {
    return SelectQuerySelect(SelectQuery()).select(*block)
}

fun select(vararg block: Any): InnerSelectQuerySelect {
    return SelectQuerySelect(SelectQuery()).select(*block)
}

fun select(vararg raw: Raw): InnerSelectQuerySelect {
    return SelectQuerySelect(SelectQuery()).select(*raw)
}

/**
 * INSERT
 */
fun insertInto(tableName: String): ColumnsInsertQuery {
    return InsertQueryEmpty(InsertQuery()).insertInto(tableName)
}

/**
 * UPDATE
 */

fun update(tableName: String): SetQueryUpdate {
    return EmptyUpdateQuery(UpdateQuery()).update(tableName)
}


/**
 * DELETE
 */

fun deleteFrom(tableName: String): UsingDeleteQuery {
    return EmptyDeleteQuery(DeleteQuery()).deleteFrom(tableName)
}