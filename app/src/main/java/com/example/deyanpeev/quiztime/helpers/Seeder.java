package com.example.deyanpeev.quiztime.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.models.AnswerModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

//TODO Complete
public class Seeder {
    private Context context;

    public Seeder(Context context){
        this.context = context;
    }

    public void seedCategories()
            throws XmlPullParserException, IOException {
        StoreDbHelper db = new StoreDbHelper(this.context);
        Resources resources = this.context.getResources();

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
        StoreDbHelper db = new StoreDbHelper(this.context);
        Resources resources = this.context.getResources();

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
                    db.insertNewAnswer(new AnswerModel(answer, category, context));
                }
            }

            eventType = xpp.next();
        }
    }
}

