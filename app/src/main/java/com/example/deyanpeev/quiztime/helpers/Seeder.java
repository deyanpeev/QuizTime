package com.example.deyanpeev.quiztime.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.SQLException;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.models.AnswerModel;
import com.example.deyanpeev.quiztime.models.InterestingFactModel;
import com.example.deyanpeev.quiztime.models.QuestionModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Seeder {
    //TODO create test mode for buttons
    //public static final boolean

    private Context context;
    private StoreDbHelper db;
    private Resources resources;

    public Seeder(Context context){
        this.context = context;
        db = new StoreDbHelper(this.context);
        resources = this.context.getResources();
    }

    public void seedCategories() throws XmlPullParserException, IOException {
        XmlResourceParser xpp = resources.getXml(R.xml.categories);
        xpp.next();

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_category_tag))) {
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    String category = xpp.getText();
                    db.insertNewCategory(category);
                }
            }

            eventType = xpp.next();
        }
    }

    //TODO test
    public void seedAnswers() throws XmlPullParserException, IOException {
        XmlResourceParser xpp = resources.getXml(R.xml.answers);
        xpp.next();

        String category = null;

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_category_name_tag))) {
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    category = xpp.getText();
                }
            }
            else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_answer_tag))){
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    String answer = xpp.getText();
                    try {
                        db.insertNewAnswer(new AnswerModel(answer, category, context));
                    } catch (StringIndexOutOfBoundsException | SQLException e){
                         //Eighter the answer is too long or SQLite database connection failed - should move on
                    }
                }
            }

            eventType = xpp.next();
        }
    }

    public void seedInterestingFacts() throws XmlPullParserException, IOException {
        XmlResourceParser xpp = resources.getXml(R.xml.interesting_facts);
        xpp.next();

        String shortTag = null;
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_short_tag))) {
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    shortTag = xpp.getText();
                }
            }
            else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_description_tag))){
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    String descrption = xpp.getText();
                    db.insertNewInterestingFact(new InterestingFactModel(shortTag, descrption));
                }
            }

            eventType = xpp.next();
        }
    }

    public void seedQuestions() throws XmlPullParserException, IOException {
        XmlResourceParser xpp = resources.getXml(R.xml.questions);
        xpp.next();

        String questionContent = null;
        String answer = null;
        String category = null;
        String interestingFact = null;

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_question_content))) {
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    questionContent = xpp.getText();
                }
            }
            else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_answer_tag))){
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    answer = xpp.getText();
                }
            }
            else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_category_tag))){
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    category = xpp.getText();
                }
            }
            else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals(resources.getString(R.string.xml_interesting_fact_tag))) {
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    answer = xpp.getText();
                }
            }
            else if(eventType == XmlPullParser.END_TAG && xpp.getName().equals(resources.getString(R.string.xml_question_tag))){
                if(questionContent != null && answer != null && category != null) {
                    //insert into the database
                    try {
                        db.insertNewQuestion(new QuestionModel(questionContent, category, answer, interestingFact, context));
                    } catch (Exception e) {
                        //continue witouth adding
                    }
                }

                //Set default value to everything
                questionContent = null;
                answer = null;
                category = null;
                interestingFact = null;
            }

            eventType = xpp.next();
        }
    }
}

