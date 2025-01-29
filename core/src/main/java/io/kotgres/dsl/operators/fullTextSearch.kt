package io.kotgres.dsl.operators

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.Raw
import io.kotgres.dsl.internal.logic.Operations

infix fun String.`@@`(value: String): IQueryBlock {
    return Operations.match(this, value)
}

infix fun String.`@@`(raw: Raw): IQueryBlock {
    return Operations.match(this, raw)
}

infix fun String.match(value: String): IQueryBlock {
    return Operations.match(this, value)
}

infix fun String.match(raw: Raw): IQueryBlock {
    return Operations.match(this, raw)
}

//infix fun List<String>.match(value: String): IQueryBlock {
//    return match(this, value)
//}
