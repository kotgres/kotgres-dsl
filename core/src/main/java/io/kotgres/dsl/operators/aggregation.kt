package io.kotgres.dsl.operators

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.QueryBlock

/**
 * GENERAL
 */

fun agg(funcName: String, field: String): IQueryBlock {
    return QueryBlock("${funcName}($field)")
}

fun agg(funcName: String, raw: Raw): IQueryBlock {
    return QueryBlock("${funcName}(${raw})")
}


/**
 * BASIC
 */

fun sum(field: String): IQueryBlock {
    return agg("sum", field)
}

fun sum(raw: Raw): IQueryBlock {
    return agg("sum", raw)
}

fun count(field: String): IQueryBlock {
    return agg("count", field)
}

fun count(raw: Raw): IQueryBlock {
    return agg("count", raw)
}

fun avg(field: String): IQueryBlock {
    return agg("avg", field)
}

fun avg(raw: Raw): IQueryBlock {
    return agg("avg", raw)
}

fun min(field: String): IQueryBlock {
    return agg("min", field)
}

fun min(raw: Raw): IQueryBlock {
    return agg("min", raw)
}

fun max(field: String): IQueryBlock {
    return agg("max", field)
}

fun max(raw: Raw): IQueryBlock {
    return agg("max", raw)
}

/**
 * ADVANCED
 */

fun arrayAgg(field: String): IQueryBlock {
    return agg("array_agg", field)
}

fun arrayAgg(raw: Raw): IQueryBlock {
    return agg("array_agg", raw)
}

fun bitAnd(field: String): IQueryBlock {
    return agg("bit_and", field)
}

fun bitAnd(raw: Raw): IQueryBlock {
    return agg("bit_and", raw)
}

fun bitOr(field: String): IQueryBlock {
    return agg("bit_or", field)
}

fun bitOr(raw: Raw): IQueryBlock {
    return agg("bit_or", raw)
}

fun boolAnd(field: String): IQueryBlock {
    return agg("boolAnd", field)
}

fun boolAnd(raw: Raw): IQueryBlock {
    return agg("boolAnd", raw)
}

fun boolOr(field: String): IQueryBlock {
    return agg("bool_or", field)
}

fun boolOr(raw: Raw): IQueryBlock {
    return agg("bool_or", raw)
}

fun every(field: String): IQueryBlock {
    return agg("every", field)
}

fun every(raw: Raw): IQueryBlock {
    return agg("every", raw)
}

fun jsonAgg(field: String): IQueryBlock {
    return agg("json_agg", field)
}

fun jsonAgg(raw: Raw): IQueryBlock {
    return agg("json_agg", raw)
}

fun jsonbAgg(field: String): IQueryBlock {
    return agg("jsonb_agg", field)
}

fun jsonbAgg(raw: Raw): IQueryBlock {
    return agg("jsonb_agg", raw)
}

fun jsonObjectAgg(field: String): IQueryBlock {
    return agg("json_object_agg", field)
}

fun jsonObjectAgg(raw: Raw): IQueryBlock {
    return agg("json_object_agg", raw)
}

fun jsonbObjectAgg(field: String): IQueryBlock {
    return agg("jsonb_object_agg", field)
}

fun jsonbObjectAgg(raw: Raw): IQueryBlock {
    return agg("jsonb_object_agg", raw)
}

fun stringAgg(field: String): IQueryBlock {
    return agg("string_agg", field)
}

fun stringAgg(raw: Raw): IQueryBlock {
    return agg("string_agg", raw)
}

fun xmlagg(field: String): IQueryBlock {
    return agg("xmlagg", field)
}

fun xmlagg(raw: Raw): IQueryBlock {
    return agg("xmlagg", raw)
}

// TODO implement all from docs https://www.postgresql.org/docs/9.5/functions-aggregate.html
