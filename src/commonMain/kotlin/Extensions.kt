package ru.spbstu.matchers.rewrite

inline fun <T> Iterable<T>.allWithIndex(body: (Int, T) -> Boolean): Boolean {
    var ix = 0
    for (e in this) {
        if (!body(ix, e)) return false
        ++ix
    }
    return true
}

inline fun <T> Array<T>.allWithIndex(body: (Int, T) -> Boolean): Boolean {
    var ix = 0
    for (e in this) {
        if (!body(ix, e)) return false
        ++ix
    }
    return true
}

fun Boolean.toBit(): Int = when(this) {
    true -> 1
    false -> 0
}
