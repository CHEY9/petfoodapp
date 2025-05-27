package com.example.dogfoodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dogfoodapp.Domain.User;
import com.example.dogfoodapp.R;
import com.example.dogfoodapp.databinding.ActivityProfileBinding;
import com.example.dogfoodapp.databinding.ActivityUpdateDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDetailsActivity extends BaseActivity {
    private ActivityUpdateDetailsBinding binding;
    private DatabaseReference userInfoRef;
    private String userEmail, userKey;
    private EditText usernameEditText, addressEditText, paymentMethodEditText;
    private Button saveDetailsBtn;
    String[] items =  {"Visa / Master Debit Card","Credit Card","Cash On Delivery","Paypal","KoKo Pay"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    String updatedPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        saveDetailsBtn = findViewById(R.id.saveDetailsBtn);
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        userKey = userEmail.replace(".", ",");
        usernameEditText = findViewById(R.id.usernameEditTex);
        addressEditText = findViewById(R.id.addressEditText);

        //drop Down Menu
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatedPaymentMethod = parent.getItemAtPosition(position).toString();
            }
        });

        setVariable();

        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }




private void saveUserInfo() {

        userInfoRef = FirebaseDatabase.getInstance().getReference("UserInfo");

        String updatedUsername = usernameEditText.getText().toString().trim();
        String updatedAddress = addressEditText.getText().toString().trim();
        // String updatedPaymentMethod = paymentMethodEditText.getText().toString().trim();

        if (updatedUsername.isEmpty() || updatedAddress.isEmpty() || updatedPaymentMethod == null || updatedPaymentMethod.isEmpty()) {
            Toast.makeText(UpdateDetailsActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }


        User updatedUser = new User(updatedUsername, updatedAddress, updatedPaymentMethod);

        userInfoRef.child(userKey).setValue(updatedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateDetailsActivity.this, "User information updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateDetailsActivity.this, "Failed to update user information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
    }
}