package com.codingninjas.todolist
import android.os.FileObserver.CREATE

const val DB_NAME = "ToDoList"
const val DB_VER = 1
const val TABLE_TODO = "ToDo"

const val COL_ID = "id"
const val COL_NAME = "Name"
const val COL_Date = "createdAt"


const val COL_TODO_ID = "ToDoid"
const val COL_TODO_ITM = "itemName"
const val TABLE_TODO_ITEM = "ToDoItem"
const val COL_COMPLETED = "completed"

const val INTENT_TODO_ID = "TodoId"
const val INTENT_TODO_NAME = "TodoName"
