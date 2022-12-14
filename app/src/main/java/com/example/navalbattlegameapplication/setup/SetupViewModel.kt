package com.example.navalbattlegameapplication.setup

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navalbattlegameapplication.models.*

class SetupViewModel : ViewModel() {

    var board = Board()
    var ships: ArrayList<Ship> = arrayListOf()

    var player = User()
    var opponent = User()

    private var selectedShip: Ship? = null
    private var shipDirection = Orientation.VERTICAL

    var shipListVisibility = false
    var startGameVisibility = false

    val refreshBoardLiveData = MutableLiveData<Board>()
    val refreshShipsLiveData = MutableLiveData<ArrayList<Ship>>()
    val shipsLiveData = MutableLiveData<ArrayList<Ship>>()
    val blinkLiveData = MutableLiveData<View>()

    fun rotateShip() {
        shipDirection = if (shipDirection == Orientation.VERTICAL)
            Orientation.HORIZONTAL
        else
            Orientation.VERTICAL
        selectedShip?.orientation = shipDirection
    }

    fun initShips() {
        ships = arrayListOf()
        ships.apply {
            add(Ship(points = 100, size = 4))
            add(Ship(points = 50, size = 3))
            add(Ship(points = 50, size = 3))
            add(Ship(points = 25, size = 2))
            add(Ship(points = 25, size = 2))
        }
        shipsLiveData.value = ships
    }

    fun selectedShip(ship: Ship) {
        selectedShip = ship
        selectedShip?.orientation = shipDirection
    }

    fun handleBoardClick(view: View, position: Int) {
        val x: Int = kotlin.math.floor((position / board.width).toDouble()).toInt()
        val y: Int = position % board.width

        if (selectedShip != null) {

            val ship = selectedShip

            if (player.tryToPlaceShip(board, ship!!, Point(x, y))){

                ships.remove(ship)
                selectedShip = null

                refreshBoardLiveData.value = board
                refreshShipsLiveData.value = ships

            } else {
                blinkLiveData.value = view
                ship!!.coords.clear()
            }
        }
    }

    fun isShipListEmpty(): Boolean {
        return ships.isEmpty()
    }




}