package io.kotgres.dsl.exceptions

import io.kotgres.dsl.internal.extensions.capitalizeFirstLetter

class DslUnexpectedQueryBlockTypeException(val type: String) :
    DslException("${type.capitalizeFirstLetter()} statement only accepts IQueryBlock, String or Raw queries")
