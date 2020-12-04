package com.carswaddle.carswaddleandroid.Extensions

public fun <T> List<T>.safeFirst(): T? {
    if (isEmpty())
        return null
    return this[0]
}

public fun <T> List<T>.safeObject(index: Int): T? {
    if (index >= count())
        return null
    return this[index]
}