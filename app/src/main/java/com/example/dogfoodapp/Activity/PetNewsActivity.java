package com.example.dogfoodapp.Activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dogfoodapp.R;
import com.example.dogfoodapp.databinding.ActivityCartBinding;
import com.example.dogfoodapp.databinding.ActivityPetNewsBinding;

public class PetNewsActivity extends BaseActivity {
    private ActivityPetNewsBinding binding;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPetNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://veterinarypartner.vin.com/");
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
    }
}