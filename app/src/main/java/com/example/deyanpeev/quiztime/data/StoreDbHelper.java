package com.example.deyanpeev.quiztime.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.SimpleCursorAdapter;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.helpers.Constants;
import com.example.deyanpeev.quiztime.models.AnswerModel;
import com.example.deyanpeev.quiztime.models.InterestingFactModel;
import com.example.deyanpeev.quiztime.models.QuestionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
                    "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_ANSWER_KEY + ") REFERENCES " + ProductContract.AnswerEntity.TABLE_NAME + "(" + ProductContract.AnswerEntity._ID + ")," +
                    "FOREIGN KEY (" + ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY + ") REFERENCES " + ProductContract.InterestingFactEntity.TABLE_NAME + "(" + ProductContract.InterestingFactEntity._ID + ")" +
                    ");";

    private final String SQL_CREATE_ANSWER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ProductContract.AnswerEntity.TABLE_NAME + " (" +
                    ProductContract.AnswerEntity._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY + " TEXT NOT NULL, " +
                    ProductContract.AnswerEntity.COLUMN_CONTENT + " VARCHAR NOT NULL UNIQUE, " +
                    "FOREIGN KEY (" + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY + ") REFERENCES " + ProductContract.CategoryEntity.TABLE_NAME + "(" + ProductContract.CategoryEntity._ID + ")" +
                    ");";

    private final String SQL_SELECT_ALL_CATEGORIES = "SELECT " + ProductContract.CategoryEntity.COLUMN_TITLE
            + " FROM " + ProductContract.CategoryEntity.TABLE_NAME;

    private final String SQL_SELECT_IS_THERE_CATEGORIES = "SELECT " + ProductContract.CategoryEntity._ID + " FROM "
            + ProductContract.CategoryEntity.TABLE_NAME + " LIMIT 1";

    private final String SQL_SELECT_ALL_PLAYABLE_CATEGORIES = "SELECT " + ProductContract.CategoryEntity.TABLE_NAME + "." + ProductContract.CategoryEntity.COLUMN_TITLE
            + " FROM " + ProductContract.CategoryEntity.TABLE_NAME
            + " LEFT OUTER JOIN " + ProductContract.QuestionEntity.TABLE_NAME
            + " ON " + ProductContract.CategoryEntity.TABLE_NAME + "." + ProductContract.CategoryEntity._ID
            + " = " + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY

            + " WHERE " + ProductContract.CategoryEntity.TABLE_NAME + "." + ProductContract.CategoryEntity._ID
            + " IN("
            + " SELECT " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
            + " FROM " + ProductContract.AnswerEntity.TABLE_NAME
            + " GROUP BY " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
            + " HAVING COUNT(" + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY + ") >= " + Constants.NUMBER_OF_ANSWER_OPTIONS
            + ")"
            + " GROUP BY " + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY
            + " HAVING COUNT("+ ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY + ") >= " + Constants.NUMBER_OF_QUESTIONS;

    private final String SQL_SELECT_ALL_INTERESTING_FACTS_TAGS = "SELECT " + ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG
            + " FROM " + ProductContract.InterestingFactEntity.TABLE_NAME;

    private final String SQL_SELECT_ALL_QUESTIONS = "SELECT * FROM " + ProductContract.QuestionEntity.TABLE_NAME;

    private final String SQL_SELECT_ALL_ANSWERS = "SELECT * FROM " + ProductContract.AnswerEntity.TABLE_NAME;

    private final String SQL_SELECT_NUMBER_OF_ALL_PLAYABLE_QUESTIONS =
            "SELECT COUNT(" + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity._ID + ")"
            + " FROM " + ProductContract.QuestionEntity.TABLE_NAME
            + " WHERE " + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY + " IN ("
            + " SELECT " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
            + " FROM " + ProductContract.AnswerEntity.TABLE_NAME
            + " LEFT OUTER JOIN " + ProductContract.CategoryEntity.TABLE_NAME
            + " ON " + ProductContract.CategoryEntity.TABLE_NAME + "." + ProductContract.CategoryEntity._ID
            + " = " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
            + " GROUP BY " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
            + " HAVING COUNT (" + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY + ") >= " + Constants.NUMBER_OF_ANSWER_OPTIONS
            + ")";

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
        values.put(ProductContract.AnswerEntity.COLUMN_CONTENT, answer.getContent());
        values.put(ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY, answer.getCategoryId());

        return db.insert(ProductContract.AnswerEntity.TABLE_NAME, null, values);
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

    public List<QuestionModel> getAllQuestions(){
        return this.getQuestions(SQL_SELECT_ALL_QUESTIONS);
    }

    public List<QuestionModel> getRandomPlayQuestions(String category, Resources resorces){
        String query = "SELECT "
                + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_CONTENT + ", "
                + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CONTENT + ", "
                + ProductContract.InterestingFactEntity.TABLE_NAME + "." + ProductContract.InterestingFactEntity.COLUMN_TEXT + ", "
                + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY
                + " FROM " + ProductContract.QuestionEntity.TABLE_NAME
                + " LEFT JOIN " + ProductContract.CategoryEntity.TABLE_NAME
                + " ON " + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY
                + " = " + ProductContract.CategoryEntity.TABLE_NAME + "." + ProductContract.CategoryEntity._ID
                + " LEFT JOIN " + ProductContract.InterestingFactEntity.TABLE_NAME
                + " ON " + ProductContract.InterestingFactEntity.TABLE_NAME + "." + ProductContract.InterestingFactEntity._ID
                + " = " + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_INTERESTING_FACT_KEY
                + " LEFT JOIN " + ProductContract.AnswerEntity.TABLE_NAME
                + " ON " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity._ID
                + " = " + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_ANSWER_KEY;

        if(!category.equals(resorces.getString(R.string.all_elements).toString())){
            query = query + " WHERE " + ProductContract.CategoryEntity.TABLE_NAME + "." + ProductContract.CategoryEntity.COLUMN_TITLE
                    + " = '" + category + "'";
        } else {
            query = query + " WHERE " + ProductContract.QuestionEntity.TABLE_NAME + "." + ProductContract.QuestionEntity.COLUMN_CATEGORY_KEY
                    + " IN (SELECT " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
                    + " FROM " + ProductContract.AnswerEntity.TABLE_NAME
                    + " GROUP BY " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
                    + " HAVING COUNT(" + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
                    + ") >= " + Constants.NUMBER_OF_ANSWER_OPTIONS + ")";
        }

        //getting the exact same number of question as
        query = query + " ORDER BY RANDOM() LIMIT " + Constants.NUMBER_OF_QUESTIONS;

        SQLiteDatabase db = this.getReadableDatabase();

        List<QuestionModel> result = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                String question = cursor.getString(0);
                String answer = cursor.getString(1);
                String interestingFact = cursor.getString(2);
                int categoryId = cursor.getInt(3);

                QuestionModel questionModel = new QuestionModel(question, answer, interestingFact, categoryId);

                result.add(questionModel);
            } while (cursor.moveToNext());
        }

        return result;
    }

    public List<String> getRandomAnswersByCategoryId(long categoryId, String answerToExclude, int limit, Resources resources){
        String query = "SELECT " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CONTENT
                + " FROM " + ProductContract.AnswerEntity.TABLE_NAME
                + " WHERE " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CATEGORY_KEY
                + " = " + categoryId
                + " AND " + ProductContract.AnswerEntity.TABLE_NAME + "." + ProductContract.AnswerEntity.COLUMN_CONTENT
                + " != '" + answerToExclude + "'"
                + " ORDER BY RANDOM() LIMIT " + limit;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<String> result = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                String answer = cursor.getString(0);
                result.add(answer);
            } while(cursor.moveToNext());
        }

        return result;
    }

    public boolean couldRunGame(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_NUMBER_OF_ALL_PLAYABLE_QUESTIONS, null);

        if(cursor.moveToFirst()){
            int numberOfQuestion = cursor.getInt(0);
            return numberOfQuestion >= Constants.NUMBER_OF_QUESTIONS;
        }
        return false;
    }

    public List<AnswerModel> getAllModels(Context context){
        List<AnswerModel> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_ANSWERS, null);

        if(cursor.moveToFirst()){
            do{
                result.add(new AnswerModel(cursor.getString(2), cursor.getLong(1)));//cursor.getString(1), cursor.getLong(2), cursor.getLong(3),cursor.getLong(4)));
            }while(cursor.moveToNext());
        }

        return result;
    }

    public List<String> getAllCategories(){
        return this.getCategories(SQL_SELECT_ALL_CATEGORIES);
    }

    //Each category must have more than 10 questions to be played
    public List<String> getAllPlayableCategories(){
        return this.getCategories(SQL_SELECT_ALL_PLAYABLE_CATEGORIES);
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
        return this.getEntityIdByString(ProductContract.InterestingFactEntity.TABLE_NAME,
                ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG, interestingFact.getShortTag()) >= 0;
    }

    public long getCategoryId(String categoryName){
        long id = this.getEntityIdByString(ProductContract.CategoryEntity.TABLE_NAME,
                ProductContract.CategoryEntity.COLUMN_TITLE, categoryName);

        if(id >= 0){
            return id;
        } else {
            throw new SQLException("No such interesting fact.");
        }
    }

    public String getCategoryTitleById(long id){
        return this.getEntityStringById(ProductContract.CategoryEntity.TABLE_NAME,
                ProductContract.CategoryEntity.COLUMN_TITLE, id);
    }

    public String getAnswerContentById(long id){
        return this.getEntityStringById(ProductContract.AnswerEntity.TABLE_NAME,
                ProductContract.AnswerEntity.COLUMN_CONTENT, id);
    }

    public String getInterestingFactTextById(long id){
        return this.getEntityStringById(ProductContract.InterestingFactEntity.TABLE_NAME,
                ProductContract.InterestingFactEntity.COLUMN_TEXT, id);
    }

    public boolean isThereCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT_IS_THERE_CATEGORIES, null);

        if(cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public long getInterestingFactId(String interestingFactName) throws SQLException {
        long id = this.getEntityIdByString(ProductContract.InterestingFactEntity.TABLE_NAME,
                ProductContract.InterestingFactEntity.COLUMN_SHORT_TAG, interestingFactName);

        if(id >= 0){
            return id;
        } else {
            throw new SQLException("No such interesting fact.");
        }
    }

    public Long getOrCreateAnswerId(String answer, long categoryId){
        long answerId = getEntityIdByString(ProductContract.AnswerEntity.TABLE_NAME,
                ProductContract.AnswerEntity.COLUMN_CONTENT, answer);

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
        db.delete(ProductContract.AnswerEntity.TABLE_NAME, null, null);
        db.delete(ProductContract.CategoryEntity.TABLE_NAME, null, null);
    }

    private Boolean doesCategoryExist(String categoryName){
        long value = this.getEntityIdByString(ProductContract.CategoryEntity.TABLE_NAME,
                ProductContract.CategoryEntity.COLUMN_TITLE, categoryName);

        return value >= 0;
    }

    private String getEntityStringById(String tableName, String returnType, long id){
        final String SQL_GET_ENTITY = "SELECT " + returnType + " FROM " + tableName + " WHERE "
                + BaseColumns._ID
                + " = " + id + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_ENTITY, null);

        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }

        throw new SQLException("Entity with such id doesn't exist.");
    }

    private long getEntityIdByString(String tableName, String columnName, String entityName){
        final String SQL_GET_ENTITY = "SELECT " + BaseColumns._ID + " FROM " + tableName + " WHERE "
                + columnName
                + " = ? COLLATE NOCASE LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_ENTITY, new String[] {entityName});

        if(cursor.moveToFirst()){
            Long id = cursor.getLong(0);
            return id;
        }

        return -1;
    }

    private List<String> getCategories(String query){
        List<String> result = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                result.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        return result;
    }

    private List<QuestionModel> getQuestions(String query){
        List<QuestionModel> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                result.add(new QuestionModel(cursor.getString(1), cursor.getLong(2), cursor.getLong(3),cursor.getLong(4)));
            }while(cursor.moveToNext());
        }

        return result;
    }
}
