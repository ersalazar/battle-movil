package com.example.navalbattlegameapplication.setup

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ListView
import androidx.lifecycle.Observer
import com.example.navalbattlegameapplication.GameActivity
import com.example.navalbattlegameapplication.R
import com.example.navalbattlegameapplication.internal.getViewModel
import com.example.navalbattlegameapplication.models.Board
import com.example.navalbattlegameapplication.models.Ship
import com.example.navalbattlegameapplication.setup.adapters.BoardGridAdapter
import com.example.navalbattlegameapplication.setup.adapters.ShipListAdapter
import kotlinx.coroutines.*


class SetupActivity : AppCompatActivity(), Animation.AnimationListener{

    companion object {
        const val BOARD = "BOARD"
        const val SHIPS = "SHIPS"
    }

    private lateinit var shipAdapter: ShipListAdapter
    private lateinit var boardAdapter: BoardGridAdapter

    private lateinit var manualButton: Button
    private lateinit var rotateButton: Button
    private lateinit var startButton: Button
    private lateinit var shipListView: ListView

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private val viewModel by lazy {
        getViewModel { SetupViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        setContentView(R.layout.activity_setup)

        title = "Coloca tus barcos"
        initObservers()
        initBoardAdapter()
        initShipAdapter()



        viewModel.player = intent.getParcelableExtra(PLAYER)
        viewModel.opponent = intent.getParcelableExtra(OPPONENT)



        manualButton.setOnClickListener {
            viewModel.initShips()
            manualButton.visibility = View.GONE;
            viewModel.shipListVisibility = true
            updateVisibility()
        }

        rotateButton.setOnClickListener {
            viewModel.rotateShip()
        }

        startButton.setOnClickListener {

            val bundle = Bundle()
            bundle.putString(ROOM_NAME, viewModel.roomName)
            bundle.putString(ROLE_NAME, viewModel.roleName)
            bundle.putParcelable(ME_PLAYER, viewModel.myPlayer)
            bundle.putParcelable(VS_PLAYER, viewModel.vsPlayer)
            bundle.putSerializable(BOARD, viewModel.board.fieldStatus)
            bundle.putParcelableArrayList(FLEET, viewModel.board.fleet)

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtras(bundle)
            this.startActivity(intent)
            finish()
        }

    }

    private fun initObservers() {
        viewModel.apply {
            refreshBoardLiveData.observe(this@SetupActivity,
                Observer { board -> refreshBoard(board) })
            shipsLiveData.observe(this@SetupActivity,
                Observer { ships -> addDataToShipAdapter(ships) })
            blinkLiveData.observe(this@SetupActivity,
                Observer { view -> setBlinkAnimation(view) })
            refreshShipsLiveData.observe(this@SetupActivity,
                Observer { shipList -> refreshShips(shipList) })
        }
    }

    override fun onResume() {
        super.onResume()
        updateVisibility()
    }

    private fun updateVisibility() {
        if (!viewModel.startGameVisibility) {
            shipsLayout.visibility = if (viewModel.shipListVisibility) View.VISIBLE else View.GONE
            manualButton.visibility = if (viewModel.shipListVisibility) View.GONE else View.VISIBLE
        } else {
            startButton.visibility = View.VISIBLE
        }
    }

    private fun refreshBoard(board: Board) {
        boardAdapter.refresh(board.coordStatus)
        manualButton.visibility = View.GONE;
    }

    private fun initBoardAdapter() {
        boardAdapter =
            BoardGridAdapter(
                this,
                viewModel.board.coordStatus
            )
            { view: View, position: Int -> handleBoardClick(view, position) }

        boardGridView.adapter = boardAdapter
    }


    private fun addDataToShipAdapter(ships: ArrayList<Ship>) {
        shipAdapter.refreshShipList(ships)
    }

    private fun refreshShips(shipList: ArrayList<Ship>) {
        shipAdapter.selectedPosition = -1
        shipAdapter.refreshShipList(shipList)

        if (viewModel.isShipListEmpty()) {
            rotateButton.visibility = View.GONE
            startButton.visibility = View.VISIBLE
            viewModel.startGameVisibility = true
        }
    }

    private fun initShipAdapter() {
        shipAdapter = ShipListAdapter(this)
        shipListView.adapter = shipAdapter

        shipListView.setOnItemClickListener { _, _, position, _ ->
            val selectedShip = shipAdapter.getItem(position) as Ship
            viewModel.selectedShip(selectedShip)
            shipAdapter.selectedPosition = position;
            shipAdapter.notifyDataSetChanged();
        }
    }

    private fun handleBoardClick(view: View, position: Int) {
        viewModel.handleBoardClick(view, position)
    }

    private fun setBlinkAnimation(view: View) {
        val animBlink: Animation =
            AnimationUtils.loadAnimation(this@SetupActivity, R.anim.blink_in);
        animBlink.setAnimationListener(this@SetupActivity)

        view.startAnimation(animBlink)

        scope.launch {
            delay(500)
            view.clearAnimation()
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onAnimationRepeat(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {

    }

    override fun onAnimationStart(animation: Animation?) {

    }

}
