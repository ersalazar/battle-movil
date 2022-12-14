package com.example.navalbattlegameapplication.models

data class Point(
    val x: Int,
    val y: Int
    )
{
    fun isSamePoint(point: Point): Boolean{
        return this.x == point.x  && this.y == point.y
    }
}
