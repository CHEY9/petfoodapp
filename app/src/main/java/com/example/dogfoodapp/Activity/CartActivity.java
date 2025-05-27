package com.example.dogfoodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.dogfoodapp.Adapter.CartAdapter;
import com.example.dogfoodapp.Domain.ItemsDomain;
import com.example.dogfoodapp.Helper.ChangeNumberItemsListener;
import com.example.dogfoodapp.Helper.ManagmentCart;
import com.example.dogfoodapp.R;
import com.example.dogfoodapp.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {
    private String userEmail;
    private DatabaseReference databaseReference;
    private ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);

        //idk
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userEmail = user.getEmail();

        calculatorCart();
        setVariable();
        initCartList();
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }
        else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
        //idk
        binding.checkoutBtn.setOnClickListener(view -> {
            placeOrder();
            Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

        });
    }

    private void calculatorCart() {
        double precentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee()*precentTax*100.0))/100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) *100 )/100.0;
        double itemTotal = Math.round((managmentCart.getTotalFee() * 100.0))/100.0;

        binding.totalFeeTxt.setText("$"+itemTotal);
        binding.taxTxt.setText("$"+tax);
        binding.delivertTxt.setText("$"+delivery);
        binding.totalTxt.setText("$"+total);

    }


    //idk
    private void placeOrder() {
        double precentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee()*precentTax*100.0))/100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) *100 )/100.0;
        ArrayList<ItemsDomain> cartItems = managmentCart.getListCart(); // Assuming you have a CartItem class

        // Create an order object
        Order order = new Order(userEmail, total, cartItems);

        // Push the order to Firebase database
        databaseReference.push().setValue(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CartActivity.this, "Order saved to database", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CartActivity.this, MainActivity.class);
                        intent.putExtra("userEmail",userEmail);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CartActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //idk
    public static class Order {
        public String userEmail;
        public double total;
        public ArrayList<ItemsDomain> items;

        public Order() {
            // Default constructor required for calls to DataSnapshot.getValue(Order.class)
        }

        public Order(String userEmail, double total, ArrayList<ItemsDomain> items) {
            this.userEmail = userEmail;
            this.total = total;
            this.items = items;
        }
    }
}