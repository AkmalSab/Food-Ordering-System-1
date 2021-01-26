package food_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.foodorderingsystem.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListFoodActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private ArrayList<FoodItem> foodList;
    RelativeLayout relativeLayoutOuter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        relativeLayoutOuter = findViewById(R.id.relativeLayoutOuter);

        foodList = new ArrayList<>();
        Intent intent = getIntent();
        Bundle intentData = intent.getExtras();

        if (LoaderManager.getInstance(this).getLoader(0) != null) {
            LoaderManager.getInstance(this).initLoader(0, null, this);
        }

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            Bundle categorySearch = new Bundle();
            categorySearch.putString("categoryString", intentData.get("categoriesClicked").toString());
            LoaderManager.getInstance(this).restartLoader(0, categorySearch, this);
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new FoodAdapter(getApplicationContext(),foodList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String foodCategories = "";
        if (args != null) {
            foodCategories = args.getString("categoryString");
        }
        return new food_list_loader(this, foodCategories);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray foodsArray = jsonObject.getJSONArray("meals");

            int i = 0;
            String foodId = null;
            String foodName = null;
            String foodPicture = null;
            StringBuilder sb = new StringBuilder();
            while (i < foodsArray.length()) {
                JSONObject food = foodsArray.getJSONObject(i);
                try {
                    foodId = food.getString("idMeal");
                    foodName = food.getString("strMeal");
                    foodPicture = food.getString("strMealThumb");
                } catch (JSONException ee) {
                    ee.printStackTrace();
                }

                if (foodName != null && foodPicture != null && foodId != null) {
                    FoodItem foods = new FoodItem(foodName, foodId, foodPicture);
                    foodList.add(foods);
                }
                i++;
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}