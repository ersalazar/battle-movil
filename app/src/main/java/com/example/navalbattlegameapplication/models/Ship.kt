package com.example.navalbattlegameapplication.models

data class Ship(
    val size: Int,
    val points: Int,
    var orientation: Orientation = Orientation.VERTICAL,
    var coords: ArrayList<Point> = arrayListOf()
    )
    {

        fun shipDown(): Boolean {
            return coords.isEmpty()
        }

        //checa si un tiro le da al barco y lo registra en caso de que si
        fun ShipIsHit(shot: Point?): Boolean{
            val coordIterator = coords.iterator()
            for (i in coordIterator) {
                if (i.isSamePoint(shot!!)){
                    coordIterator.remove()
                    return true
                }
            }
            return false
        }

        fun hasCoord(coordinate: Point?): Boolean{
            for (coord in coords){
                if (coord.isSamePoint(coordinate!!)){
                    return true
                }
            }
            return false
        }


    }

