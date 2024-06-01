package com.link184.parameterized.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.reflect.KClass

class ParameterizedSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>
): SymbolProcessor {
    private var mustProcess = true
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val parametrizedTests = ParameterizedClassGenerator(resolver)
            .generate()
//        val parametrizedTests = resolver.getSymbolsWithAnnotation(ParameterizedTest::class.asClassName().canonicalName)
//            .filterIsInstance<KSClassDeclaration>()
//            .filter { ksClassDeclaration -> ksClassDeclaration.classKind == ClassKind.CLASS }
//            .filterNot(KSClassDeclaration::isAbstract)
//            .map { ksClassDeclaration ->
//                FileSpec.builder(ksClassDeclaration.packageName.asString(), ksClassDeclaration.simpleName.asString() + "_Generated")
//                    .addType(
//                        TypeSpec.classBuilder(ksClassDeclaration.simpleName.asString() + "_Generated")
//                            .addOriginatingKSFile(ksClassDeclaration.containingFile!!)
//                            .primaryConstructor(
//                                FunSpec.constructorBuilder()
//                                    .addParameters(
//                                        ksClassDeclaration.primaryConstructor?.parameters!!.map { ksValueParameter ->
//                                            ParameterSpec.builder(
//                                                ksValueParameter.name?.asString() ?: throw IllegalArgumentException("Invalid parameter name: ${ksValueParameter.name}"),
//                                                ksValueParameter.type.toTypeName()
//                                            ).build()
//                                        }
//                                    )
//                                    .build()
//                            )
//                            .build()
//                    )
//            }

        if (mustProcess) {
            parametrizedTests.forEach {
                it.build().writeTo(codeGenerator, aggregating = false)
            }
        }

        mustProcess = false

        return emptyList()
    }
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ParameterizedTest

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Parameters(val name: String = "testName", vararg val parameterType: KClass<Any>)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ParameterizedScenario(val name: String = "testName")