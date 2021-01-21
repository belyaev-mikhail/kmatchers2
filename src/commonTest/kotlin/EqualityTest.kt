package ru.spbstu.matchers

import ru.spbstu.matchers.rewrite.*
import ru.spbstu.matchers.rewrite.match
import kotlin.test.Test
import kotlin.test.assertEquals

class EqualityTest {
    @Test
    fun testEquality() {
        val data1 = Triple(1, "Hello", Pair(1, 2))

        val res1 = match(data1) with {
            case(Triple(_1(), _2(), Pair(_3(), _1()))) of {
                // fail
                1
            }
            case(Triple(_1(), _2(), Pair(_1(), _3()))) of {
                // success
                2
            }
        }
        assertEquals(2, res1)
    }
}