package io.kotgres.dsl.internal.model.condition

import io.kotgres.dsl.internal.model.condition.base.Condition

internal class Or(logic: String) : Condition(logic) {
    override fun getOperator(): String = "OR"
}
