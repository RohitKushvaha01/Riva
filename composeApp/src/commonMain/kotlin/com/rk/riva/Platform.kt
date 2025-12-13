package com.rk.riva

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform