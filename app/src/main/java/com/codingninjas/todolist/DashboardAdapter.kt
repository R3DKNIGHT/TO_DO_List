package com.codingninjas.todolist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.codingninjas.todolist.DTO.ToDo

class DashboardAdapter(val activity: dashboard, var list: MutableList<ToDo>) :
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_layout,p0,false))

    }
    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.toDoName.text = list[p1].name
        holder.toDoName.setOnClickListener{
            val id_long = list[p1].id
            val intent = Intent(activity,subtask_list::class.java)
            intent.putExtra(INTENT_TODO_ID,id_long)
            intent.putExtra(INTENT_TODO_NAME,list[p1].name)
            activity.startActivity(intent)
        }
        holder.menu.setOnClickListener{
            val pop = PopupMenu(activity,holder.menu)
            pop.inflate(R.menu.dashboard_chld)
            pop.setOnMenuItemClickListener {

                when(it.itemId){
                    R.id.menu_edit->{

                        activity.updateToDo(list[p1])
                    }
                    R.id.menu_delete->{
                        val dialog = AlertDialog.Builder(activity)
                        dialog.setTitle("Delete?")
                        dialog.setMessage("Do you want to delete this item?")
                        dialog.setPositiveButton("Continue") { _: DialogInterface, _:Int->
                            activity.dbHandler.deleteToDo(list[p1].id)
                            activity.refreshList()
                        }
                        dialog.setNegativeButton("Cancel"){ _: DialogInterface, _:Int->
                            Toast.makeText(activity,"No Task Deleted",Toast.LENGTH_SHORT).show()
                        }
                        dialog.show()
                    }
                    R.id.menu_mark_as_completed->{
                        activity.dbHandler.updateToDoItemCompletedStatus(list[p1].id,true)
                        activity.findViewById<TextView>(R.id.tv_todo_name).apply {
                            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        }
                    }
                    R.id.menu_reset->{
                        activity.dbHandler.updateToDoItemCompletedStatus(list[p1].id,false)
                        activity.findViewById<TextView>(R.id.tv_todo_name).apply {
                            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG.inv()
                        }
                    }
                }

                true
            }
            pop.show()
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        val toDoName : TextView = v.findViewById(R.id.tv_todo_name)
        val menu: ImageView = v.findViewById(R.id.ed_menu)
    }

}