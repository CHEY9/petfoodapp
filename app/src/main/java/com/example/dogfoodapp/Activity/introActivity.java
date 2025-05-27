package com.example.dogfoodapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.dogfoodapp.databinding.ActivityIntroBinding;

public class introActivity extends BaseActivity {
    private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            }
        );

    }
}