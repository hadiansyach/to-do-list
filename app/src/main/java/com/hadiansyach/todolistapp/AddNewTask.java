package com.hadiansyach.todolistapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hadiansyach.todolistapp.model.TodoModel;
import com.hadiansyach.todolistapp.utils.DatabaseHandler;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";

    private EditText editText;
    private Button newTaskButton;
    private Button cancelTaskButton;
    private DatabaseHandler db;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.etNewTask);
        newTaskButton = view.findViewById(R.id.btnSave);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            editText.setText(task);
            assert task != null;
            if (task.length() > 0)
                newTaskButton.setText("Update");
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskButton.setEnabled(false);
                    newTaskButton.setBackgroundColor(getResources().getColor(R.color.black));
                } else {
                    newTaskButton.setEnabled(true);
                    newTaskButton.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (finalIsUpdate) {
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    TodoModel item = new TodoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    db.insertTask(item);
                }
                dismiss();
            }
        });

        cancelTaskButton = view.findViewById(R.id.btnCancel);
        cancelTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {

            {
                ((DialogCloseListener) activity).handleDialogClose(dialog);
            }
            super.onDismiss(dialog);
        }
    }

}
