package com.example.navalbattlegameapplication.models

data class User(
    var fullname: String? = null,
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var active: Boolean? = false,
    var maxPoints: Int? = 0,
    var currentPoints: Int? = 0
){
    fun addPoints(points: Int){
        currentPoints = currentPoints?.plus(points)
    }

    fun tryToPlaceShip(board: Board, ship: Ship, startPoint: Point): Boolean{

        var shipCoord: Point

        for (i in 0 until ship.size){
            shipCoord = if(ship.orientation == Orientation.VERTICAL){
                Point(startPoint.x + i, startPoint.y)
            }
            else{
                Point(startPoint.x, startPoint.y + i )
            }
            ship.coords.add(shipCoord)
        }

        if (board.canPlaceShip(ship)){
            board.placeShip(ship)
            return true
        }
        else{
            ship.coords.clear()
            return false
        }

    }


}
