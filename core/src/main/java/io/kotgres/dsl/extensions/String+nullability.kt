package io.kotgres.dsl.extensions

import io.kotgres.dsl.internal.logic.Operations
import io.kotgres.dsl.queries.block.IQueryBlock


fun String.isNull(): IQueryBlock {
    return Operations.isNull(this)
}

fun String.isNotNull(): IQueryBlock {
    return Operations.isNotNull(this)
}
