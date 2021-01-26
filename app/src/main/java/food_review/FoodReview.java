package food_review;

import android.provider.BaseColumns;

public class FoodReview {

    //constructor
    private FoodReview(){}

    //table
    public static final class ReviewEntry implements BaseColumns {

        //table name
        public static final String TABLE_NAME = "review";
        //column name
        public static final String COLUMN_COMMENT = "comment";
        //column name
        public static final String COLUMN_IMAGE = "image";
    }
}
