package food_review;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.foodorderingsystem.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class ReviewFoodActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private FoodReviewAdapter mAdapter;
    LinearLayout mLinear;
    ImageView mImageView;
    Button mOpenCamera, mSubmitImage;
    EditText mReviewComment;
    Bitmap captureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_food);

        mLinear = findViewById(R.id.outerLinerLayout);
        mImageView = findViewById(R.id.imageFromCamera);
        mOpenCamera = findViewById(R.id.openCamera);
        mSubmitImage = findViewById(R.id.submitPicture);
        mReviewComment = findViewById(R.id.reviewComment);

        //request for camera permission
        if (ContextCompat.checkSelfPermission(ReviewFoodActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReviewFoodActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
        }

        //if we want to insert into database
        FoodReviewDBHelper dbHelper = new FoodReviewDBHelper(this);
        //call writable db not readable
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FoodReviewAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

    }

    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            //get capture image
            captureImage = (Bitmap) data.getExtras().get("data");
            //set capture image to ImageView
            mImageView.setImageBitmap(captureImage);

        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }



    public void submitImage(View view) {

        if (mReviewComment.getText().toString().trim().length() == 0){
            Snackbar.make(mLinear, "Please enter comment input", Snackbar.LENGTH_LONG).show();
        }

        if (mImageView.getDrawable() == null){
            Snackbar.make(mLinear, "Please send a picture", Snackbar.LENGTH_LONG).show();
        }

        if (mReviewComment.getText().toString().trim().length() > 0 && mImageView.getDrawable() != null) {
            String comment = mReviewComment.getText().toString();
            ContentValues cv = new ContentValues();
            //this will make sure we add data values to respective column
            cv.put(FoodReview.ReviewEntry.COLUMN_COMMENT, comment);
            cv.put(FoodReview.ReviewEntry.COLUMN_IMAGE, getBytes(captureImage));
            //insert data into db
            mDatabase.insert(FoodReview.ReviewEntry.TABLE_NAME, null, cv);
            mAdapter.swapCursor(getAllItems());
            //reset the edit text values
            mReviewComment.getText().clear();

            Snackbar.make(mLinear, "Review has been submitted", Snackbar.LENGTH_LONG).show();
        }
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                FoodReview.ReviewEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FoodReview.ReviewEntry._ID + " DESC"
        );
    }

    private void removeItem(long id){
        mDatabase.delete(FoodReview.ReviewEntry.TABLE_NAME, FoodReview.ReviewEntry._ID + " = " + id, null);
        mAdapter.swapCursor(getAllItems());
    }
}