package submit_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.foodorderingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SubmitOrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView mDate, mTime;
    private EditText mOrderText, mQuantity, mAdress;
    private FirebaseFirestore mFirestore;
    private SharedPreferences mSharedPreferences;
    private String foodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        mOrderText = findViewById(R.id.orderText1);
        mQuantity = findViewById(R.id.orderText2);
        mAdress = findViewById(R.id.orderText3);
        mDate = findViewById(R.id.textView1);
        mTime = findViewById(R.id.textView2);

        Intent intent = getIntent();
        Bundle intentData = intent.getExtras();


        mSharedPreferences = getSharedPreferences("USERSUBMITINFO", MODE_PRIVATE);

        if(intentData != null){
            foodName = intentData.getString("foodName");
            mOrderText.setText(foodName);
        }
        else {
            if (mSharedPreferences.contains("ORDER")) {
                mOrderText.setText(mSharedPreferences.getString("ORDER", null));
            }
        }

        //Initialize firebase
        mFirestore = FirebaseFirestore.getInstance();
    }


    public void chooseDate(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        mDate.setText(currentDateString);
    }

    public void chooseTime(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTime.setText(hourOfDay + ":" + minute);
    }

    public void submitOrder(View view) {
        String order = mOrderText.getText().toString();
        int quantity = Integer.parseInt(mQuantity.getText().toString());
        String address = mAdress.getText().toString();
        String date = mDate.getText().toString();
        String time = mTime.getText().toString();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("ORDER", order);
        editor.apply();

        //Hide keyboard after user has pressed the button
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        //Enter the user's input into HashMap
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("Food Name", order);
        userMap.put("Quantity", quantity);
        userMap.put("Address", address);
        userMap.put("Date", date);
        userMap.put("Time", time);


        //Add the user's input into firebase firestore
        mFirestore.collection("Orders").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(SubmitOrderActivity.this, "Order is submitted", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(SubmitOrderActivity.this, "Error : " + e, Toast.LENGTH_LONG).show();
            }
        });

        mFirestore.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
    }
}