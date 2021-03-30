package com.globomed.learn

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
//import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddEmployeeActivity : Activity() {

    private val myCalendar = Calendar.getInstance()
    lateinit var bSave: Button
    lateinit var bCancel: Button
    lateinit var etEmpName:TextInputEditText
    lateinit var etDesignation:TextInputEditText
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val etDOB: TextInputEditText = findViewById(R.id.etDOB)
        // on clicking ok on the calender dialog
        databaseHelper= DatabaseHelper(this)
        bSave=findViewById(R.id.bSave)
        bCancel=findViewById(R.id.bCancel)
        etEmpName=findViewById(R.id.etEmpName)
        etDesignation=findViewById(R.id.etDesignation)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            etDOB.setText(getFormattedDate(myCalendar.timeInMillis))
        }

        etDOB.setOnClickListener {
            setUpCalender(date)
        }
        bSave.setOnClickListener { saveEmployee() }
        bCancel.setOnClickListener {  }
    }

    private fun saveEmployee() {
         var isValid=true
       etEmpName.error=if(etEmpName?.text.toString().isEmpty()){
           isValid=false
           "Required field"
       }else null
        etDesignation.error=if(etDesignation?.text.toString().isEmpty()){
            isValid=false
            "Required field"
        }else null
        if(isValid){
            val name=etEmpName?.text.toString()
            val designation=etDesignation?.text.toString()
            val dob=myCalendar.timeInMillis
           val db= databaseHelper.writableDatabase
            val values=ContentValues()
            values.put(GloboMedDbContract.EmployeeEntry.COLUMN_NAME,name)
            values.put(GloboMedDbContract.EmployeeEntry.COLUMN_DOB,dob)
            values.put(GloboMedDbContract.EmployeeEntry.COLUMN_DESIGNATION,designation)
          val result= db.insert(GloboMedDbContract.EmployeeEntry.TABLE_NAME,null,values)
            setResult(RESULT_OK, Intent())
            Toast.makeText(this,"Employee Added",Toast.LENGTH_LONG).show()
             Log.d("Employee","Added")
            println("Employee Added")
        }
        finish()
    }

    private fun setUpCalender(date: DatePickerDialog.OnDateSetListener) {

        DatePickerDialog(
            this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getFormattedDate(dobInMilis: Long?): String {

        return dobInMilis?.let {
            val sdf = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())
            sdf.format(dobInMilis)
        } ?: "Not Found"
    }
}
