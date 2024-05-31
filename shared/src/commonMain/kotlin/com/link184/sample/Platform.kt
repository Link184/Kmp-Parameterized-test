package com.link184.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform