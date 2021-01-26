package food_categories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import food_list.ListFoodActivity;
import com.example.foodorderingsystem.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder> {

    private Context context;
    private ArrayList<FoodCategoryItem> mFoodCategoryList;

    public FoodCategoryAdapter(Context ctx, ArrayList<FoodCategoryItem> foodCategoryList) {
        this.context = ctx;
        this.mFoodCategoryList = foodCategoryList;
    }

    @NonNull
    @Override
    public FoodCategoryAdapter.FoodCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.food_category_item, parent, false);
        FoodCategoryViewHolder fcv = new FoodCategoryViewHolder(v);
        return fcv;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCategoryAdapter.FoodCategoryViewHolder holder, int position) {
        FoodCategoryItem currentItem = mFoodCategoryList.get(position);
        holder.mTextView1.setText(currentItem.getFoodCategory());
        Picasso.get().load(currentItem.getFoodPicture()).into(holder.mImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "You clicked: " + currentItem.getFoodCategory(), Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(context, ListFoodActivity.class);
                intent.putExtra("categoriesClicked", currentItem.getFoodCategory());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoodCategoryList.size();
    }

    public static class FoodCategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public ImageView mImageView;

        public FoodCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mImageView = itemView.findViewById(R.id.imageview1);
        }
    }
}
