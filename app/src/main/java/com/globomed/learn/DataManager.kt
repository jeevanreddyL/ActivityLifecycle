package com.globomed.learn

import android.content.ContentValues

object DataManager {
    fun fetchAllEmployees(databaseHelper: DatabaseHelper): ArrayList<Employee> {
        val employees = ArrayList<Employee>()
        val db = databaseHelper.readableDatabase
        val columns = arrayOf(
            GloboMedDbContract.EmployeeEntry.COLUMN_ID,
            GloboMedDbContract.EmployeeEntry.COLUMN_NAME,
            GloboMedDbContract.EmployeeEntry.COLUMN_DOB,
            GloboMedDbContract.EmployeeEntry.COLUMN_DESIGNATION
        )
        val cursor = db.query(
            GloboMedDbContract.EmployeeEntry.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null
        )
        val idPos = cursor.getColumnIndex(GloboMedDbContract.EmployeeEntry.COLUMN_ID)
        val namePos = cursor.getColumnIndex(GloboMedDbContract.EmployeeEntry.COLUMN_NAME)
        val dobPos = cursor.getColumnIndex(GloboMedDbContract.EmployeeEntry.COLUMN_DOB)
        val designationPos =
            cursor.getColumnIndex(GloboMedDbContract.EmployeeEntry.COLUMN_DESIGNATION)
        while (cursor.moveToNext()) {
            val id = cursor.getString(idPos)
            val name = cursor.getString(namePos)
            val dob = cursor.getLong(dobPos)
            val designation = cursor.getString(designationPos)
            employees.add(Employee(id, name, dob, designation))
        }
        cursor.close()
        return employees
    }

    fun fetchEmployee(databaseHelper: DatabaseHelper, empId: String):Employee? {
        val db = databaseHelper.readableDatabase
        var employee: Employee? = null
        val columns = arrayOf(
            GloboMedDbContract.EmployeeEntry.COLUMN_NAME,
            GloboMedDbContract.EmployeeEntry.COLUMN_DOB,
            GloboMedDbContract.EmployeeEntry.COLUMN_DESIGNATION
        )
        val selection = GloboMedDbContract.EmployeeEntry.COLUMN_ID + " LIKE ? "
        val selectionArgs = arrayOf(empId)
        val cursor = db.query(
            GloboMedDbContract.EmployeeEntry.TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val namePos = cursor.getColumnIndex(GloboMedDbContract.EmployeeEntry.COLUMN_NAME)
        val dobPos = cursor.getColumnIndex(GloboMedDbContract.EmployeeEntry.COLUMN_DOB)
        val designationPos =
            cursor.getColumnIndex(GloboMedDbContract.EmployeeEntry.COLUMN_DESIGNATION)
        while (cursor.moveToNext()) {
            val name = cursor.getString(namePos)
            val dob = cursor.getLong(dobPos)
            val designation = cursor.getString(designationPos)
             employee= Employee(empId,name,dob, designation)
        }
        cursor.close()
        return employee
    }
    fun updateEmployee(databaseHelper: DatabaseHelper,employee: Employee){
        val db=databaseHelper.writableDatabase
        val values= ContentValues()
        values.put(GloboMedDbContract.EmployeeEntry.COLUMN_NAME,employee.name)
        values.put(GloboMedDbContract.EmployeeEntry.COLUMN_DOB,employee.dob)
        values.put(GloboMedDbContract.EmployeeEntry.COLUMN_DESIGNATION,employee.designation)
        val selection = GloboMedDbContract.EmployeeEntry.COLUMN_ID + " LIKE ? "
        val selectionArgs = arrayOf(employee.id)
        db.update(GloboMedDbContract.EmployeeEntry.TABLE_NAME,values,selection,selectionArgs)
    }
    fun deleteEmployee(databaseHelper: DatabaseHelper,empId: String):Int{
        val db=databaseHelper.writableDatabase
        val selection = GloboMedDbContract.EmployeeEntry.COLUMN_ID + " LIKE ? "
        val selectionArgs = arrayOf(empId)
        return db.delete(GloboMedDbContract.EmployeeEntry.TABLE_NAME,selection,selectionArgs)

    }

    fun deleteAllEmployee(databaseHelper: DatabaseHelper): Any {
        val db=databaseHelper.writableDatabase
        return db.delete(GloboMedDbContract.EmployeeEntry.TABLE_NAME,"1",null)

    }
}