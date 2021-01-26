package food_review;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;

public class FoodReviewAdapter extends RecyclerView.Adapter<FoodReviewAdapter.FoodReviewViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FoodReviewAdapter(Context ctx, Cursor cursor){
        mContext = ctx;
        mCursor = cursor;
    }
    @NonNull
    @Override
    public FoodReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.food_review, parent, false);
        return new FoodReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodReviewViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) {
            return;
        }
        String comment = mCursor.getString(mCursor.getColumnIndex(FoodReview.ReviewEntry.COLUMN_COMMENT));
        byte[] imageString = mCursor.getBlob(mCursor.getColumnIndex(FoodReview.ReviewEntry.COLUMN_IMAGE));
        long id = mCursor.getLong(mCursor.getColumnIndex(FoodReview.ReviewEntry._ID));
        holder.mReviewView.setText(comment);
        holder.mReviewImage.setImageBitmap(getImage(imageString));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public class FoodReviewViewHolder extends RecyclerView.ViewHolder{

        public TextView mReviewView;
        public ImageView mReviewImage;

        public FoodReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mReviewView = itemView.findViewById(R.id.reviewText);
            mReviewImage = itemView.findViewById(R.id.reviewImage);
        }
    }
}
