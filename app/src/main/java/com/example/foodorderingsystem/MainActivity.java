package com.example.foodorderingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import register.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mainLayout;
    private EditText mUsername, mPassword;
    private FirebaseFirestore mFirestore;
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    String Username = null;
    String Password = null;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mainLayout = findViewById(R.id.mainLayout);

        mSharedPreferences = getSharedPreferences("USERLOGIN", MODE_PRIVATE);

        if(mSharedPreferences.contains("USERNAME") && mSharedPreferences.contains("PASSWORD")){
            mUsername.setText(mSharedPreferences.getString("USERNAME", null));
            mPassword.setText(mSharedPreferences.getString("USERNAME", null));
        }
    }

    public void validateLogin(View view) {

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("USERNAME", username);
        editor.putString("PASSWORD", password);
        editor.apply();

        //Hide the keyborad when the button is pressed
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if (!username.isEmpty() && !password.isEmpty()) {
            mFirestore = FirebaseFirestore.getInstance();
            DocumentReference usersReference = mFirestore.collection("Users").document(username);
            usersReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Username = documentSnapshot.getString(USERNAME);
                                Password = documentSnapshot.getString(PASSWORD);
                                if (Username.equals(username) && Password.equals(password)) {
                                    Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                                    startActivity(intent);
                                } else {
                                    Snackbar.make(mainLayout, "Username or Password may wrong", Snackbar.LENGTH_LONG).show();
                                    mUsername.setText("");
                                    mPassword.setText("");
                                }
                            } else {
                                Snackbar.make(mainLayout, "User does not exist", Snackbar.LENGTH_LONG).show();
                                mUsername.setText("");
                                mPassword.setText("");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(mainLayout, "Error: " + e.toString(), Snackbar.LENGTH_LONG).show();
                            mUsername.setText("");
                            mPassword.setText("");
                        }
                    });
        } else {
            Snackbar.make(mainLayout, "Please fill in your username and password", Snackbar.LENGTH_LONG).show();
        }
    }

    public void openRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}