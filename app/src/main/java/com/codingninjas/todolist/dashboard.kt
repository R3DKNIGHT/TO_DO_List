package com.codingninjas.todolist

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingninjas.todolist.DTO.ToDo
import kotlinx.android.synthetic.main.dashboard.*
import androidx.recyclerview.widget.ItemTouchHelper


class dashboard : AppCompatActivity() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_a) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_a) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_a) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_a) }
    private val fromRight: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_right) }
    private val toRight: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_right_a) }

    private var clicked = false

    lateinit var dbHandler: DBHandler


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        setSupportActionBar(dashboard_toolbar)
        setTitle("Dashboard")
        dbHandler = DBHandler(this)
        rv_dashboard.layoutManager = LinearLayoutManager(this)
        addBtn.setOnClickListener{
            onAddBtnClicked()
        }

        newTask.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialogue_dashboard, null)
            val toDoName = view.findViewById<EditText>(R.id.et_todo)
            dialog.setView(view)
            dialog.setTitle("New Task")
            dialog.setPositiveButton("Add"){ _: DialogInterface, _ : Int ->
                if(toDoName.text.isNotEmpty()){
                    val toDo = ToDo()
                    toDo.name = toDoName.text.toString()
                    dbHandler.addToDo(toDo)
                    refreshList()
                }
            }
            dialog.setNegativeButton("Cancel"){ _: DialogInterface, _: Int ->
                Toast.makeText(this,"No Task Added",Toast.LENGTH_SHORT).show()
            }
            dialog.show()

        }

    }
    public fun updateToDo(toDo: ToDo){
        val dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialogue_dashboard, null)
        val toDoName = view.findViewById<EditText>(R.id.et_todo)
        toDoName.setText(toDo.name)
        dialog.setView(view)
        dialog.setTitle("Edit Task")
        dialog.setPositiveButton("Edit"){ _: DialogInterface, _ : Int ->
            if(toDoName.text.isNotEmpty()){
                toDo.name = toDoName.text.toString()
                dbHandler.updateToDo(toDo)
                refreshList()
            }
        }
        dialog.setNegativeButton("Cancel"){ _: DialogInterface, _: Int ->
            Toast.makeText(this,"Task Not Edited",Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }



    fun refreshList(){
        rv_dashboard.adapter  = DashboardAdapter(this,dbHandler.getToDo())
    }


    override fun onResume() {
        refreshList()
        super.onResume()
    }




    private fun onAddBtnClicked() {
        setVisiblity(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisiblity(clicked: Boolean) {
        if(!clicked){
            newTask.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
        }else{
            newTask.visibility = View.INVISIBLE
            textView.visibility = View.INVISIBLE
        }

    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            newTask.startAnimation(fromBottom)
            addBtn.startAnimation(rotateOpen)
            textView.startAnimation(fromRight)
        }else{
            newTask.startAnimation(toBottom)
            addBtn.startAnimation(rotateClose)
            textView.startAnimation(toRight)
        }
    }
}