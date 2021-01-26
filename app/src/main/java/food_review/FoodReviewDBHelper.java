package food_review;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class FoodReviewDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "review.db";
    public static final int DATABASE_VERSION = 3;

    //constructor
    public FoodReviewDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table statement with desired column
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
                FoodReview.ReviewEntry.TABLE_NAME + " (" +
                FoodReview.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodReview.ReviewEntry.COLUMN_COMMENT + " TEXT NOT NULL, " +
                FoodReview.ReviewEntry.COLUMN_IMAGE + " BLOB " +
                ");";

        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this will be executed if we changed the database version number
        db.execSQL("DROP TABLE IF EXISTS " + FoodReview.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
