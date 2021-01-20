package ru.spbstu.matchers

import kotlinx.warnings.Warnings
import ru.spbstu.matchers.rewrite.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InlineTest {
    @Test
    @Suppress(Warnings.USELESS_IS_CHECK)
    fun testInlineForm() {
        val m1 = match(Plus(Var("x"), Const(0)), case(Plus(Var(_1()), Const(_2()))))
        assertNotNull(m1)
        with(m1) {
            assertTrue(_1() is String)
            assertTrue(_2() is Int)
            assertEquals("x", _1())
            assertEquals(0, _2())
        }

        val m2 = match(Plus(Var("x"), Const(0)), Plus(Var(_1()), Const(_2())))
        assertNotNull(m2)
        with(m2) {
            assertTrue(_1() is String)
            assertTrue(_2() is Int)
            assertEquals("x", _1())
            assertEquals(0, _2())
        }
    }

    @Test
    @Suppress(Warnings.USELESS_IS_CHECK)
    fun testInlineForced() {
        val m1 = matchForced(
            Triple(1, "Hello", Pair(3.14, 29)),
            case(Triple(_1(), any(), Pair(second = _2())))
        )
        with(m1) {
            assertTrue(_1() is Int)
            assertTrue(_2() is Int)
            assertEquals(1, _1())
            assertEquals(29, _2())
        }

        val m2 = matchForced(
            Triple(1, "Hello", Pair(3.14, 29)),
            Triple(_1(), any(), Pair(second = _2()))
        )
        with(m2) {
            assertTrue(_1() is Int)
            assertTrue(_2() is Int)
            assertEquals(1, _1())
            assertEquals(29, _2())
        }
    }

}