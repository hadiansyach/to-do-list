package com.hadiansyach.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hadiansyach.todolistapp.adapter.TodoAdapter;
import com.hadiansyach.todolistapp.model.TodoModel;
import com.hadiansyach.todolistapp.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView taskRecyclerView;
    private TodoAdapter taskAdapter;
    private List<TodoModel> todoList;
    private DatabaseHandler db;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.openDatabase();

        todoList = new ArrayList<>();

        taskRecyclerView = findViewById(R.id.rvTask);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TodoAdapter(db, this);
        taskRecyclerView.setAdapter(taskAdapter);

        todoList = db.getAllTasks();
        Collections.reverse(todoList);
        taskAdapter.setTodoList(todoList);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        fab = findViewById(R.id.fabAddTask);
        fab.setOnClickListener(view -> {
            AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        todoList = db.getAllTasks();
        Collections.reverse(todoList);
        taskAdapter.setTodoList(todoList);
        taskAdapter.notifyDataSetChanged();
    }
}