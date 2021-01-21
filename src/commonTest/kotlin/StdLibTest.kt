package ru.spbstu.matchers

import ru.spbstu.matchers.rewrite.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class StdLibTest {
    @Test
    fun testPairs() {
        var visits = 0
        match(2 to "hello" to 3.14) with {
            case(Pair(Pair(_1(), _2()), _3())) of {
                ++visits
                assertEquals(2, _1())
                assertEquals("hello", _2())
                assertEquals(3.14, _3())
            }
            otherwise {
                fail()
            }
        }
        assertEquals(1, visits)
    }

    @Test
    fun testTriples() {
        var visits = 0
        match(Triple(1, "Hello", Triple(2, 3.14, "a"))) with {
            case(Triple(_1(), _2(), Triple(_3(), _4(), _5()))) of {
                ++visits
                assertEquals(1, _1())
                assertEquals("Hello", _2())
                assertEquals(2, _3())
                assertEquals(3.14, _4())
                assertEquals("a", _5())
            }
            otherwise {
                fail()
            }
        }
        assertEquals(1, visits)
    }

    @Test
    fun testIterable() {
        run {
            var visits = 0
            match((1..30)) with {
                case(Iterable(_1(), _2(), _3(), rest = _4())) of {
                    ++visits
                    assertEquals(1, _1())
                    assertEquals(2, _2())
                    assertEquals(3, _3())
                    assertEquals((4..30).toList(), _4().toList())
                }
                otherwise {  }
            }
            assertEquals(1, visits)
        }

        run {
            var visits = 0
            match((0..15).toList()) with {
                case(Collection(_1(), _2(), _3())) of {
                    fail()
                }
                case(Collection(_1(), rest = _2(), size = _3())) of {
                    ++visits
                    assertEquals(0, _1())
                    assertEquals((1..15).toList(), _2().toList())
                    assertEquals(16, _3())
                }
                otherwise { fail() }
            }
            assertEquals(1, visits)
        }
    }

    @Test
    fun testResult() {
        // due to Result bugs this does not work =(
//        val res = kotlin.runCatching { 2 }
//        val recheck = match(res) with {
//            case(Result.successPattern(_1())) of {
//                _1()
//            }
//        }
//        assertEquals(2, recheck)


    }
}