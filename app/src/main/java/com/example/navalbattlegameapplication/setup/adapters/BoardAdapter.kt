package com.example.navalbattlegameapplication.setup.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.navalbattlegameapplication.R

class BoardGridAdapter(private val context: Context, private val fieldStatus: Array<Array<Int>>,
                       private val clickListener: (View, Int) -> Unit): BaseAdapter() {

    var fields = fieldStatus.flatten()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // Inflate the custom view
        val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.board_cell,null)

        val grid: GridView = parent as GridView
        val size = grid.columnWidth

        // Get the custom view widgets reference
        val cell = view.findViewById<TextView>(R.id.cellView)

        // Display color on view
        cell.setBackgroundColor(ContextCompat.getColor(context, getStatusColor(fields[position])))

        cell.width = size
        cell.height = size

        cell.setOnClickListener { clickListener(cell, position) }

        // Finally, return the view
        return view
    }


    private fun getStatusColor(status: Int): Int {
        return when (status) {
            0 -> Color.WHITE
            1 -> Color.BLUE
            2 -> Color.GRAY
            else -> {
                Color.RED
            }
        }
    }


    override fun getCount(): Int {
        return fields.size
    }

    override fun getItem(position: Int): Any {
        return fields[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    fun refresh(fieldStatus: Array<Array<Int>>) {
        fields = fieldStatus.flatten()
        notifyDataSetChanged()
    }


}

