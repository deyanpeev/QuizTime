package com.example.deyanpeev.quiztime.models;

import android.content.Context;

import com.example.deyanpeev.quiztime.data.StoreDbHelper;

/**
 * Created by dpeev on 6/26/16.
 */
public class QuestionModel {

    private String questionName;
    private long categoryId;
    private long answerId;
    private long interestingFactId;

    private String categoryName;
    private String answer;
    private String interestingFactTag;

    public QuestionModel(String questionName, String categoryName, String answer, String interestingFactTag, Context context){
        this.questionName = questionName;
        this.categoryName = categoryName;
        this.answer = answer;
        this.interestingFactTag = interestingFactTag;

        this.transformIntoIds(context);
    }

    public QuestionModel(String questionName, long categoryId, long answerId, long interestingFactId){
        this.questionName = questionName;
        this.categoryId = categoryId;
        this.answerId = answerId;
        this.interestingFactId = interestingFactId;
    }

    public String getQuestionContent() {
        return questionName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public long getInterestingFactId() {
        return interestingFactId;
    }

    public long getAnserId() {return answerId; }

    private void transformIntoIds(Context context){
        StoreDbHelper db = new StoreDbHelper(context);

        this.categoryId = db.getCategoryId(this.categoryName);
        this.answerId = db.getOrCreateAnswerId(this.answer, this.categoryId);
        this.interestingFactId = db.getInterestingFactId(this.interestingFactTag);
    }
}
