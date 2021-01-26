package register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderingsystem.MainActivity;
import com.example.foodorderingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName, mEmail, mPhone, mUsername, mPassword, mConfirm;
    LinearLayout mLinear;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mLinear = findViewById(R.id.registerLinearLayout);
        mName = findViewById(R.id.nameText);
        mEmail = findViewById(R.id.emailText);
        mPhone= findViewById(R.id.phoneText);
        mUsername = findViewById(R.id.usernameText);
        mPassword= findViewById(R.id.passwordText);
        mConfirm= findViewById(R.id.confirmPasswordText);

        //Initialize firebase
        mFirestore = FirebaseFirestore.getInstance();

    }

    public void registerUser(View view) {
        //Hide keyboard after user has pressed the button
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        String Password = mPassword.getText().toString();
        String Confirm = mConfirm.getText().toString();
        String Name = mName.getText().toString();
        String Email = mEmail.getText().toString();
        String Username = mUsername.getText().toString();
        String Phone = mPhone.getText().toString();

        if(!Password.equals(Confirm)){
            Snackbar.make(mLinear, "Password and Confirm Password are matched!", Snackbar.LENGTH_LONG).show();
        }

        if(Password.equals(Confirm) && !Name.isEmpty() && !Email.isEmpty() && !Username.isEmpty()){
            Map<String, Object> users = new HashMap<>();
            users.put("Username", Username);
            users.put("Password", Password);
            users.put("Name", Name);
            users.put("Email", Email);
            users.put("Phone", Phone);

            mFirestore.collection("Users").document(Username).set(users)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(mLinear, "User successfully registered!", Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(mLinear, "User registration failed!", Snackbar.LENGTH_LONG).show();
                        }
                    });

            mFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("TAG", "Error getting documents .", task.getException());
                    }
                }
            });

        } else {
            Snackbar.make(mLinear, "Do not leave any input blank!", Snackbar.LENGTH_LONG).show();
        }
    }

    public void goBacktoLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}