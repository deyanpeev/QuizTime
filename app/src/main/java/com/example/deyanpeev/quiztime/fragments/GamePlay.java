package com.example.deyanpeev.quiztime.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.ProductContract;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.helpers.Constants;
import com.example.deyanpeev.quiztime.helpers.DifficultyLevel;
import com.example.deyanpeev.quiztime.models.QuestionModel;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

public class GamePlay extends AppCompatActivity {

    private String difficultyLevel;
    private int numberOfFreeQuestions;
    private int numberOfFreeQuestionsLeft;
    private int numberOfQuestionsSoFar;
    private String category;

    private List<QuestionModel> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        this.getBundles();
        this.numberOfFreeQuestions = DifficultyLevel.getNumberOfFreeQuestions(this.difficultyLevel);

        StoreDbHelper dbHelper = new StoreDbHelper(getApplicationContext());

        this.questions = dbHelper.getRandomPlayQuestions(this.category, this.getResources());

        this.numberOfQuestionsSoFar = 0;
        this.numberOfFreeQuestions = DifficultyLevel.getNumberOfFreeQuestions(this.difficultyLevel);
        this.numberOfFreeQuestionsLeft = this.numberOfFreeQuestions;

        this.putNextQuestion(this.questions.get(this.numberOfQuestionsSoFar));
    }

    public void markAnswer(View view) {
        String selectedAnswer = ((Button) findViewById(view.getId())).getText().toString();

        String correctAnswer = this.questions.get(this.numberOfQuestionsSoFar).getAnswerName();

        boolean isAnswerCorrect = selectedAnswer.equals(correctAnswer);

        QuestionModel question = this.questions.get(this.numberOfQuestionsSoFar);

        if (isAnswerCorrect) {
            Toast.makeText(getApplicationContext(), "You selected the correct answer.", Toast.LENGTH_LONG).show();

            if(question.getInterestingFactText() != null && !question.getInterestingFactText().isEmpty()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GamePlay.this);
                builder.setMessage(question.getInterestingFactText())
                        .setTitle("Interesting fact").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {
            if(--numberOfFreeQuestionsLeft <= 0){
                Intent goToStartNewCategoryActivity = new Intent(this, FailGame.class);
                startActivity(goToStartNewCategoryActivity );
                return;
            } else {
                Toast.makeText(getApplicationContext(), "You did not select the correct answer.", Toast.LENGTH_LONG).show();
            }
        }

        if(++this.numberOfQuestionsSoFar >= Constants.NUMBER_OF_QUESTIONS){
            Intent goToStartNewCategoryActivity = new Intent(this, SuccessfulGame.class);
            startActivity(goToStartNewCategoryActivity );
            return;
        }

        //update the question
        question = this.questions.get(this.numberOfQuestionsSoFar);
        this.putNextQuestion(question);
    }

    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            throw new IllegalStateException("There is no category and difficulty levels set.");
        }

        this.difficultyLevel = bundle.getString(getResources().getString(R.string.difficulty_level_bundle));
        this.category = bundle.getString(getResources().getString(R.string.category_bunde));
    }

    private void preSetUpFields() {
        this.setNumberOfFreeQuestions();
        this.setNumberOfTotalQuestions();

        TextView tvCategory = (TextView) findViewById(R.id.lblCategory);
        tvCategory.setText(this.category);

        TextView tvDifficultyLevel = (TextView) findViewById(R.id.lblDifficultyLevel);
        tvDifficultyLevel.setText(this.difficultyLevel);
    }

    private void setNumberOfFreeQuestions() {
        String numberOfFreeQuestionsString = this.numberOfFreeQuestionsLeft + "/" + this.numberOfFreeQuestions;
        TextView tv = (TextView) findViewById(R.id.lblFreeQuestions);
        tv.setText(numberOfFreeQuestionsString);
    }

    private void setNumberOfTotalQuestions() {
        String numberOfTotalQuestionString = this.numberOfQuestionsSoFar + 1 + "/" + Constants.NUMBER_OF_QUESTIONS;
        TextView tv = (TextView) findViewById(R.id.lblTotalQuestions);
        tv.setText(numberOfTotalQuestionString);
    }

    public String getCategory() {
        return category;
    }

    private void setupAnswerButtons(QuestionModel question) {
        StoreDbHelper dbHelper = new StoreDbHelper(getApplicationContext());

        List<String> answers = dbHelper.getRandomAnswersByCategoryId(question.getCategoryId(), question.getAnswerName(), 3, this.getResources());
        answers.add(question.getAnswerName());
        Collections.shuffle(answers);

        ((Button) findViewById(R.id.firstButton)).setText(answers.get(0));
        ((Button) findViewById(R.id.secondButton)).setText(answers.get(1));
        ((Button) findViewById(R.id.thirdButton)).setText(answers.get(2));
        ((Button) findViewById(R.id.fourtButton)).setText(answers.get(3));
    }

    private void putNextQuestion(QuestionModel question){
        ((TextView) findViewById(R.id.tvQuestionText)).setText(question.getQuestionContent());
        this.setupAnswerButtons(question);
        this.preSetUpFields();
    }
}

