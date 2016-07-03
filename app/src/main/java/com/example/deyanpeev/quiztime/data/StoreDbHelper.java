package com.example.deyanpeev.quiztime.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.deyanpeev.quiztime.models.AnswerModel;
import com.example.deyanpeev.quiztime.models.InterestingFactModel;
import com.example.deyanpeev.quiztime.models.QuestionModel;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

public class StoreDbHelper extends SQLiteOpenHelper {

    private static final int ANSWER_MAX_LENGTH = 70;
    private static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "store";

    private final String SQL_CREATE_CATEGORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.CategoryEntity.TABLE_NAME + " (" +
                    ProductContract.CategoryEntity._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.CategoryEntity.COLUMN_TITLE + " VARCHAR NOT NULL" +
                    ");";

    private final String SQL_CREATE_INTERESTING_FACT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.InterestingFactEntity.TABLE_NAME + " (" +
                    ProductContract.InterestingFactEntity._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG + " VARCHAR NOT NULL," +
                    ProductContract.InterestingFactEntity.COLUMN_TEXT + " VARCHAR NOT NULL" +
                    ");";

    private final String SQL_CREATE_QUESTION_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.QuestionEntity.TABLE_NAME + " (" +
                    ProductContract.QuestionEntity._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.QuestionEntity.COLUMN_CONTENT + " VARCHAR NOT NULL," +
                    ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY + " TEXT NOT NULL," +
                    ProductContract.QuestionEntity.COLUMN_ANSWER_KEY + " TEXT NOT NULL," +
                    ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY + " TEXT, " +
                    "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY + ") REFERENCES " + ProductContract.CategoryEntity.TABLE_NAME + "(" + ProductContract.CategoryEntity._ID + ")," +
                    "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_ANSWER_KEY + ") REFERENCES " + ProductContract.Answer.TABLE_NAME + "(" + ProductContract.Answer._ID + ")," +
                    "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY + ") REFERENCES " + ProductContract.InterestingFactEntity.TABLE_NAME + "(" + ProductContract.InterestingFactEntity._ID + ")" +
                    ");";

    private final String SQL_CREATE_ANSWER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.Answer.TABLE_NAME + " (" +
                    ProductContract.Answer._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.Answer.COLUMN_CATEGORY_KEY + " TEXT NOT NULL, " +
                    ProductContract.Answer.COLUMN_CONTENT + " VARCHAR NOT NULL UNIQUE, " +
                    "FOREIGN KEY (" + ProductContract.Answer.COLUMN_CATEGORY_KEY + ") REFERENCES " + ProductContract.CategoryEntity.TABLE_NAME + "(" + ProductContract.CategoryEntity._ID + ")" +
                    ");";

    private final String SQL_SELECT_ALL_CATEGORIES = "SELECT " + ProductContract.CategoryEntity.COLUMN_TITLE
            + " FROM " + ProductContract.CategoryEntity.TABLE_NAME;

    private final String SQL_SELECT_ALL_INTERESTING_FACTS_TAGS = "SELECT " + ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG
            + " FROM " + ProductContract.InterestingFactEntity.TABLE_NAME;

    private final String SQL_SELECT_ALL_QUESTIONS = "SELECT * FROM " + ProductContract.QuestionEntity.TABLE_NAME;

    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_INTERESTING_FACT_TABLE);
        db.execSQL(SQL_CREATE_ANSWER_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        this.deleteTables(db);
//        this.onCreate(db);
    }

    public boolean insertNewCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(this.doesCategoryExist(categoryName)){
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(ProductContract.CategoryEntity.COLUMN_TITLE, categoryName);

        db.insert(ProductContract.CategoryEntity.TABLE_NAME, null, values);
        db.close();
        return true;
    }

    public Long insertNewAnswer(AnswerModel answer) throws SQLException, StringIndexOutOfBoundsException{
        if(answer.getContent().length() > ANSWER_MAX_LENGTH){
            throw new StringIndexOutOfBoundsException("The answer must be less than " + ANSWER_MAX_LENGTH);
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.Answer.COLUMN_CONTENT, answer.getContent());
        values.put(ProductContract.Answer.COLUMN_CATEGORY_KEY, answer.getCategoryId());

        return db.insert(ProductContract.Answer.TABLE_NAME, null, values);
    }

    public void insertNewInterestingFact(InterestingFactModel interestingFact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG, interestingFact.getShortTag());
        values.put(ProductContract.InterestingFactEntity.COLUMN_TEXT, interestingFact.getDescription());

        db.insert(ProductContract.InterestingFactEntity.TABLE_NAME, null, values);
        db.close();
    }

    public boolean insertNewQuestion(QuestionModel question){
        SQLiteDatabase dbWrite = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.QuestionEntity.COLUMN_CONTENT, question.getQuestionContent());
        values.put(ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY, question.getCategoryId());
        values.put(ProductContract.QuestionEntity.COLUMN_ANSWER_KEY, question.getAnserId());
        if(question.getInterestingFactId() != null) {
            values.put(ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY, question.getInterestingFactId());
        }

        long result = dbWrite.insert(ProductContract.QuestionEntity.TABLE_NAME, null, values);
        dbWrite.close();

        return result > -1;
    }

    public List<QuestionModel> getAllQuestions(Context context){
        List<QuestionModel> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_QUESTIONS, null);

        if(cursor.moveToFirst()){
            do{
                result.add(new QuestionModel(cursor.getString(1), cursor.getLong(2), cursor.getLong(3),cursor.getLong(4)));
            }while(cursor.moveToNext());
        }

        return result;
    }

    public List<String> getAllCategories(){
        List<String> result = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_CATEGORIES, null);

        if(cursor.moveToFirst()){
            do{
                result.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        return result;
    }

    public List<String> getAllInterestingFactTags(){
        List<String> result = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_INTERESTING_FACTS_TAGS, null);

        if(cursor.moveToFirst()){
            do{
                result.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        return result;
    }

    public Boolean doesInterestingFactExist(InterestingFactModel interestingFact){
        return this.getEntityId(ProductContract.InterestingFactEntity.TABLE_NAME,
                ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG, interestingFact.getShortTag()) >= 0;
    }

    public int getCategoryId(String categoryName){
        final String SQL_GET_CATEGORY_ID = "SELECT " + ProductContract.CategoryEntity._ID + " FROM "
                + ProductContract.CategoryEntity.TABLE_NAME + " WHERE "
                + ProductContract.CategoryEntity.COLUMN_TITLE + " = '" + categoryName + "' LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_CATEGORY_ID, null);

        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        } else {
            throw new SQLException("There is no such category.");
        }
    }

    public long getInterestingFactId(String interestingFactName) throws SQLException {
        long id = this.getEntityId(ProductContract.InterestingFactEntity.TABLE_NAME,
                ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG, interestingFactName);

        if(id >= 0){
            return id;
        } else {
            throw new SQLException("No such interesting fact.");
        }
    }

    public Long getOrCreateAnswerId(String answer, long categoryId){
        long answerId = getEntityId(ProductContract.Answer.TABLE_NAME,
                ProductContract.Answer.COLUMN_CONTENT, answer);

        //doesn't exist - creating
        if(answerId < 0){
            answerId = this.insertNewAnswer(new AnswerModel(answer, categoryId));
        }

        return answerId;
    }

    public void clearAllTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductContract.QuestionEntity.TABLE_NAME, null, null);
        db.delete(ProductContract.InterestingFactEntity.TABLE_NAME, null, null);
        db.delete(ProductContract.Answer.TABLE_NAME, null, null);
        db.delete(ProductContract.CategoryEntity.TABLE_NAME, null, null);
    }

    private Boolean doesCategoryExist(String categoryName){
        long value = this.getEntityId(ProductContract.CategoryEntity.TABLE_NAME,
                ProductContract.CategoryEntity.COLUMN_TITLE, categoryName);

        return value >= 0;
    }

    private long getEntityId(String tableName, String columnName, String entityName){
        final String SQL_GET_ENTITY = "SELECT " + BaseColumns._ID + " FROM " + tableName + " WHERE "
                + columnName
                + " = '" + entityName + "' LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_ENTITY, null);

        if(cursor.moveToFirst()){
            return cursor.getLong(0);
        }

        return -1;
    }
}
