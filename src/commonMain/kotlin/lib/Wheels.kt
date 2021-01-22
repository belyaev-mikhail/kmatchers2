package ru.spbstu.matchers.rewrite

import ru.spbstu.matchers.rewrite.MatchResult
import ru.spbstu.matchers.rewrite.Pattern
import ru.spbstu.matchers.rewrite.allWithIndex
import ru.spbstu.matchers.rewrite.any
import ru.spbstu.wheels.*

fun <T1, T2, T3, T4, T5, T6, T> Option.Companion.justPattern(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Option<T>> = Pattern { v, matchResult ->
    v.isNotEmpty() && value.unapply(v.get(), matchResult)
}

fun <T1, T2, T3, T4, T5, T6> Option.Companion.nothingPattern(): Pattern<T1, T2, T3, T4, T5, T6, Option<Nothing>> =
    Pattern.simple { it.isEmpty() }

fun <T1, T2, T3, T4, T5, T6, T> Try.Companion.justPattern(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Try<T>> = Pattern { v, matchResult ->
    v.isNotException() && value.unapply(v.getOrThrow(), matchResult)
}

fun <T1, T2, T3, T4, T5, T6> Try.Companion.exceptionPattern(
    exception: Pattern<T1, T2, T3, T4, T5, T6, Exception>
): Pattern<T1, T2, T3, T4, T5, T6, Try<Nothing>> = Pattern { v, matchResult ->
    when(val vex = v.getExceptionOrNull()) {
        null -> false
        else -> exception.unapply(vex, matchResult)
    }
}

inline fun <T1, T2, T3, T4, T5, T6, reified T> Either.Companion.leftPattern(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Either<T, Nothing>> = Pattern { v, matchResult ->
    v.isLeft() && value.unapply(v.value, matchResult)
}

inline fun <T1, T2, T3, T4, T5, T6, reified T> Either.Companion.rightPattern(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Either<Nothing, T>> = Pattern { v, matchResult ->
    v.isRight() && value.unapply(v.value, matchResult)
}

fun <T1, T2, T3, T4, T5, T6> IntBits(
    vararg bits: Pattern<T1, T2, T3, T4, T5, T6, Int>,
    value: Pattern<T1, T2, T3, T4, T5, T6, Int> = any(),
    popCount: Pattern<T1, T2, T3, T4, T5, T6, Int>? = null,
    lowestBitSet: Pattern<T1, T2, T3, T4, T5, T6, IntBits>? = null,
    highestBitSet: Pattern<T1, T2, T3, T4, T5, T6, IntBits>? = null,
    numberOfLeadingZeros: Pattern<T1, T2, T3, T4, T5, T6, Int>? = null,
    numberOfTrailingZeros: Pattern<T1, T2, T3, T4, T5, T6, Int>? = null
): Pattern<T1, T2, T3, T4, T5, T6, IntBits> = Pattern { v, matchResult ->
    val res = value.unapply(v.data, matchResult) && bits.allWithIndex { ix, e -> e.unapply(v[ix].toBit(), matchResult) }
    if (!res) return@Pattern false

    if (popCount?.unapply(v.popCount, matchResult) == false) return@Pattern false
    if (lowestBitSet?.unapply(v.lowestBitSet, matchResult) == false) return@Pattern false
    if (highestBitSet?.unapply(v.highestBitSet, matchResult) == false) return@Pattern false
    if (numberOfLeadingZeros?.unapply(v.numberOfLeadingZeros, matchResult) == false) return@Pattern false
    if (numberOfTrailingZeros?.unapply(v.numberOfLeadingZeros, matchResult) == false) return@Pattern false

    return@Pattern true
}
