package com.codingninjas.todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.codingninjas.todolist.DTO.ToDo
import com.codingninjas.todolist.DTO.ToDoItem

class DBHandler(val context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {

    override fun onCreate(db: SQLiteDatabase) {
        val createToDoTable = " CREATE TABLE $TABLE_TODO (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_NAME varchar," +
                "$COL_Date datetime DEFAULT CURRENT_TIMESTAMP" +
                ");"
                val createToDoItemTable =
                "CREATE TABLE $TABLE_TODO_ITEM (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT,\n" +
                "$COL_TODO_ITM varchar," +
                "$COL_TODO_ID varchar," +
                "$COL_Date datetime DEFAULT CURRENT_TIMESTAMP," +
                "$COL_COMPLETED integer);"

        db.execSQL(createToDoTable)
        db.execSQL(createToDoItemTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }



    fun addToDo(toDo : ToDo) : Boolean{
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        val result = db.insert(TABLE_TODO,null,cv)
        return result != (-1).toLong()
    }

    fun getToDo() : MutableList<ToDo>{
        val result : MutableList<ToDo> = ArrayList()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO", null)
        if(queryResult.moveToFirst()){
            do{
                val todo = ToDo()
                todo.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                todo.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                result.add(todo)
            } while (queryResult.moveToNext())

        }
        queryResult.close()
        return result
    }

    fun addToDoItem(item: ToDoItem):Boolean{
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_TODO_ITM, item.itemName)
        cv.put(COL_TODO_ID, item.toDoID)
        if (item.isCompleted)
            cv.put(COL_COMPLETED, true)
        else
            cv.put(COL_COMPLETED, false)
        val result = db.insert(TABLE_TODO_ITEM,null,cv)
        return result != (-1).toLong()
    }
    fun updateToDoItem(item: ToDoItem){
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_TODO_ITM, item.itemName)
        cv.put(COL_TODO_ID, item.toDoID)
        cv.put(COL_COMPLETED,item.isCompleted)
        db.update(TABLE_TODO_ITEM,cv,"$COL_ID=?", arrayOf(item.id.toString()))
    }
    fun deleteToDoItem(itemId: Long){
        val db =writableDatabase
        db.delete(TABLE_TODO_ITEM,"$COL_ID=?", arrayOf(itemId.toString()))
    }

    fun getToDoItems(todoId: Long) : MutableList<ToDoItem>{
        val result : MutableList<ToDoItem> = ArrayList()

        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO_ITEM WHERE $COL_TODO_ID = $todoId",null)

        if(queryResult.moveToFirst()){
            do {
                val item = ToDoItem()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.toDoID = queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID))
                item.itemName = queryResult.getString(queryResult.getColumnIndex(COL_TODO_ITM))
                item.itemName = queryResult.getString(queryResult.getColumnIndex(COL_TODO_ITM))
                item.isCompleted = queryResult.getInt(queryResult.getColumnIndex(COL_COMPLETED)) == 1
                result.add(item)
            }while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }
    fun updateToDo(toDo: ToDo) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        db.update(TABLE_TODO,cv,"$COL_ID=?" , arrayOf(toDo.id
            .toString()))
    }
    fun deleteToDo(todoId: Long){
        val db = writableDatabase
        db.delete(TABLE_TODO_ITEM,"$COL_TODO_ID=?", arrayOf(todoId.toString()))
        db.delete(TABLE_TODO,"$COL_ID=?", arrayOf(todoId.toString()))
    }
    fun updateToDoItemCompletedStatus(todoId: Long,isCompleted: Boolean){
        val db = writableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO_ITEM WHERE $COL_TODO_ID=$todoId", null)

        if (queryResult.moveToFirst()) {
            do {
                val item = ToDoItem()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.toDoID = queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID))
                item.itemName = queryResult.getString(queryResult.getColumnIndex(COL_TODO_ITM))
                item.isCompleted = isCompleted
                updateToDoItem(item)
            } while (queryResult.moveToNext())
        }

        queryResult.close()
    }

}


