package io.kotgres.dsl.internal.model.condition.base

internal abstract class Condition(private var logic: String) {

    open fun getStatement(): String = logic
    open fun getOperator(): String = ""
}