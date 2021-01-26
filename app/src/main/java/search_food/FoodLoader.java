package search_food;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class FoodLoader extends AsyncTaskLoader<String> {

    private String mSearchQuery;

    FoodLoader (Context context, String searchString){
        super(context);
        mSearchQuery = searchString;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return FoodUtils.getFoodInfo(mSearchQuery);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
