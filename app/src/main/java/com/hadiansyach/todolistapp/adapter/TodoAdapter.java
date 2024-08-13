package com.hadiansyach.todolistapp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadiansyach.todolistapp.AddNewTask;
import com.hadiansyach.todolistapp.MainActivity;
import com.hadiansyach.todolistapp.R;
import com.hadiansyach.todolistapp.model.TodoModel;
import com.hadiansyach.todolistapp.utils.DatabaseHandler;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoModel> todoList;
    private MainActivity mainActivity;
    private DatabaseHandler db;

    public TodoAdapter(DatabaseHandler db, MainActivity mainActivity) {
        this.db = db;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        db.openDatabase();
        TodoModel item = todoList.get(position);

        holder.todo.setText(item.getTask());
        holder.todo.setChecked(toBoolean(item.getStatus()));

        holder.todo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                    holder.todo.setPaintFlags(holder.todo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    db.updateStatus(item.getId(), 0);
                    holder.todo.setPaintFlags(holder.todo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public void setTodoList(List<TodoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    void setTasks(List<TodoModel> todoList) {
        this.todoList = todoList;
    }

    public void deleteItem(int position) {
        TodoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        TodoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(mainActivity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public Context getContext() {
        return mainActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox todo;

        ViewHolder(View view) {
            super(view);
            todo = view.findViewById(R.id.todoCheckbox);
        }
    }
}
