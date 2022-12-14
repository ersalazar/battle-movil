package com.example.navalbattlegameapplication.models

enum class Orientation {
    HORIZONTAL{
        override fun getFinishPoint(startPoint: Point?, stepsToMove: Int): Point {
            val point = Point(startPoint!!.x + stepsToMove, startPoint!!.y)
            return point
        }
              },
    VERTICAL{
        override fun getFinishPoint(startPoint: Point?, stepsToMove: Int): Point {
            val point = Point(startPoint!!.x , startPoint!!.y + stepsToMove)
            return point
        }
    };

    abstract fun getFinishPoint(startPoint: Point?, stepsToMove: Int ): Point
}