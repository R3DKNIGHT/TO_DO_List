package com.codingninjas.todolist

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingninjas.todolist.DTO.ToDoItem
import kotlinx.android.synthetic.main.activity_subtask_list.*
import java.util.*

class subtask_list : AppCompatActivity() {
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_a
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_a
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_a
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_a
        )
    }
    private val fromRight: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_right
        )
    }
    private val toRight: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_right_a) }

    private var clicked = false
    lateinit var dbHandler: DBHandler
    var todoId  : Long = -1
    var list: MutableList<ToDoItem>? = null
    var adapter:ItemAdapter? = null
    var touchHelper:ItemTouchHelper? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtask_list)
        setSupportActionBar(item_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = intent.getStringExtra(INTENT_TODO_NAME)
        todoId = intent.getLongExtra(INTENT_TODO_ID, -1)
        dbHandler = DBHandler(this)
        rv_item.layoutManager = LinearLayoutManager(this)

        addSubBtn.setOnClickListener{
            onAddBtnClicked()
        }
        newSubTask.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialogue_dashboard, null)
            val toDoName = view.findViewById<EditText>(R.id.et_todo)
            dialog.setView(view)
            dialog.setTitle("New Sub Task")
            dialog.setPositiveButton("Add"){ _: DialogInterface, _ : Int ->
                if(toDoName.text.isNotEmpty()){
                    val item = ToDoItem()
                    item.itemName = toDoName.text.toString()
                    item.toDoID = todoId
                    item.isCompleted = false
                    dbHandler.addToDoItem(item)
                    refreshList_item()
                }
            }
            dialog.setNegativeButton("Cancel"){ _: DialogInterface, _: Int ->
                Toast.makeText(this,"No Sub Task Added", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
        touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                p0: RecyclerView,
                p1: RecyclerView.ViewHolder,
                p2: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = p1.adapterPosition
                val targetPosition = p2.adapterPosition
                Collections.swap(list,sourcePosition,targetPosition)
                adapter?.notifyItemMoved(sourcePosition,targetPosition)
                return true
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
            touchHelper?.attachToRecyclerView(rv_item)
    }
    fun updateItem(item: ToDoItem){
        val dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialogue_dashboard, null)
        val toDoName = view.findViewById<EditText>(R.id.et_todo)
        toDoName.setText(item.itemName)
        dialog.setView(view)
        dialog.setTitle("Edit Sub Task")
        dialog.setPositiveButton("Add"){ _: DialogInterface, _ : Int ->
            if(toDoName.text.isNotEmpty()){
                item.itemName = toDoName.text.toString()
                item.toDoID = todoId
                item.isCompleted = false
                dbHandler.updateToDoItem(item)
                refreshList_item()
            }
        }
        dialog.setNegativeButton("Cancel"){ _: DialogInterface, _: Int ->
            Toast.makeText(this,"${item.itemName} Not Edited", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }
    override fun onResume() {
        refreshList_item()
        super.onResume()
    }
    public fun refreshList_item(){
        list = dbHandler.getToDoItems(todoId)
        adapter = ItemAdapter(this,list!!)
        rv_item.adapter  = adapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else
            super.onOptionsItemSelected(item)
    }

    private fun onAddBtnClicked() {
        setVisiblity(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisiblity(clicked: Boolean) {
        if (!clicked) {
            newSubTask.visibility = View.VISIBLE
            SubtextView.visibility = View.VISIBLE
        } else {
            newSubTask.visibility = View.INVISIBLE
            SubtextView.visibility = View.INVISIBLE
        }

    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            newSubTask.startAnimation(fromBottom)
            addSubBtn.startAnimation(rotateOpen)
            SubtextView.startAnimation(fromRight)
        } else {
            newSubTask.startAnimation(toBottom)
            addSubBtn.startAnimation(rotateClose)
            SubtextView.startAnimation(toRight)
        }
    }
}