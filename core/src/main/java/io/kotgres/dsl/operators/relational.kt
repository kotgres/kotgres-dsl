package io.kotgres.dsl.operators

import io.kotgres.dsl.Raw
import io.kotgres.dsl.extensions.raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery
import io.kotgres.dsl.internal.logic.Operations


/**
 * EQUALITY
 */
infix fun String.eq(value: String): IQueryBlock {
    return Operations.eq(this, value)
}

infix fun String.eq(value: Int): IQueryBlock {
    return Operations.eq(this, value)
}

infix fun String.eq(value: Long): IQueryBlock {
    return Operations.eq(this, value)
}

infix fun String.eq(value: Double): IQueryBlock {
    return Operations.eq(this, value)
}

infix fun String.eq(value: Raw): IQueryBlock {
    return Operations.eq(this, value)
}

infix fun String.eq(value: SelectQuery): IQueryBlock {
    return Operations.eq(this, value)
}

infix fun String.eq(value: IQueryBlock): IQueryBlock {
    return Operations.eq(this, value)
}

infix fun String.eq(value: Any): IQueryBlock {
    return Operations.eq(this, value)
}

/**
 * INEQUALITY
 */

infix fun String.neq(value: String): IQueryBlock {
    return Operations.neq(this, value)
}

infix fun String.neq(value: Int): IQueryBlock {
    return Operations.neq(this, value)
}

infix fun String.neq(value: Long): IQueryBlock {
    return Operations.neq(this, value)
}

infix fun String.neq(value: Double): IQueryBlock {
    return Operations.neq(this, value)
}

infix fun String.neq(value: Raw): IQueryBlock {
    return Operations.neq(this, value)
}

infix fun String.neq(value: SelectQuery): IQueryBlock {
    return Operations.neq(this, value)
}

infix fun String.neq(value: IQueryBlock): IQueryBlock {
    return Operations.neq(this, value)
}

infix fun String.neq(value: Any): IQueryBlock {
    return Operations.neq(this, value)
}

/**
 * GREATER
 */

infix fun String.greater(value: String): IQueryBlock {
    return Operations.greater(this, value)
}

infix fun String.greater(value: Int): IQueryBlock {
    return Operations.greater(this, value)
}

infix fun String.greater(value: Long): IQueryBlock {
    return Operations.greater(this, value)
}

infix fun String.greater(value: Double): IQueryBlock {
    return Operations.greater(this, value)
}

infix fun String.greater(raw: Raw): IQueryBlock {
    return Operations.greater(this, raw)
}

infix fun String.greater(value: SelectQuery): IQueryBlock {
    return Operations.greater(this, value)
}

infix fun String.greater(value: IQueryBlock): IQueryBlock {
    return Operations.greater(this, value)
}

infix fun String.greater(value: Any): IQueryBlock {
    return Operations.greater(this, value)
}

/**
 * GREATER OR EQUAL
 */

infix fun String.greaterEq(value: String): IQueryBlock {
    return Operations.greaterEq(this, value)
}

infix fun String.greaterEq(value: Int): IQueryBlock {
    return Operations.greaterEq(this, value)
}

infix fun String.greaterEq(value: Long): IQueryBlock {
    return Operations.greaterEq(this, value)
}

infix fun String.greaterEq(value: Double): IQueryBlock {
    return Operations.greaterEq(this, value)
}

infix fun String.greaterEq(raw: Raw): IQueryBlock {
    return Operations.greaterEq(this, raw)
}

infix fun String.greaterEq(value: SelectQuery): IQueryBlock {
    return Operations.greaterEq(this, value)
}

infix fun String.greaterEq(value: IQueryBlock): IQueryBlock {
    return Operations.greaterEq(this, value)
}

infix fun String.greaterEq(value: Any): IQueryBlock {
    return Operations.greaterEq(this, value)
}

/**
 * LESS
 */

infix fun String.less(value: String): IQueryBlock {
    return Operations.less(this, value)
}

infix fun String.less(value: Int): IQueryBlock {
    return Operations.less(this, value)
}

infix fun String.less(value: Long): IQueryBlock {
    return Operations.less(this, value)
}

infix fun String.less(value: Double): IQueryBlock {
    return Operations.less(this, value)
}

infix fun String.less(value: Raw): IQueryBlock {
    return Operations.less(this, value)
}

infix fun String.less(value: SelectQuery): IQueryBlock {
    return Operations.less(this, value)
}

infix fun String.less(value: IQueryBlock): IQueryBlock {
    return Operations.less(this, value)
}

infix fun String.less(value: Any): IQueryBlock {
    return Operations.less(this, value)
}

/**
 * LESS OR EQUAL
 */

infix fun String.lessEq(value: String): IQueryBlock {
    return Operations.lessEq(this, value)
}

infix fun String.lessEq(value: Int): IQueryBlock {
    return Operations.lessEq(this, value)
}

infix fun String.lessEq(value: Long): IQueryBlock {
    return Operations.lessEq(this, value)
}

infix fun String.lessEq(value: Double): IQueryBlock {
    return Operations.lessEq(this, value)
}

infix fun String.lessEq(value: Raw): IQueryBlock {
    return Operations.lessEq(this, value)
}

infix fun String.lessEq(value: SelectQuery): IQueryBlock {
    return Operations.lessEq(this, value)
}

infix fun String.lessEq(value: IQueryBlock): IQueryBlock {
    return Operations.lessEq(this, value)
}

infix fun String.lessEq(value: Any): IQueryBlock {
    return Operations.lessEq(this, value)
}


/**
 * LIKE
 */

infix fun String.like(value: String): IQueryBlock {
    return Operations.like(this, value)
}

infix fun String.like(raw: Raw): IQueryBlock {
    return Operations.like(this, raw)
}

infix fun String.like(value: SelectQuery): IQueryBlock {
    return Operations.like(this, value)
}

infix fun String.like(value: IQueryBlock): IQueryBlock {
    return Operations.like(this, value)
}
infix fun String.notLike(value: String): IQueryBlock {
    return Operations.notLike(this, value)
}

infix fun String.notLike(raw: Raw): IQueryBlock {
    return Operations.notLike(this, raw)
}

infix fun String.notLike(value: SelectQuery): IQueryBlock {
    return Operations.notLike(this, value)
}

infix fun String.notLike(value: IQueryBlock): IQueryBlock {
    return Operations.notLike(this, value)
}

infix fun String.ilike(value: String): IQueryBlock {
    return Operations.ilike(this, value)
}

infix fun String.ilike(raw: Raw): IQueryBlock {
    return Operations.ilike(this, raw)
}

infix fun String.ilike(value: SelectQuery): IQueryBlock {
    return Operations.ilike(this, value)
}

infix fun String.ilike(value: IQueryBlock): IQueryBlock {
    return Operations.ilike(this, value)
}

infix fun String.notIlike(value: String): IQueryBlock {
    return Operations.notIlike(this, value)
}

infix fun String.notIlike(raw: Raw): IQueryBlock {
    return Operations.notIlike(this, raw)
}

infix fun String.notIlike(value: SelectQuery): IQueryBlock {
    return Operations.notIlike(this, value)
}

infix fun String.notIlike(value: IQueryBlock): IQueryBlock {
    return Operations.notIlike(this, value)
}


/**
 * REGEXP
 */

infix fun String.regexp(value: String): IQueryBlock {
    return Operations.regexp(this, value)
}

infix fun String.regexp(raw: Raw): IQueryBlock {
    return Operations.regexpRaw(this, raw)
}

infix fun String.regexp(value: SelectQuery): IQueryBlock {
    return Operations.regexp(this, "( $value )")
}

infix fun String.regexp(value: IQueryBlock): IQueryBlock {
    return Operations.regexp(this, "( $value )")
}

infix fun String.notRegexp(value: String): IQueryBlock {
    return Operations.notRegexp(this, value)
}

infix fun String.notRegexp(raw: Raw): IQueryBlock {
    return Operations.notRegexpRaw(this, raw)
}

infix fun String.notRegexp(value: SelectQuery): IQueryBlock {
    return Operations.notRegexp(this, "( $value )")
}

infix fun String.notRegexp(value: IQueryBlock): IQueryBlock {
    return Operations.notRegexp(this, "( $value )")
}

/**
 * BETWEEN
 */

infix fun <T> String.between(values: Pair<T, T>): IQueryBlock {
    return Operations.between(this, values)
}

/**
 * NULLABILITY
 */

fun isNull(field: String): IQueryBlock {
    return Operations.isNull(field)
}

fun isNull(value: SelectQuery): IQueryBlock {
    return Operations.isNull("( $value )")
}

fun isNull(value: IQueryBlock): IQueryBlock {
    return Operations.isNull("( $value )")
}

fun isNotNull(field: String): IQueryBlock {
    return Operations.isNotNull(field)
}

fun isNotNull(value: SelectQuery): IQueryBlock {
    return Operations.isNotNull("( $value )")
}

fun isNotNull(value: IQueryBlock): IQueryBlock {
    return Operations.isNotNull("( $value )")
}

/**
 * IN LIST
 */

infix fun String.inList(values: List<Any>): IQueryBlock {
    return Operations.inList(this, values)
}

infix fun String.inList(values: Array<*>): IQueryBlock {
    return Operations.inList(this, values)
}

infix fun String.inList(raw: Raw): IQueryBlock {
    return Operations.inList(this, raw)
}

infix fun String.inList(value: SelectQuery): IQueryBlock {
    return Operations.inList(this, "( $value )".raw)
}

infix fun String.inList(value: IQueryBlock): IQueryBlock {
    return Operations.inList(this, "( $value )".raw)
}

infix fun String.notInList(values: List<Any>): IQueryBlock {
    return Operations.notInList(this, values)
}

infix fun String.notInList(values: Array<*>): IQueryBlock {
    return Operations.notInList(this, values)
}

infix fun String.notInList(raw: Raw): IQueryBlock {
    return Operations.notInList(this, raw)
}

infix fun String.notInList(value: SelectQuery): IQueryBlock {
    return Operations.notInList(this, "( $value )".raw)
}

infix fun String.notInList(value: IQueryBlock): IQueryBlock {
    return Operations.notInList(this, "( $value )".raw)
}

/**
 * ANY
 */

infix fun String.eqAny(values: List<Any>): IQueryBlock {
    return Operations.eqAny(this, values)
}

infix fun String.eqAny(raw: Raw): IQueryBlock {
    return Operations.eqAny(this, raw)
}

infix fun String.eqAny(value: SelectQuery): IQueryBlock {
    return Operations.eqAny(this, "( $value )".raw)
}

infix fun String.eqAny(value: IQueryBlock): IQueryBlock {
    return Operations.eqAny(this, "( $value )".raw)
}

infix fun String.neqAny(values: List<Any>): IQueryBlock {
    return Operations.neqAny(this, values)
}

infix fun String.neqAny(values: Array<*>): IQueryBlock {
    return Operations.neqAny(this, values)
}

infix fun String.neqAny(raw: Raw): IQueryBlock {
    return Operations.neqAny(this, raw)
}

infix fun String.neqAny(value: SelectQuery): IQueryBlock {
    return Operations.neqAny(this, "( $value )".raw)
}

infix fun String.neqAny(value: IQueryBlock): IQueryBlock {
    return Operations.neqAny(this, "( $value )".raw)
}
