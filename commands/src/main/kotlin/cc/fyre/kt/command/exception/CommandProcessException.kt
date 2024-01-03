package cc.fyre.kt.command.exception

import cc.fyre.kt.command.Command

class CommandProcessException(val command: Command,val error: ErrorType,vararg val parameters: Any?) : RuntimeException() {

    enum class ErrorType {

        COOLDOWN,
        NO_PERMISSION,

        PARAMETER_INVALID,
        PARAMETER_CONVERSION,
        PARAMETER_COUNT_INSUFFICIENT,
        PARAMETER_MISSING_SEPARATOR,

        ANNOTATION_INVALID,
        ANNOTATION_CONVERSION,

        NO_FUNCTION_OR_HELPER;
    }

}