package com.example.deyanpeev.quiztime.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.models.InterestingFact;

import java.util.ArrayList;
import java.util.List;

public class StoreDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "store";

    private final String SQL_CREATE_CATEGORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.CategoryEntity.TABLE_NAME + " (" +
                    ProductContract.CategoryEntity._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.CategoryEntity.COLUMN_TITLE + " TEXT NOT NULL" +
                    ");";

    private final String SQL_CREATE_INTERESTING_FACT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.InterestingFactEntity.TABLE_NAME + " (" +
                    ProductContract.InterestingFactEntity._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG + " TEXT NOT NULL," +
                    ProductContract.InterestingFactEntity.COLUMN_TEXT + " TEXT NOT NULL" +
                    ");";

    private final String SQL_CREATE_QUESTION_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.QuestionEntity.TABLE_NAME + " (" +
                    ProductContract.QuestionEntity._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.QuestionEntity.COLUMN_CONTENT + " TEXT NOT NULL," +
                    ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY + " TEXT NOT NULL," +
                    ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY + ") REFERENCES " + ProductContract.CategoryEntity.TABLE_NAME + "(" + ProductContract.CategoryEntity._ID + ")," +
                    "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY + ") REFERENCES " + ProductContract.InterestingFactEntity.TABLE_NAME + "(" + ProductContract.InterestingFactEntity._ID + ")" +
                    ");";

    private final String SQL_SELECT_ALL_CATEGORIES = "SELECT * FROM " + ProductContract.CategoryEntity.TABLE_NAME;

    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(SQL_CREATE_CATEGORY_TABLE);
            db.execSQL(SQL_CREATE_INTERESTING_FACT_TABLE);
            db.execSQL(SQL_CREATE_QUESTION_TABLE);
        } catch(SQLException e){
            Log.e("tag", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.deleteTables(db);
        this.onCreate(db);
    }

    public void insertNewCategory(String categoryName){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.CategoryEntity.COLUMN_TITLE, categoryName);

        db.insert(ProductContract.CategoryEntity.TABLE_NAME, null, values);
        db.close();
    }

    public List<String> getAllCategories(){
        List<String> result = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_CATEGORIES, null);

        if(cursor.moveToFirst()){
            do{
                result.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }

        return result;
    }

    public void insertNewInterestingFact(InterestingFact interestingFact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG, interestingFact.getShortTag());
        values.put(ProductContract.InterestingFactEntity.COLUMN_TEXT, interestingFact.getDescription());

        db.insert(ProductContract.CategoryEntity.TABLE_NAME, null, values);
        db.close();
    }

    private void deleteTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE " + ProductContract.InterestingFactEntity.TABLE_NAME);
    }
}
