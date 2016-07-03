package com.example.deyanpeev.quiztime.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.models.QuestionModel;

import java.util.List;

public class CreateNewQuestion extends AppCompatActivity {

    private Spinner spinnerCategory;
    private Spinner spinnerInterestingFact;

    private static final String CANNOT_INSERT_QUESTION = "Fail to create a category.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_question);

        this.spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        this.spinnerInterestingFact = (Spinner) findViewById(R.id.spinnerInterestingFact);

        this.LoadCategoriesSpinner();
        this.LoadInterestingFactSpinner();
    }

    public void createNewQuestion(View view) {
        EditText etQuestion = (EditText) findViewById(R.id.teQuestionText);
        String questionContent = etQuestion.getText().toString();

        EditText etAnswer = (EditText) findViewById(R.id.teAnswer);
        String answer = etAnswer.getText().toString();

        //Validating the input
        if(questionContent == null || questionContent.isEmpty()){
            Toast.makeText(getApplicationContext(), "The question cannot be empty.", Toast.LENGTH_LONG).show();
            return;
        }
        if(answer == null || answer.isEmpty()){
            Toast.makeText(getApplicationContext(), "The answer cannot be empty.", Toast.LENGTH_LONG).show();
            return;
        }

        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        String categoryName = spinnerCategory.getSelectedItem().toString();

        Spinner spinnerInterestingFact = (Spinner) findViewById(R.id.spinnerInterestingFact);
        String interestingFactName = spinnerInterestingFact.getSelectedItem().toString();
        if(interestingFactName.equals(getResources().getString(R.string.empty_spinner_element))){
            interestingFactName = null;
        }

        QuestionModel question = null;
        try {
            question = new QuestionModel(questionContent, categoryName, answer, interestingFactName, getApplicationContext());
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), CANNOT_INSERT_QUESTION, Toast.LENGTH_LONG).show();
        }

        StoreDbHelper dbHelper = new StoreDbHelper(getApplicationContext());

        if(dbHelper.insertNewQuestion(question)) {
            Toast.makeText(getApplicationContext(), "The category was successfully created.", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(getApplicationContext(), CANNOT_INSERT_QUESTION, Toast.LENGTH_LONG).show();
        }
    }

    private void LoadCategoriesSpinner(){
        StoreDbHelper db = new StoreDbHelper(getApplicationContext());

        List<String> categories = db.getAllCategories();

        ArrayAdapter<String> dataAdater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);

        this.spinnerCategory.setAdapter(dataAdater);
    }

    private void LoadInterestingFactSpinner(){
        StoreDbHelper db = new StoreDbHelper(getApplicationContext());

        List<String> interestingFactTags = db.getAllInterestingFactTags();
        interestingFactTags.add(0, getResources().getString(R.string.empty_spinner_element));

        ArrayAdapter<String> dataAdater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, interestingFactTags);

        this.spinnerInterestingFact.setAdapter(dataAdater);
    }

    public void testGetAllQuestions(View view) {
        StoreDbHelper dbHelper = new StoreDbHelper((getApplicationContext()));
        List<QuestionModel> test = dbHelper.getAllQuestions(getApplicationContext());
    }
}
