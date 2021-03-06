package com.example.deyanpeev.quiztime.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.helpers.Constants;
import com.example.deyanpeev.quiztime.helpers.Seeder;

import java.util.List;

public class CreateNewCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_category);

        this.setTestMode(Constants.IS_IN_TEST_MODE);
    }

    public void createNewCategory(View view) {
        EditText et = (EditText) findViewById(R.id.teCategory);
        String newCategoryName = et.getText().toString();

        StoreDbHelper dbHelper = new StoreDbHelper(getApplicationContext());

        if(dbHelper.insertNewCategory(newCategoryName)){
            Toast.makeText(getApplicationContext(), "The category was successfully created.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "The category already exists.", Toast.LENGTH_LONG).show();
        }
    }

    public void testGetAll(View view) {
        StoreDbHelper dbHelper = new StoreDbHelper((getApplicationContext()));
        List<String> test = dbHelper.getAllCategories();
    }

    private void setTestMode(boolean isTestMode){
        Button btnTestQuestons = (Button) findViewById(R.id.btnTestCategories);

        if(isTestMode){
            btnTestQuestons.setVisibility(View.VISIBLE);
        } else{
            btnTestQuestons.setVisibility(View.INVISIBLE);
        }
    }
}
