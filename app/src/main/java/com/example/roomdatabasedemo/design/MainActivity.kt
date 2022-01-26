package com.example.roomdatabasedemo.design

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabasedemo.R
import com.example.roomdatabasedemo.data.Database
import com.example.roomdatabasedemo.design.adapter.DatabaseDataListAdapter
import com.example.roomdatabasedemo.design.inter.DatabaseItemInterface
import com.example.roomdatabasedemo.model.Repository
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), DatabaseItemInterface {

    private var isUpdate = false
    private lateinit var userName: EditText
    private  lateinit var userEmail: EditText
    private lateinit var rvDatabaseDataList: RecyclerView
    private lateinit var btnShowData: Button
    private lateinit var btnOk: Button
    private lateinit var databaseDataListAdapter: DatabaseDataListAdapter
    private lateinit var btnAllDelete: Button
    private lateinit var btnInsertAllUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userName = findViewById(R.id.edtName)
        userEmail = findViewById(R.id.edtEmail)
        btnShowData = findViewById(R.id.btnShowData)
        btnOk = findViewById(R.id.btnOk)
        btnAllDelete = findViewById(R.id.btnAllDelete)
        btnInsertAllUser = findViewById(R.id.btnInsertAllUser)
        rvDatabaseDataList = findViewById(R.id.rvMainGetList)
        btnOk.text = "Insert"

        val insertUserList = arrayListOf<Repository>()

//        insertUserList= arrayListOf(
//            Repository(userEmail = "paras email1",
//                userName = "paras name1",
//                userId = null),
//            Repository(userEmail = "paras email2",
//                userName = "paras name2",
//                userId = null),
//            Repository(userEmail = "paras email3",
//                userName = "paras name3",
//                userId = null),
//            Repository(userEmail = "paras email4",
//                userName = "paras name4",
//                userId = null)
//        )

        for (i in 1..10) {
            insertUserList.add(
                Repository(
                    userEmail = "paras email $i",
                    userName = "paras name $i",
                    userId = null,
                    number = null
                )
            )

        }
        val db = Database.getDatabase(this)
        databaseDataListAdapter =
            DatabaseDataListAdapter(this@MainActivity, arrayListOf(), this@MainActivity)

        rvDatabaseDataList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = databaseDataListAdapter
        }

        btnOk.setOnClickListener {
            if (userName.text.toString().isNotEmpty() && userEmail.text.toString().isNotEmpty()) {

                if (!isUpdate) {
                    insertUser(db)

                } else {
                    updateUser(db)

                }
            } else {
                Toast.makeText(this, "Enter value", Toast.LENGTH_SHORT).show()
            }
        }
        btnShowData.setOnClickListener {
            getAllUser(db)
        }
        btnAllDelete.setOnClickListener {

            allDeleteUser(db)
        }
        btnInsertAllUser.setOnClickListener {
            insertAllUser(insertUserList, db)
        }
    }

    private fun insertUser(db: Database) {
        val user = Repository(
            userName = userName.text.toString(),
            userEmail = userEmail.text.toString(),
            userId = null,
            number = null
        )

        doAsync {
            // Put the student in database
            db.userDao().insertUser(user)

            uiThread {
                Toast.makeText(
                    this@MainActivity,
                    "One record inserted.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                btnOk.text = "Insert"
                isUpdate = false
                userEmail.setText("")
                userName.setText("")
                userName.setTag("")
                btnShowData.performClick()
            }
        }
    }

    private fun allDeleteUser(db: Database) {

        doAsync {
            db.userDao().deleteAllUSer()
            uiThread {
                btnShowData.performClick()
            }
        }
    }

    private fun deleteUser(db: Database, user: Repository) {
        if (user.userId != null && user.userId.toString().isNotEmpty()) {
            doAsync {
                db.userDao().deleteUser(user.userId.toString().toInt())
                btnShowData.performClick()
                rvDatabaseDataList.adapter?.notifyDataSetChanged()
                uiThread {

                }
            }


        }
    }

    private fun updateUser(db: Database) {
        if (userName.getTag() != null && !userName.getTag().toString().isEmpty()) {
            val user = Repository(
                userName = userName.text.toString(), userEmail = userEmail.text.toString(),
                userId = userName.getTag().toString().toInt(),number = null
            )

            doAsync {
                // Put the student in database
                db.userDao().update(user)

                uiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "One record updated.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    btnOk.text = "Insert"
                    isUpdate = false
                    userEmail.setText("")
                    userName.setText("")
                    userName.setTag("")
                    btnShowData.performClick()

                }
            }

        }
    }

    private fun getAllUser(db: Database) {
        doAsync {
            val list = db.userDao().getAllData()
            Log.e("TAG", "onCreate: $list")

            uiThread {
                if (!list.isNullOrEmpty()){
                    databaseDataListAdapter.onGetListener(list)
                }

            }

        }
    }

    private fun insertAllUser(insertUserList: ArrayList<Repository>, db: Database) {
        doAsync {
            db.userDao().insertMultipleUser(insertUserList)
            uiThread {
                btnShowData.performClick()
            }
        }

        Log.e("TAG", "insertAllUser: ${insertUserList.size}")

    }


    override fun recyclerItemClickListener(user: Repository) {
        openAlertBox(user)

    }

    private fun openAlertBox(user: Repository) {
        val db = Database.getDatabase(this)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Record delete or update")
        builder.setMessage("are u sure delete or update")
        builder.setPositiveButton("UPDATE") { dialogInterface, i ->
            btnOk.text = "Update"
            isUpdate = true
            userEmail.setText(user.userEmail)
            userName.setText(user.userName)
            userName.setTag(user.userId)
            dialogInterface.dismiss()

        }
        builder.setNegativeButton("DELETE") { dialogInterface, i ->

            deleteUser(db, user)

            dialogInterface.dismiss()
        }
        builder.show()
    }
}