package com.globomed.learn

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Sqlite : AppCompatActivity() {
    lateinit var fab:FloatingActionButton
    lateinit var recyclerView:RecyclerView
    private val employeeListAdapter=EmployeeListAdapter(this)
    lateinit var databaseHelper:DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper= DatabaseHelper(this)
        fab=findViewById(R.id.fab)
        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.adapter=employeeListAdapter
        recyclerView.layoutManager=LinearLayoutManager(this)
        employeeListAdapter.setEmployee(DataManager.fetchAllEmployees(databaseHelper))
        fab.setOnClickListener(View.OnClickListener {
            val addEmployee= Intent(this,AddEmployeeActivity::class.java)
            startActivityForResult(addEmployee,1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            employeeListAdapter.setEmployee(DataManager.fetchAllEmployees(databaseHelper))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_deleteAll -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.confirm_sure)
                    .setPositiveButton(R.string.yes) { dialog, eId ->
                        val result = DataManager.deleteAllEmployee(databaseHelper)

                        Toast.makeText(this, "record deleted", Toast.LENGTH_LONG).show()
                        //Log.d("record","deleted")
                        setResult(Activity.RESULT_OK, Intent())
                        finish()


                    }
                    .setNegativeButton(R.string.no) { dialog, id ->
                        dialog.dismiss()
                    }
                val dialog = builder.create()
                dialog.setTitle("Are you sure")
                dialog.show()
                true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }

    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter?.notifyDataSetChanged()
    }
}