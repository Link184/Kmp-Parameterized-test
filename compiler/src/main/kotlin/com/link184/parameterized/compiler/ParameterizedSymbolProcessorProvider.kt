package com.link184.parameterized.compiler

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal lateinit var LOGGER: KSPLogger

class ParameterizedSymbolProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        LOGGER = environment.logger
        return ParameterizedSymbolProcessor(
            environment.codeGenerator,
            environment.options
        )
    }
}