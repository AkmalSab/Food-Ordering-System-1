package food_categories;

public class FoodCategoryItem {

    private String mFoodCategory;
    private String mFoodPicture;

    public FoodCategoryItem() {
    }

    public void setFoodCategory(String category) {
        this.mFoodCategory = category;
    }

    public String getFoodCategory() {
        return mFoodCategory;
    }

    public void setFoodPicture(String picture) {
        this.mFoodPicture = picture;
    }

    public String getFoodPicture() {
        return mFoodPicture;
    }
}
