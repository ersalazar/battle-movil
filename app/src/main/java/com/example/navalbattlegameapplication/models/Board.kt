package com.example.navalbattlegameapplication.models

import kotlinx.coroutines.handleCoroutineException

data class Board(

    val width: Int = 8,
    val height: Int = 8,
    var ships: ArrayList<Ship> = ArrayList(),
    // genera un array de width x height, señalando las coordenadas
    // va a mostrar el estado de cada coordenada en el campo
    // 0: coordenada vacia, no le han tirado / 1: vacia pero ya le tiraron (fallada)
    // / 2: no esta vacia, pero no le han tirado (con  barco) / 3: no esta vacia y ya le dieron (tiro atinado)
    var coordStatus: Array<Array<Int>> = Array(width) {_ -> Array(height) {_ -> 0} }
){
    val activeShips =  ships.filter { !it.shipDown() }
    val downShips = ships.filter { it.shipDown() }


    // ver si se puede colocar un barco en el tablero
    fun canPlaceShip(ship: Ship): Boolean{
        val iterator: Iterator<Point> = ship.coords.iterator()
        //iteramos sobre todas las coordenadas del barco
        while (iterator.hasNext()){
            val boardCoord: Point = iterator.next()

            val coord_x = boardCoord.x
            val coord_y = boardCoord.y

            //checa que la coordenada no esté siendo ocupada
            if (coordStatus[coord_x][coord_y] != 0){
                return false
            }
            //checa que la coordenada sea valida
            if (coord_x >= width || coord_x < 0 || coord_y >= height || coord_y < 0){
                return false
            }
        }
        return true
    }

    fun placeShip(ship: Ship) {
        val iterator: Iterator<Point> = ship.coords.iterator()

        while (iterator.hasNext()){

            val boardCoord: Point = iterator.next()

            val coord_x = boardCoord.x
            val coord_y = boardCoord.y
            /*
            if (coordStatus[coord_x][coord_y] != 2){
                throw
            }
            */

            //Se asigna la coordenada como ocupada pero sin haber recibido un tiro
            coordStatus[coord_x][coord_y] = 2
        }
        //añadimos el barco a la lista del tablero
        ships.add(ship)

    }

    fun canShoot(coord: Point): Boolean {
        val coord_x = coord.x
        val coord_y = coord.y

        //Si el valor es 0 o dos si se puede realizar el tiro
        return coordStatus[coord_x][coord_y] == 0 || coordStatus[coord_x][coord_y] == 2
    }

    fun placeShot(coord: Point): Int{
        val coord_x = coord.x
        val coord_y = coord.y

        if  (!(coordStatus[coord_x][coord_y] == 0 || coordStatus[coord_x][coord_y] == 2)){
            return -1
        }

        if (coordStatus[coord_x][coord_y] == 0){
            coordStatus[coord_x][coord_y] = 1
            return coordStatus[coord_x][coord_y]
        }

        else {
            coordStatus[coord_x][coord_y] = 3
            for (ship in this.ships){
                if (ship.ShipIsHit(coord)){
                    return coordStatus[coord_x][coord_y]
                }
            }
            return -1
        }

    }

    fun isGameOver(): Boolean {
        return (downShips.size == ships.size)
    }
}
