package food_detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodorderingsystem.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import food_categories.FoodCategoryItem;
import submit_order.SubmitOrderActivity;

public class FoodDetailsActivity extends AppCompatActivity {

    ConstraintLayout cl;
    private ImageView mFoodPicture;
    private TextView mFoodName, mFoodCategory, mFoodOrigin, mFoodInstruction, mFoodInstructionContent;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String URL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
    private String strMeal, strCategory, strArea, strInstructions, strMealThumb, foodId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        cl = findViewById(R.id.constraintLayout);
        mFoodPicture = findViewById(R.id.foodPicture);
        mFoodName = findViewById(R.id.foodName);
        mFoodCategory = findViewById(R.id.foodCategory);
        mFoodOrigin = findViewById(R.id.foodOrigin);
        mFoodInstruction = findViewById(R.id.foodInstructionHeader);
        mFoodInstructionContent = findViewById(R.id.foodInstructionContent);

        Intent intent = getIntent();
        Bundle intentData = intent.getExtras();
        foodId = intentData.getString("foodID");
//        Snackbar.make(cl, "You clicked: " + intentData.getString("foodID").toString(), Snackbar.LENGTH_LONG).show();
        FetchFoodDetails();
    }

    private void FetchFoodDetails(){
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL+foodId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("meals");
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject meals = jsonArray.getJSONObject(i);
                        strMeal = meals.getString("strMeal");
                        strCategory = meals.getString("strCategory");
                        strArea = meals.getString("strArea");
                        strInstructions = meals.getString("strInstructions");
                        strMealThumb = meals.getString("strMealThumb");

                        Picasso.get().load(strMealThumb).into(mFoodPicture);
                        mFoodName.setText(strMeal);
                        mFoodCategory.setText("Food Category: " + strCategory);
                        mFoodOrigin.setText("Food Origin: " + strArea);
                        mFoodInstructionContent.setText(strInstructions);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    public void orderFood(View view) {
        String foodName = mFoodName.getText().toString();
        Intent intent = new Intent(this, SubmitOrderActivity.class);
        intent.putExtra("foodName", foodName);
        startActivity(intent);
    }
}