package com.example.deyanpeev.quiztime.models;

import android.content.Context;
import android.database.SQLException;

import com.example.deyanpeev.quiztime.data.StoreDbHelper;

/**
 * Created by dpeev on 6/26/16.
 */
public class QuestionModel {

    private String questionName;
    private long categoryId;
    private long answerId;
    private Long interestingFactId;

    private String categoryName;
    private String answerName;
    private String interestingFactTag;
    private String interestingFactText;

    public QuestionModel(String questionName, String categoryName, String answer, String interestingFactTag, Context context){
        this.questionName = questionName;
        this.categoryName = categoryName;
        this.answerName = answer;
        this.interestingFactTag = interestingFactTag;

        this.transformIntoIds(context);
    }

    public QuestionModel(String questionName, long categoryId, long answerId, long interestingFactId){
        this.questionName = questionName;
        this.categoryId = categoryId;
        this.answerId = answerId;
        this.interestingFactId = interestingFactId;
    }

    public QuestionModel(String questionName, String answerName, String interestingFactText, long categoryId){
        this.questionName = questionName;
        this.answerName = answerName;
        this.interestingFactText = interestingFactText;
        this.categoryId = categoryId;
    }

    public String getQuestionContent() {
        return questionName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public Long getInterestingFactId() {
        return interestingFactId;
    }

    public long getAnserId() {return answerId; }

    public String getAnswerName() {
        return answerName;
    }

    public String getInterestingFactText() {
        return interestingFactText;
    }

    private void transformIntoIds(Context context){
        StoreDbHelper db = new StoreDbHelper(context);

        this.categoryId = db.getCategoryId(this.categoryName);
        this.answerId = db.getOrCreateAnswerId(this.answerName, this.categoryId);
        try {
            this.interestingFactId = db.getInterestingFactId(this.interestingFactTag);
        }catch (SQLException e){
            this.interestingFactId = null;
        }
    }
}
