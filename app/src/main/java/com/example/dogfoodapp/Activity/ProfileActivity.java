package com.example.dogfoodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogfoodapp.Adapter.OrderAdapter;
import com.example.dogfoodapp.Domain.Order;
import com.example.dogfoodapp.Domain.User;
import com.example.dogfoodapp.R;
import com.example.dogfoodapp.databinding.ActivityCartBinding;
import com.example.dogfoodapp.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {
    private TextView userEmailTextView;
    private TextView usernameTextView, addressTextView, paymentMethodTextView;
    private Button logoutButton,editDetailsBtn ;
    private FirebaseAuth mAuth;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private DatabaseReference userInfoRef;
    private String userKey;
    private ActivityProfileBinding binding;
    String userEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        userEmailTextView = findViewById(R.id.userEmailTextView);
        logoutButton = findViewById(R.id.logoutButton);
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        usernameTextView = findViewById(R.id.usernameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        paymentMethodTextView = findViewById(R.id.paymentMethodTextView);
        editDetailsBtn = findViewById(R.id.editDetailsBtn);


        mAuth = FirebaseAuth.getInstance();
        userInfoRef = FirebaseDatabase.getInstance().getReference("UserInfo");
        FirebaseUser user = mAuth.getCurrentUser();


        if (user != null) {
            userEmail = user.getEmail();
            userKey = userEmail.replace(".", ",");
            userEmailTextView.setText(userEmail);
            fetchOrders(userEmail);
            loadUserInfo();

            logoutButton.setOnClickListener(v -> {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, introActivity.class));
                finish();
            });
        }

        binding.editDetailsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this,UpdateDetailsActivity.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        });

        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
    }

    private void loadUserInfo() {

        userInfoRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                ///
                //Toast.makeText(ProfileActivity.this, user.getUsername(), Toast.LENGTH_SHORT).show();
                ///
                if (user != null) {
                    usernameTextView.setText("User name: " + user.getUsername());
                    addressTextView.setText("Address: " +user.getAddress());
                    paymentMethodTextView.setText("Payment Method: " +user.getPaymentMethod());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    private void fetchOrders(String userEmail) {
        orderList = new ArrayList<>();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    String orderId = orderSnapshot.getKey();
                    order.setOrder(orderId);
                    if (order != null && order.getUserEmail().equals(userEmail)) {
                        orderList.add(order);
                    }
                }
                orderAdapter = new OrderAdapter(orderList);
                ordersRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                ordersRecyclerView.setAdapter(orderAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
