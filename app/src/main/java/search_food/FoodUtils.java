package search_food;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FoodUtils {

    private static final String LOG_TAG = FoodUtils.class.getSimpleName();
    private static final String FOOD_BASE_URL = "https://www.themealdb.com/api/json/v1/1/search.php?";
    private static final String SEARCH_NAME = "s";

    static String getFoodInfo(String searchString){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String foodJSONString = null;
        String line = null;

        try{
            // Build the full query URI,
            Uri builtURI = Uri.parse(FOOD_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(SEARCH_NAME, searchString)
                    .build();

            // Convert the URI to a URL.
            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //get the input stream
            InputStream inputStream = urlConnection.getInputStream();

            //create a buffered reader from the input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //use a string builder to hold incoming response
            StringBuilder builder = new StringBuilder();

            while((line = reader.readLine()) != null){
                //add the current line to the string
                builder.append(line);
                builder.append("\n");
            }

            if(builder.length() == 0){
                //stream is empty
                return null;
            }

            foodJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //close the connection and buffered reader
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, foodJSONString);
        return foodJSONString;
    }
}
