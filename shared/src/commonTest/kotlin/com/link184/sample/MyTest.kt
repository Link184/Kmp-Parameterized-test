package com.link184.sample

import kotlin.test.Test

annotation class MyField

class MyTest {

    @MyField
    lateinit var myField: String

    @Test
    fun test() {


//        throw IllegalStateException()
    }
}

//class TestRunner: TestE