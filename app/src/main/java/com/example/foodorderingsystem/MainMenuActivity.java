package com.example.foodorderingsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import food_categories.ListFoodCategoryActivity;
import food_list.ListFoodActivity;
import food_review.ReviewFoodActivity;
import search_food.FetchApi;
import submit_order.SubmitOrderActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void openFetchApi(View view) {
        Intent intent2 = new Intent(this, FetchApi.class);
        startActivity(intent2);
    }

    public void showFoodCategory(View view) {
        Intent intent3 = new Intent(this, ListFoodCategoryActivity.class);
        startActivity(intent3);
    }

    public void orderFood(View view) {
        Intent intent4 = new Intent(this, submit_order.SubmitOrderActivity.class);
        startActivity(intent4);
    }

    public void openReview(View view) {
        Intent intent5 = new Intent(this, food_review.ReviewFoodActivity.class);
        startActivity(intent5);
    }
}