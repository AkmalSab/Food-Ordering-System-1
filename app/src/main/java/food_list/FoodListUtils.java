package food_list;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FoodListUtils {

    private static final String LOG_TAG = FoodListUtils.class.getSimpleName();
    private static final String FOOD_LIST_BASE_URL = "https://www.themealdb.com/api/json/v1/1/filter.php?";
    private static final String CATEGORY_SEARCH = "c";

    static String getFoodList(String categoryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String foodListJsonString = null;

        try{
            Uri builtURI = Uri.parse(FOOD_LIST_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(CATEGORY_SEARCH, categoryString)
                    .build();

            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }

            if(sb.length() == 0){
                return null;
            }

            foodListJsonString = sb.toString();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, foodListJsonString);
        return foodListJsonString;
    }
}
