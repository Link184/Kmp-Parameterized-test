package com.link184.parameterized.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.jvm.throws
import com.squareup.kotlinpoet.ksp.writeTo

class ParameterizedSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>
): SymbolProcessor {
    private var invoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val greeterClass = ClassName("", "Greeter")
        val file = FileSpec.builder("com.link184.sample", "HelloWorld")
            .addType(
                TypeSpec.classBuilder("Greeter")
                    .addFunction(
                        FunSpec.builder("greet")
                            .addAnnotation(ClassName("kotlin.test", "Test"))
                            .addStatement("println(%P)", "Hello, name")
                            .addStatement("throw OutOfMemoryError(\"From generated class\")")
                            .build()
                    )
                    .build()
            )
            .build()

        if (!invoked) {
            file.writeTo(codeGenerator, aggregating = false)
        }

        invoked = true

        return emptyList()
    }
}

annotation class ParameterizedTest