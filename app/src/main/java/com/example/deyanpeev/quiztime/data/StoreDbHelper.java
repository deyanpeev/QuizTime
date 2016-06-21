package com.example.deyanpeev.quiztime.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "store.db";

    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CATEGORY_TABLE =
                "CREATE TABLE IF NOT EXISTS " + ProductContract.CategoryEntity.TABLE_NAME + " (" +
                ProductContract.CategoryEntity._ID + " INTEGER PRIMARY KEY," +
                ProductContract.CategoryEntity.COLUMN_TITLE + " TEXT NOT NULL," +
                ");";

        final String SQL_CREATE_INTERESTING_FACT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + ProductContract.InterestingFactEntity.TABLE_NAME + " (" +
                        ProductContract.InterestingFactEntity._ID + " INTEGER PRIMARY KEY," +
                        ProductContract.InterestingFactEntity.COLUMN_TEXT + " TEXT NOT NULL," +
                        ");";

        final String SQL_CREATE_QUESTION_TABLE =
                "CREATE TABLE IS NOT EXISTS " + ProductContract.QuestionEntity.TABLE_NAME + " (" +
                ProductContract.QuestionEntity._ID + " INTEGER PRIMARY KEY," +
                ProductContract.QuestionEntity.COLUMN_CONTENT + " TEXT NOT NULL," +
                ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY + " TEXT NOT NULL," +
                "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY + ") REFERENCES " + ProductContract.CategoryEntity.TABLE_NAME + "(" + ProductContract.CategoryEntity._ID + ")," +
                ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY + "TEXT NOT NULL, " +
                "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY + ") REFERENCES " + ProductContract.InterestingFactEntity.TABLE_NAME + "(" + ProductContract.InterestingFactEntity._ID + ")," +
                ");";

        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_INTERESTING_FACT_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNewCategory(String categoryName){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.CategoryEntity.COLUMN_TITLE, categoryName);

        db.insert(ProductContract.CategoryEntity.TABLE_NAME, null, values);
        db.close();
    }
}
