package ru.spbstu.matchers.rewrite

import kotlinx.warnings.Warnings
import ru.spbstu.wheels.getOption
import ru.spbstu.wheels.getOrElse

import org.intellij.lang.annotations.Language

fun <T1, T2, T3, T4, T5, T6, Arg> Iterable(
    vararg first: Pattern<T1, T2, T3, T4, T5, T6, Arg>,
    rest: Pattern<T1, T2, T3, T4, T5, T6, Iterable<Arg>>? = null
): Pattern<T1, T2, T3, T4, T5, T6, Iterable<Arg>> = Pattern { value, matchResult ->
    val iter = value.iterator()
    for (element in first) {
        if (!iter.hasNext()) return@Pattern false
        if (!element.unapply(iter.next(), matchResult)) return@Pattern false
    }
    when (rest) {
        null -> !iter.hasNext()
        else -> rest.unapply(kotlin.collections.Iterable { iter }, matchResult)
    }
}

fun <T1, T2, T3, T4, T5, T6, Arg> Collection(
    vararg first: Pattern<T1, T2, T3, T4, T5, T6, Arg>,
    rest: Pattern<T1, T2, T3, T4, T5, T6, Iterable<Arg>>? = null,
    size: Pattern<T1, T2, T3, T4, T5, T6, Int>? = null
): Pattern<T1, T2, T3, T4, T5, T6, Collection<Arg>> = Pattern { value, matchResult ->
    if (size?.unapply(value.size, matchResult) == false) return@Pattern false

    Iterable(first = first, rest = rest).unapply(value, matchResult)
}

fun <T1, T2, T3, T4, T5, T6, A, B> Pair(
    first: Pattern<T1, T2, T3, T4, T5, T6, A> = any(),
    second: Pattern<T1, T2, T3, T4, T5, T6, B> = any()
): Pattern<T1, T2, T3, T4, T5, T6, Pair<A, B>> = first.divideWith(second, { it.first }, { it.second })

fun <T1, T2, T3, T4, T5, T6, A, B, C> Triple(
    first: Pattern<T1, T2, T3, T4, T5, T6, A> = any(),
    second: Pattern<T1, T2, T3, T4, T5, T6, B> = any(),
    third: Pattern<T1, T2, T3, T4, T5, T6, C> = any()
): Pattern<T1, T2, T3, T4, T5, T6, Triple<A, B, C>> =
    Pattern { value, matchResult ->
        first.unapply(value.first, matchResult) &&
                second.unapply(value.second, matchResult) &&
                third.unapply(value.third, matchResult)
    }

fun <T1, T2, T3, T4, T5, T6, K, V> mapContaining(
    vararg entries: Pair<K, Pattern<T1, T2, T3, T4, T5, T6, V>>
): Pattern<T1, T2, T3, T4, T5, T6, Map<K, V>> =
    Pattern { value, matchResult ->
        var res = true
        for ((k, p) in entries) {
            @Suppress(Warnings.UNCHECKED_CAST)
            val v = value.getOption(k).getOrElse { return@Pattern false }
            res = res && p.unapply(v, matchResult)
        }
        res
    }

fun Pattern.Companion.regex(
    @Language("regexp") re: String
) = viewMany<CharSequence, String> { cs ->
    Regex(re).matchEntire(cs)?.groupValues?.let { it.subList(1, it.size) } ?: emptyList()
}

fun <T1, T2, T3, T4, T5, T6, T> Result.Companion.success(
    value: Pattern<T1, T2, T3, T4, T5, T6, T>
): Pattern<T1, T2, T3, T4, T5, T6, Result<T>> = Pattern { v, matchResult ->
    v.isSuccess && value.unapply(v.getOrThrow(), matchResult)
}

fun <T1, T2, T3, T4, T5, T6> Result.Companion.failure(
    value: Pattern<T1, T2, T3, T4, T5, T6, Throwable>
): Pattern<T1, T2, T3, T4, T5, T6, Result<Nothing>> = Pattern { v, matchResult ->
    v.isFailure && value.unapply(v.exceptionOrNull()!!, matchResult)
}


