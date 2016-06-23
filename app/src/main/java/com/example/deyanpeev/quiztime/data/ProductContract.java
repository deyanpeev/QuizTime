package com.example.deyanpeev.quiztime.data;

import android.provider.BaseColumns;

public class ProductContract {
    public static final class CategoryEntity implements BaseColumns {
        public static final String TABLE_NAME = "Categories";

        public static final String COLUMN_TITLE = "Title";
    }

    public static final class QuestionEntity implements BaseColumns {
        public static final String TABLE_NAME = "Questions";
        public static final String COLUMN_CATEGORY_KEY = "Category_ID";
        public static final String COLUMN_INTERESTING_FACT_KEY = "InterestingFact_ID";

        public static final String COLUMN_CONTENT = "Content";
    }

    public static final class InterestingFactEntity implements BaseColumns {
        public static final String TABLE_NAME = "InterestingFacts";

        public static final String COLUMN_SHORT_TAG = "Tag";
        public static final String COLUMN_TEXT = "Text";
    }
}
