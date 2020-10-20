package com.codingninjas.todolist

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.codingninjas.todolist.DTO.ToDoItem

class ItemAdapter(val activity: subtask_list,  val list: MutableList<ToDoItem>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_item,p0,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.itemName.text = list[p1].itemName
        holder.itemName.isChecked = list[p1].isCompleted
        holder.itemName.setOnClickListener{
            list[p1].isCompleted = !list[p1].isCompleted
            activity.dbHandler.updateToDoItem(list[p1])
        }
        holder.del.setOnClickListener{
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("Delete?")
            dialog.setMessage("Do you want to delete this item?")
            dialog.setPositiveButton("Continue"){_:DialogInterface,_ :Int ->
            activity.dbHandler.deleteToDoItem(list[p1].id)
            activity.refreshList_item()
            }
            dialog.setNegativeButton("Cancel"){_:DialogInterface,_ :Int ->
                Toast.makeText(activity,"No Task Deleted", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
        holder.edit.setOnClickListener {
            activity.updateItem(list[p1])
        }
        holder.move.setOnTouchListener{v, event ->
            if(event.actionMasked==MotionEvent.ACTION_DOWN){
                activity.touchHelper?.startDrag(holder)
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        val itemName : CheckBox = v.findViewById(R.id.cb_item)
        val edit: ImageView = v.findViewById(R.id.child_edit)
        val del: ImageView = v.findViewById(R.id.child_del)
        val move: ImageView = v.findViewById(R.id.child_move)
    }
}