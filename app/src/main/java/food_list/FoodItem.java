package food_list;

public class FoodItem {

    private String mImageResource;
    private String mText1, mText2;


    public FoodItem(String text1, String text2, String imageResource) {
        mText1 = text1;
        mText2 = text2;
        mImageResource = imageResource;
    }

    public String getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

}
