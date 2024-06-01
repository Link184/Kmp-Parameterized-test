package com.link184.parameterized.compiler

import com.squareup.kotlinpoet.FileSpec

interface Generator {
    fun generate(): Collection<FileSpec.Builder>
}