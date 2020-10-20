package com.codingninjas.todolist.DTO

class ToDoItem{

    var id : Long = -1
    var toDoID : Long  = -1
    var itemName = ""
    var isCompleted = false
    var items: MutableList<ToDoItem> = ArrayList()

}