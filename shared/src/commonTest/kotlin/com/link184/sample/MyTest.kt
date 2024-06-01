package com.link184.sample

import com.link184.parameterized.compiler.ParameterizedTest
import com.link184.parameterized.compiler.Parameters
import com.link184.sample.complex.model.ComplexObject
import org.junit.runners.Parameterized

@Parameterized.Parameters
fun params() {

}


@Parameters()
fun myTopLevelDeclarationParams() = arrayOf<Any>()

@ParameterizedTest
class MyTest(
    val input: String,
    val output: ComplexObject
) {

    @Parameters
    fun myClassParams() {

    }

    lateinit var myField: String

    fun test() {


//        throw IllegalStateException()
    }
}

