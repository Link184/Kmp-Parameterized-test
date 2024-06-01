package com.link184.parameterized.compiler

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName

class ParameterizedClassGenerator(
    private val resolver: Resolver
): Generator {
    @OptIn(KspExperimental::class)
    override fun generate(): Collection<FileSpec.Builder> {
        return resolver.parameterizedTestKsClassDeclaration
            .associateBy { it.parametersFunction }
            .map { (parametersFunction, classDeclaration) ->
                FileSpec.builder(classDeclaration.generatedClassName())
                    .addType(
                        TypeSpec
                            .classBuilder(classDeclaration.generatedClassName())
                            .addProperty(parametersFunction.propertySpec)
                            .addFunction(
                                FunSpec.builder(parametersFunction.getAnnotationsByType(Parameters::class).first().name)
                                    .addAnnotation(ClassName("kotlin.test", "Test"))
//                                    .addStatement("val ${classDeclaration.simpleName.asString()} = %T()", classDeclaration.toClassName())
                                    .build()
                            )
                            .build()
                    )

            }
    }

    private fun KSClassDeclaration.generatedClassName(): ClassName {
        return ClassName(packageName.asString(), simpleName.asString() + "_Generated")
    }

    private val Resolver.parameterizedTestKsClassDeclaration: Sequence<KSClassDeclaration>
        get() = getSymbolsWithAnnotation(ParameterizedTest::class.asClassName().canonicalName)
            .filterIsInstance<KSClassDeclaration>()
            .filter { ksClassDeclaration -> ksClassDeclaration.classKind == ClassKind.CLASS }
            .filterNot(KSClassDeclaration::isAbstract)

    @OptIn(KspExperimental::class)
    private val KSClassDeclaration.parametersFunction get() = containingFile!!
        .declarations
        .filterIsInstance<KSFunctionDeclaration>()
        .filter { it.isAnnotationPresent(Parameters::class) }
        .also {
            if (it.count() > 1) {
                throw IllegalArgumentException("There must be only one element annotated with @${Parameters::class.qualifiedName}")
            }
            if (it.count() < 1) {
                throw IllegalArgumentException("There must be at least one element annotated with @${Parameters::class.qualifiedName}")
            }
        }
        .first()

    private val KSFunctionDeclaration.propertySpec get() = PropertySpec
        .builder("_${simpleName.asString()}", returnType!!.toTypeName())
        .addModifiers(KModifier.PRIVATE)
        .initializer("%M()", MemberName(packageName.asString(), simpleName.asString()))
        .build()
}