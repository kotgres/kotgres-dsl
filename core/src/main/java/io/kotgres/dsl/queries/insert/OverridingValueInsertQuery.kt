package io.kotgres.dsl.queries.insert

import io.kotgres.dsl.queries.insert.base.InsertQuery
import io.kotgres.dsl.queries.insert.base.OverrideType

class OverridingValueInsertQuery internal constructor(query: InsertQuery) : ValuesInsertQuery(query) {

    fun overridingSystemValue(): ValuesInsertQuery {
        overrideType = OverrideType.SYSTEM
        return ValuesInsertQuery(this)
    }

    fun overridingUserValue(): ValuesInsertQuery {
        overrideType = OverrideType.USER
        return ValuesInsertQuery(this)
    }


}