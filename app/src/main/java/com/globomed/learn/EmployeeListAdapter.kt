package com.globomed.learn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//import kotlinx.android.synthetic.main.list_item.view.*

class EmployeeListAdapter(
    private val context: Context
) : RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder>() {
    lateinit var employeeList: ArrayList<Employee>
    lateinit var tvEmpName: TextView
    lateinit var tvEmpDesignation: TextView
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeViewHolder {
val MY_TAG=EmployeeListAdapter::class.java.name
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        tvEmpName = itemView.findViewById(R.id.tvEmpName)
        tvEmpDesignation = itemView.findViewById(R.id.tvEmpDesignation)
        return EmployeeViewHolder(itemView)
    }


    override fun getItemCount(): Int = employeeList.size

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employeeList[position]
        holder.setData(employee.name, employee.designation,position)
        holder.setListener()
    }

    fun setEmployee(employees: ArrayList<Employee>) {
        employeeList = employees
    }

   inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmpName:TextView=itemView.findViewById(R.id.tvEmpName)
        val tvEmpDesignation=itemView.findViewById<TextView>(R.id.tvEmpDesignation)
        var pos=0
        fun setData(name: String, designation: String,pos: Int) {
            tvEmpName.text=name
            tvEmpDesignation.text=designation
            this.pos=pos
        }

        fun setListener() {
itemView.setOnClickListener {
//    val databaseHelper=DatabaseHelper(context)
//    val employee=DataManager.fetchEmployee(databaseHelper,employeeList[pos].id)
//    Log.d(EmployeeListAdapter::class.java.name,employee.toString())
val intent= Intent(context,UpdateEmployeeActivity::class.java)
    intent.putExtra(GloboMedDbContract.EmployeeEntry.COLUMN_ID,employeeList[pos].id)
    (context as Activity).startActivityForResult(intent,2)

}
        }
    }
}
