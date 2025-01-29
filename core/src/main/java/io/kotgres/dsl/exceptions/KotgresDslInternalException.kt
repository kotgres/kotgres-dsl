package io.kotgres.dsl.exceptions


class KotgresDslInternalException(msg: String) :
    DslException("$msg. This should not happen. Please report this error through Github issues.")

