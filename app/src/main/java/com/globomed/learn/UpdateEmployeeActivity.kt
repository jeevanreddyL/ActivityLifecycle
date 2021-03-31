package com.globomed.learn

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
//import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateEmployeeActivity : AppCompatActivity() {
    var empId: String? = null
    lateinit var databaseHelper: DatabaseHelper
    private val myCalendar = Calendar.getInstance()
    lateinit var etDOB: TextInputEditText
    lateinit var bSave: Button
    lateinit var bCancel: Button
    lateinit var etEmpName: TextInputEditText
    lateinit var etDesignation: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        databaseHelper = DatabaseHelper(this)
        etDOB = findViewById(R.id.etDOB)
        bSave = findViewById(R.id.bSave)
        bCancel = findViewById(R.id.bCancel)
        etEmpName = findViewById(R.id.etEmpName)
        etDesignation = findViewById(R.id.etDesignation)
        val bundle: Bundle? = intent.extras
        bundle?.let {
            empId = bundle.getString(GloboMedDbContract.EmployeeEntry.COLUMN_ID)
            val employee = DataManager.fetchEmployee(databaseHelper, empId!!)
            employee?.let {
                etEmpName.setText(employee.name)
                etDesignation.setText(employee.designation)
                etDOB.setText(getFormattedDate(employee.dob))
            }
        }

        // on clicking ok on the calender dialog
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            etDOB.setText(getFormattedDate(myCalendar.timeInMillis))
        }

        etDOB.setOnClickListener {
            setUpCalender(date)
        }

        bSave.setOnClickListener {
            saveEmployee()
        }

        bCancel.setOnClickListener {
            finish()
        }
    }

    private fun saveEmployee() {

        var isValid = true

        etDesignation.error = if (etDesignation?.text.toString().isEmpty()) {
            isValid = false
            "Required Field"
        } else null

        etEmpName.error = if (etEmpName?.text.toString().isEmpty()) {
            isValid = false
            "Required Field"
        } else null

        if (isValid) {
            val updateName = etEmpName.text.toString()
            val updateDOB = myCalendar.timeInMillis
            val updateDesignation = etDesignation.text.toString()
            val updatedEmployee = Employee(empId!!, updateName, updateDOB, updateDesignation)
            DataManager.updateEmployee(databaseHelper, updatedEmployee)
            setResult(Activity.RESULT_OK, Intent())
            Toast.makeText(this, "Employee updated", Toast.LENGTH_LONG).show()
           // Log.d("Employee", "updated")
            finish()
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.confirm_sure)
                    .setPositiveButton(R.string.yes) { dialog, eId ->
                        val result = DataManager.deleteEmployee(databaseHelper, empId.toString())

                        Toast.makeText(this, "record deleted", Toast.LENGTH_LONG).show()
                       // Log.d("record","deleted")
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
}