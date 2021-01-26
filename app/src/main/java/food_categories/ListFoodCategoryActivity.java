package food_categories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodorderingsystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListFoodCategoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<FoodCategoryItem> mFoodCategoryList;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String URL = "https://www.themealdb.com/api/json/v1/1/categories.php";
    private String strCategory, strCategoryThumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_category);
        mFoodCategoryList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        FetchCategories();

        mAdapter = new FoodCategoryAdapter(getApplicationContext(), mFoodCategoryList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void FetchCategories(){
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("categories");
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject meals = jsonArray.getJSONObject(i);
                        strCategory = meals.getString("strCategory");
                        strCategoryThumb = meals.getString("strCategoryThumb");
                        FoodCategoryItem foodCategoryItem = new FoodCategoryItem();
                        foodCategoryItem.setFoodCategory(strCategory);
                        foodCategoryItem.setFoodPicture(strCategoryThumb);
                        mFoodCategoryList.add(foodCategoryItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }
}