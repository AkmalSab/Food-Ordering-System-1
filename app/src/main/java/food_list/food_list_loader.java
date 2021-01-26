package food_list;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class food_list_loader extends AsyncTaskLoader<String> {

    private String categories;

    public food_list_loader(@NonNull Context context, String cat) {
        super(context);
        this.categories = cat;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return FoodListUtils.getFoodList(categories);
    }
}
