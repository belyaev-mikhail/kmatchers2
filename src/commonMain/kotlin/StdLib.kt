package ru.spbstu.matchers.rewrite

import kotlinx.warnings.Warnings

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
    first: Pattern<T1, T2, T3, T4, T5, T6, A>,
    second: Pattern<T1, T2, T3, T4, T5, T6, B>
): Pattern<T1, T2, T3, T4, T5, T6, Pair<A, B>> = first.divideWith(second, { it.first }, { it.second })

fun <T1, T2, T3, T4, T5, T6, A, B, C> Triple(
    first: Pattern<T1, T2, T3, T4, T5, T6, A>,
    second: Pattern<T1, T2, T3, T4, T5, T6, B>,
    third: Pattern<T1, T2, T3, T4, T5, T6, C>
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
            val v = value[k] ?: if (k in value) (null as V) else return@Pattern false
            res = res && p.unapply(v, matchResult)
        }
        res
    }