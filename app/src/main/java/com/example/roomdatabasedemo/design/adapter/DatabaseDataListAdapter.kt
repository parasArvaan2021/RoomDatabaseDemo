package com.example.roomdatabasedemo.design.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabasedemo.R
import com.example.roomdatabasedemo.design.inter.DatabaseItemInterface
import com.example.roomdatabasedemo.model.Repository

class DatabaseDataListAdapter(
    private val context: Context,
    private var listOfDatabaseAllData: List<Repository>,
    private val databaseItemInterface: DatabaseItemInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.list_item_database_data, parent, false)
        return ViewHolderForDatabaseList(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder1 = holder as ViewHolderForDatabaseList
        holder1.txtEmail.text = listOfDatabaseAllData[position].userEmail
        holder1.txtName.text = listOfDatabaseAllData[position].userName
        Log.i("TAG", "getItemCount: ${listOfDatabaseAllData.size}")


        holder1.itemView.setOnClickListener {
            databaseItemInterface.recyclerItemClickListener(listOfDatabaseAllData[position])
        }

    }

    fun onGetListener(listOfDatabase: List<Repository>) {
        listOfDatabaseAllData = listOfDatabase
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        Log.e("TAG", "getItemCount111: ${listOfDatabaseAllData.size}")
        return listOfDatabaseAllData.size
    }

    inner class ViewHolderForDatabaseList(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtShowName)
        val txtEmail: TextView = itemView.findViewById(R.id.txtShowEmail)
        val clParent: ConstraintLayout = itemView.findViewById(R.id.clParent)
    }
}