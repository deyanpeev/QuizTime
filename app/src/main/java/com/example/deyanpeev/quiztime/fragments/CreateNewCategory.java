package com.example.deyanpeev.quiztime.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;

import java.util.List;

public class CreateNewCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_category);
    }

    public void createNewCategory(View view) {
        EditText et = (EditText) findViewById(R.id.teCategory);
        String newCategoryName = et.getText().toString();

        StoreDbHelper dbHelper = new StoreDbHelper(getApplicationContext());
        dbHelper.insertNewCategory(newCategoryName);
    }

    public void testGetAll(View view) {
        StoreDbHelper dbHelper = new StoreDbHelper((getApplicationContext()));
        List<String> test = dbHelper.getAllCategories();
    }
}
