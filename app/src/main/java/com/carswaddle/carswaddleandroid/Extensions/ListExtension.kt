package com.carswaddle.carswaddleandroid.Extensions

public fun <T> List<T>.safeFirst(): T? {
    if (isEmpty())
        return null
    return this[0]
}