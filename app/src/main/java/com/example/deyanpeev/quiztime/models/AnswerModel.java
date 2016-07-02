package com.example.deyanpeev.quiztime.models;

import android.content.Context;
import android.database.SQLException;

import com.example.deyanpeev.quiztime.data.StoreDbHelper;

public class AnswerModel {
    private String content;
    private long categoryId;

    private String categoryName;

    public AnswerModel(String content, String categoryName, Context context){
        this.content = content;
        this.categoryName = categoryName;

        this.transformIntoIds(context);
    }

    public AnswerModel(String content, long categoryId){
        this.content = content;
        this.categoryId = categoryId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getContent() {
        return content;
    }

    private void transformIntoIds(Context context) throws SQLException{
        StoreDbHelper db = new StoreDbHelper(context);

        this.categoryId = db.getCategoryId(this.categoryName);
    }
}
