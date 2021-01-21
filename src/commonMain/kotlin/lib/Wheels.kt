package lib

import ru.spbstu.matchers.rewrite.MatchResult
import ru.spbstu.matchers.rewrite.Pattern
import ru.spbstu.wheels.*

fun <T1, T2, T3, T4, T5, T6, T> Option.Companion.just(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Option<T>> = Pattern { v, matchResult ->
    v.isNotEmpty() && value.unapply(v.get(), matchResult)
}

fun <T1, T2, T3, T4, T5, T6, T> Try.Companion.just(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Try<T>> = Pattern { v, matchResult ->
    v.isNotException() && value.unapply(v.getOrThrow(), matchResult)
}

fun <T1, T2, T3, T4, T5, T6> Try.Companion.exception(
    exception: Pattern<T1, T2, T3, T4, T5, T6, Exception>
): Pattern<T1, T2, T3, T4, T5, T6, Try<Nothing>> = Pattern { v, matchResult ->
    when(val vex = v.getExceptionOrNull()) {
        null -> false
        else -> exception.unapply(vex, matchResult)
    }
}

inline fun <T1, T2, T3, T4, T5, T6, reified T> Either.Companion.left(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Either<T, Nothing>> = Pattern { v, matchResult ->
    v.isLeft() && value.unapply(v.value, matchResult)
}

inline fun <T1, T2, T3, T4, T5, T6, reified T> Either.Companion.right(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Either<Nothing, T>> = Pattern { v, matchResult ->
    v.isRight() && value.unapply(v.value, matchResult)
}
