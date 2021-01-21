package ru.spbstu.matchers

import ru.spbstu.matchers.rewrite.*
import ru.spbstu.matchers.rewrite.match
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class GuardTest {
    @Test
    fun testGuards() {

        val res1 = match(1..20) with {
            case(Iterable(_1(), _2(), _3(), rest = any())) where { _2() > 5} of {
                assertNotEquals(2, _1())
                _1()
            }
            otherwise {
                2
            }
        }

        assertEquals(2, res1)

        val res2 = match(1..20) with {
            case(Iterable(_1(), _2(), _3(), rest = any())) where { _3() > 2 } of {
                _2()
            }
            otherwise {
                42
            }
        }
        assertEquals(2, res2)

        val res3 = match(Plus(Var("x"), Const(1))) with {
            case(Plus(Const(_1()), Var(_2()))) or case(Plus(Var(_2()), Const(_1()))) where { _1() != 0 } of {
                1
            }
            otherwise { 2 }
        }

        assertEquals(1, res3)

        val res4 = match(Plus(Const(1), Const(1))) with {

            (case(Plus(Const(_1()), Const(_2()))) where { _2() > _1() }) or
                    (case(Plus(Const(_1()), Const(_2()))) where { _1() > _2() }) of {
                        3
            }

            otherwise { 1 }

        }

        assertEquals(1, res4)

    }
}