package food_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingsystem.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import food_detail.FoodDetailsActivity;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private ArrayList<FoodItem> mFoodList;
    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView1, mTextView2;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageview1);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }

    public FoodAdapter(Context ctx, ArrayList<FoodItem> foodList) {
        context = ctx;
        mFoodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        FoodViewHolder fvh = new FoodViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem currentItem = mFoodList.get(position);
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        Picasso.get().load(currentItem.getImageResource()).into(holder.mImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "You clicked: " + currentItem.getText2(), Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(context, FoodDetailsActivity.class);
                intent.putExtra("foodID", currentItem.getText2());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}
