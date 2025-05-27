package com.example.dogfoodapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.dogfoodapp.Adapter.PopularAdapter;
import com.example.dogfoodapp.Domain.ItemsDomain;
import com.example.dogfoodapp.Helper.ManagmentCart;
import com.example.dogfoodapp.R;
import com.example.dogfoodapp.databinding.ActivityCartBinding;
import com.example.dogfoodapp.databinding.ActivityItemDisplayBinding;
import com.example.dogfoodapp.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemDisplayActivity extends AppCompatActivity {
    private ActivityItemDisplayBinding binding;
    private SearchView searchView;
    private ArrayList<ItemsDomain> items = new ArrayList<>();
    private PopularAdapter PopularAdapter = new PopularAdapter(items);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();


        setVariable();
        initPopular();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

    }

    private void filterList(String text) {
        ArrayList<ItemsDomain>filteredList = new ArrayList<>();
        for (ItemsDomain item: items){
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No Data Found...",Toast.LENGTH_SHORT).show();
        }
        else {
            PopularAdapter.setFilteredList(filteredList);
            binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(ItemDisplayActivity.this,2));
            binding.recyclerViewPopular.setAdapter(PopularAdapter);
            binding.recyclerViewPopular.setNestedScrollingEnabled(true);
        }
    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("discoverItems");
        binding.progressBarPopular.setVisibility(View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                }
                if (!items.isEmpty()){
                    binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(ItemDisplayActivity.this,2));
                    binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
                    binding.recyclerViewPopular.setNestedScrollingEnabled(true);
                }
                binding.progressBarPopular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
    }
}