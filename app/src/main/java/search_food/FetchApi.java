package search_food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodorderingsystem.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchApi extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private EditText mEditText;
    private TextView mTextView;
    private Button mButton;
    private SharedPreferences mSharedPreferences;
    LinearLayout mLinear;

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String searchString = "";
        if (args != null) {
            searchString = args.getString("searchString");
        }
        return new FoodLoader(this, searchString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            //convert the response into JSON object
            JSONObject jsonObject = new JSONObject(data);
            //get the JSONArray of food item
            JSONArray foodsArray = jsonObject.getJSONArray("meals");

            //initialize iterator and results field
            int i = 0;
            String foodName = null;
            String foodCategory = null;
            String foodIngredient = null;
            StringBuilder sb = new StringBuilder();

            while (i < foodsArray.length()) {
                JSONObject food = foodsArray.getJSONObject(i);

                try {
                    foodName = food.getString("strMeal");
                    foodCategory = food.getString("strCategory");
                    foodIngredient = food.getString("strIngredient1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (foodCategory != null && foodIngredient != null) {
                    sb.append("Food Name: " + foodName);
                    sb.append("\n");
                    sb.append("Category: " + foodCategory);
                    sb.append("\n");
                    sb.append("Ingredient: " + foodIngredient);
                    sb.append("\n\n");
                    mTextView.setText(sb.toString());
                    mTextView.setMovementMethod(new ScrollingMovementMethod());
                }
                else {
                    Snackbar.make(mLinear, "No food data found", Snackbar.LENGTH_LONG).show();
                    mTextView.setText("");
                }
                i++;
            }

            if(sb.toString().length() != 0) {
                mTextView.setText(sb.toString());
            } else {
                Snackbar.make(mLinear, "No food data found", Snackbar.LENGTH_LONG).show();
                mTextView.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_api);

        mEditText = findViewById(R.id.searchFoodText);
        mTextView = findViewById(R.id.searchTextResult);
        mButton = findViewById(R.id.searchButton);
        mLinear = findViewById(R.id.outerLinerLayout);

        mSharedPreferences = getSharedPreferences("FOODSEARCH", MODE_PRIVATE);

        if (mSharedPreferences.contains("FOODSEARCHNAME")) {
            mEditText.setText(mSharedPreferences.getString("FOODSEARCHNAME", null));
        }

        if (LoaderManager.getInstance(this).getLoader(0) != null) {
            LoaderManager.getInstance(this).initLoader(0, null, this);
        }
    }

    public void searchFood(View view) {
        //get the search string from user's input
        String searchString = mEditText.getText().toString();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("FOODSEARCHNAME", searchString);
        editor.apply();

        //Hide the keyborad when the button is pressed
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search field
        // is not empty, start a BookLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected() && searchString.length() != 0) {
            Bundle searchBundle = new Bundle();
            searchBundle.putString("searchString", searchString);
            LoaderManager.getInstance(this).restartLoader(0, searchBundle, this);
        } else {
            if (searchString.length() == 0) {
                Snackbar.make(mLinear, "Please enter food name input", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(mLinear, "No network connected", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}